public class Solver {

    static void solve (Grid g) { // select what algorithms will be executed
        presolve(g); // solve the trivial cases
        algorithmX(g); // runs Knuths Algorithm X
    }

    private static void algorithmX (Grid g) {
        byte[][] matrix = new byte [729][324];

        for (int i = 0; i < 729; i++) { //init matrix
            for (int j = 0; j < 324; j++) {
                matrix[i][j] = 0;
            }
        }

        for (int i = 0; i < 729; i++) { // row-colum constrain
            matrix[i][(int) (i/9)] = 1;
        }

        for (int i = 0; i < 729; i++) { // row-number and column-number constrain
            matrix[i][(i%9) + 81 + ((int)i/81)*9] = 1;
            matrix[i][(i%81) + 162 /*+ ((int)i/81)*9*/] = 1;
        }

        //box-number constrain
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix[i*3*9 + j + (((int) i/3)*162)][243 + i*9 + j] = 1;
                matrix[i*3*9 + j + 9 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 2
                matrix[i*3*9 + j + 18 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 3
                matrix[i*3*9 + j + 81 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 4
                matrix[i*3*9 + j + 90 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 5
                matrix[i*3*9 + j + 99 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 6
                matrix[i*3*9 + j + 162 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 7
                matrix[i*3*9 + j + 171 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 8
                matrix[i*3*9 + j + 180 + (((int) i/3)*162)][243 + i*9 + j] = 1; // cell 9
            }
        }

        //update matrix with current values in sudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (g.getValue(i,j) != 0) {
                    updateMatrix(i,j,g.getValue(i,j),matrix);
                }
            }
        }

        boolean[] presentRows = new boolean[729];
        boolean[] solution = new boolean[729];
        boolean[] presentCols = new boolean[324];

        for (int i = 0; i < 729; i++) {
            presentRows[i] = true;
            solution[i] = false;
        }

        for (int i = 0; i < 324; i++) {
            presentCols[i] = true;
        }

        boolean ret = executeAlgorithm(matrix, presentRows, presentCols, solution, -1,0);

        if (ret) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0 ; j < 9; j++) {
                    for (int k = 0; k < 9; k++) {
                        if (solution[81*i + 9*j + k])
                        g.setValue(i, j, k + 1);
                    }
                }
            }
        }

    }

    private static boolean executeAlgorithm (byte[][] matrix, boolean[] presentRows, boolean[] presentCols, boolean[] solution, int currentRow, int level) {
//        System.out.println("executou o algoritmo: " + level);
        boolean[] solutionNew = new boolean[729];
        boolean[] presentRowsNew = new boolean[729];
        boolean[] presentColsNew = new boolean[324];
        boolean flag;
        int count;
        int minCol = 1000;
        int minValue = 1000;

        System.arraycopy(presentRows, 0, presentRowsNew, 0, 729);
        System.arraycopy(presentCols, 0, presentColsNew, 0, 324);
        System.arraycopy(solution, 0, solutionNew, 0, 729);

        if (currentRow != -1) {
            solutionNew[currentRow] = true;
            for (int j = 0; j < 324; j++) {
                if (matrix[currentRow][j] == 1 && presentColsNew[j]) {
                    for (int i = 0; i < 729; i++) {
                        if (presentRowsNew[i] && matrix[i][j] == 1) {
                            presentRowsNew[i] = false;
                        }
                    }
                    presentColsNew[j] = false;
                }
            }
        }

        // check if matrix is empty
        flag = true;
        for (int i = 0; i < 324; i++) {
            if (presentColsNew[i])
                flag = false;
        }
        if (flag) {
            // acabou o algoritmo
            System.out.println("acabou o algoritmo certo");
            System.arraycopy(presentRowsNew, 0, presentRows, 0, 729);
            System.arraycopy(presentColsNew, 0, presentCols, 0, 324);
            System.arraycopy(solutionNew, 0, solution, 0, 729);
            return true;
        }

        // select column
        for (int j = 0; j < 324; j++) {
            if (presentColsNew[j]) {
                count = 0;
                for (int i = 0; i < 729; i++) {
                    if (presentRowsNew[i])
                        count += matrix[i][j];
                }
                if (count < minValue) {
                    minValue = count;
                    minCol = j;
                }
            }
        }

        if (minValue == 0 || minValue == 1000) {
            System.out.println("retornou false meio");
            return false;
        }


        // select row
        for (int i = 0; i < 729; i++) {
            if (presentRowsNew[i] && matrix[i][minCol] == 1) {
                System.out.println("level: "+level + " coluna: " + minCol + " linha: " + i);
                flag = executeAlgorithm(matrix, presentRowsNew, presentColsNew, solutionNew, i, level + 1);
                if (flag) {
                    // update all and return true
                    System.out.println("acabou o algoritmo certo");
                    System.arraycopy(presentRowsNew, 0, presentRows, 0, 729);
                    System.arraycopy(presentColsNew, 0, presentCols, 0, 324);
                    System.arraycopy(solutionNew, 0, solution, 0, 729);
                    return true;
                } else {
                    continue;
                }
            }
        }
        System.out.println("retornou false final");
        return false;
    }

    private static void updateMatrix(int x, int y,int val, byte[][] matrix) {
        int rowIdx = 81*x + 9*y + val - 1;

        // update row-column
        for (int i = (rowIdx - val + 1); i < (rowIdx - val + 9 + 1); i++) {
            for (int j = 0; j < 324; j++) {
                if (i != rowIdx) {
                    matrix[i][j] = 0;
                }
            }
        }

        // update row-number
        for (int i = 0; i < 729; i++) {
            if (i != rowIdx) {
                matrix[i][81 + val - 1 + (9*x)] = 0;
            }
        }
        for (int i = (x*81) + val - 1; i < (x*81) + val - 1 + 81; i+=9) {
            for (int j = 0; j < 81; j++) {
                if (i != rowIdx) {
                    matrix[i][j] = 0;
                }
            }
        }

        // update column-number
        for (int i = 0; i < 729; i++) {

            if (i != rowIdx) {
                matrix[i][162 + val - 1 + (9*y)] = 0;
            }
        }
        for (int i = (y*9) + val - 1; i < 729; i+=81) {
            for (int j = 0; j < 81; j++) {
                if (i != rowIdx) {
                    matrix[i][j] = 0;
                }
            }
        }

        // update box-number
        int gridIdx = ((int) (x/3)) * 3 + ((int) (y/3));
        for (int i = 0; i < 729; i++) {
            if (i != rowIdx && matrix[i][243 + val-1+ (gridIdx*9)] == 1) {
                matrix[i][243 + val-1+ (gridIdx*9)] = 0;
                for (int j = 0; j < 81; j++) {
                    matrix[i][j] = 0;
                }
            }
        }

    }

    private static void simulatedAnnealing (Grid g) {
        int currentEnergy = 1000, neiEnergy = 1000, count = 0;
        int numMutations = 10;
        double tunnelingValue = 0.9;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (g.getValue(i,j) == 0) {
                    count++;
                }
            }
        }

        boolean[][] isLockedCell = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (g.getValue(i,j) == 0) {
                    isLockedCell[i][j] = false;
                } else {
                    isLockedCell[i][j] = true;
                }
            }
        }

        initIndividual(g, isLockedCell);

        Grid nextGrid = new Grid(g);

        for (int i = 0; i < 20; i++) {
            System.out.println("current index: "+i + " - current energy: "+currentEnergy);
            for (int j = 0; j < (count * count); j++) {
                mutation(nextGrid, isLockedCell, numMutations);
                currentEnergy = fitness(g);
                neiEnergy = fitness(nextGrid);
                if (neiEnergy < currentEnergy) {
                    // copy nextGrid in current grid
                    for (int k = 0; k < 9; k++) {
                        for (int m = 0; m < 9; m++) {
                            g.setValue(k,m,nextGrid.getValue(k,m));
                        }
                    }
                } else {
                    // generate random number to check if should copy
                    if (Math.random() < Math.exp(-(neiEnergy - currentEnergy)/tunnelingValue)) {
                        // copy nextGrid in current grid
                        for (int k = 0; k < 9; k++) {
                            for (int m = 0; m < 9; m++) {
                                g.setValue(k,m,nextGrid.getValue(k,m));
                            }
                        }
                    }
                }
            }
            numMutations--;
            if (numMutations < 1)
                numMutations = 1;
            tunnelingValue *= 0.8;
        }
        g.printGrid();
    }

    private static void geneticAlg (Grid g) {
        // create pop
        int popSize = 100;
        Grid[] population = new Grid[popSize];
        boolean[][] isLockedCell = new boolean[9][9];

        int[] fitByIdv = new int[popSize];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (g.getValue(i,j) == 0) {
                    isLockedCell[i][j] = false;
                } else {
                    isLockedCell[i][j] = true;
                }
            }
        }

        for (int i = 0; i < popSize; i++) {
            population[i] = new Grid(g);
            population[i] = initIndividual(population[i], isLockedCell);
        }

        int bestFit = 1000;
        int bestIdv = 1000;
        int currentFit;

        for (int i = 0; i < popSize; i++) {
            currentFit = fitness(population[i]);
            fitByIdv[i] = currentFit;
            if (currentFit < bestFit) {
                bestFit = currentFit;
                bestIdv = i;
            }
        }

        int count = 0;

        while (bestFit > 0) {
            if (count % 1000000 == 0) {
                System.out.println("Generation: "+count + " - bestFit: " + bestFit);
                population[bestIdv].printGrid();
            }

            for (int i = 0; i < popSize; i++) {
                mutation(population[i], isLockedCell, (int) ((bestFit + 1)/2));
            }


            for (int i = 0; i < popSize; i++) { // search for current best fit
                currentFit = fitness(population[i]);
                fitByIdv[i] = currentFit;
                if (currentFit < bestFit) {
                    bestFit = currentFit;
                    bestIdv = i;
                }
            }
            count++;
        }

        System.out.println("ACABOU O ALG GEN");
        population[bestIdv].printGrid();
    }

    private static void mutation (Grid g, boolean[][] isLockedCell, int numMutations) {
        int randX, randY;
        int numValidMutCells;
        int aux;

        while (numMutations != 0) {

            // seleciona um subgrid
            randX = (int) (Math.random() * ( 3 )) * 3;
            randY = (int) (Math.random() * ( 3 )) * 3;
            // seleciona celulas para realizar mutacao
            numValidMutCells = 0;
            for (int i = randX; i < randX + 3; i++) {
                for (int j = randY; j < randY + 3; j++) {
                    if (!isLockedCell[i][j]) {
                        numValidMutCells++;
                    }
                }
            }
            // muta
            int firstCell = (int) (Math.random() * ( numValidMutCells )) + 1;
            int secondCell = (int) (Math.random() * ( numValidMutCells )) + 1;

            int firstCellX = 0, firstCellY = 0, secondCellX = 0, secondCellY = 0;

            if (firstCell != secondCell) {

                for (int i = randX; i < randX + 3; i++) {
                    for (int j = randY; j < randY + 3; j++) {
                        if (!isLockedCell[i][j]) {
                            firstCell--;
                            if (firstCell == 0) {
                                firstCellX = i;
                                firstCellY = j;
                            }
                            secondCell--;
                            if (secondCell == 0) {
                                secondCellX = i;
                                secondCellY = j;
                            }
                        }
                    }
                }

                aux = g.getValue(secondCellX, secondCellY);
                g.setValue(secondCellX, secondCellY, g.getValue(firstCellX, firstCellY));
                g.setValue(firstCellX, firstCellY, aux);
//                System.out.println("mutacao ocorreu! Primeira celula: " + firstCellX + " " + firstCellY + " --- Segunda: " + secondCellX + " " + secondCellY);
            }

            numMutations--;
        }

    }

    private static Grid initIndividual(Grid g, boolean[][] isLockedCell) {

        boolean[] missingValues = new boolean[10];

        for (int x = 0; x < 9; x+=3) { // for each subgrid
            for (int y = 0; y < 9; y+=3) {

                //clear missing values list
                for (int i = 0; i < 10; i++) {
                    missingValues[i] = true;
                }
                missingValues[0] = false;

                for (int i = x; i < x+3; i++) { // find missing values in the current subgrid
                    for (int j = y; j < y + 3; j++) {
                        missingValues[g.getValue(i,j)] = false;
                    }
                }

                for (int i = x; i < x+3; i++) {
                    for (int j = y; j < y + 3; j++) {
                        if (g.getValue(i,j) == 0 && !isLockedCell[i][j]) {
                            g.setValue(i,j, getRandomMissing(missingValues));
                        }
                    }
                }
            }
        }


        return g;
    }

    private static int getRandomMissing(boolean[] missingValues) {

        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (missingValues[i])
                count++;
        }

        count = (int) (Math.random() * ( count )) + 1;
        for(int i = 1; i < 10; i++) {
            if (missingValues[i]) {
                count--;
            }
            if (count == 0) {
                missingValues[i] = false;
                return i;
            }
        }
        missingValues[0] = false;
        return 0;
    }

    private static void presolve (Grid grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid.getGridPosNumPossibleValues(i,j) == 1) {
                    grid.fillPosition(i,j);
                    grid.updateGrid(i,j);
                    i = 0;
                    j = 0;
                }
            }
        }

    }

    private static int fitness (Grid g) {
        int count = 0;
        int[] valuesList = new int[10];

        for (int i = 0 ; i < 9; i++) { // for each row
            for (int j = 0; j < 10; j++) { //clear valuesList
                valuesList[j] = 0;
            }

            for (int j = 0; j < 9; j++) { // run throw each row and count number of values found
                valuesList[g.getValue(i,j)]++;
            }

            for (int j = 1; j < 10; j++) {
                if (valuesList[j] != 1)
                    count++;
            }
        }
        for (int i = 0 ; i < 9; i++) { // for each col
            for (int j = 0; j < 10; j++) { //clear valuesList
                valuesList[j] = 0;
            }

            for (int j = 0; j < 9; j++) { // run throw each col and count number of values found
                valuesList[g.getValue(j,i)]++;
            }

            for (int j = 1; j < 10; j++) {
                if (valuesList[j] > 1 || valuesList[j] == 0)
                    count++;
            }
        }
        return (int) (count/2);
    }

}
