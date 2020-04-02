
public class Grid {
    private Digit[][] grid;

    Grid(int[] inputValues) {
        this.grid = new Digit[9][9];

        for (int i = 0; i < inputValues.length; i++) {
            this.grid[(int) (i / 9)][ i % 9] = new Digit(inputValues[i]);
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.updateGrid(i,j);
            }
        }
    }

    Grid(Grid g) {
        this.grid = new Digit[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grid[i][j] = new Digit(g.getValue(i,j));
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.updateGrid(i,j);
            }
        }
    }

    int getValue(int x, int y) { return this.grid[x][y].getValue(); }

    void setValue(int x, int y, int val) { this.grid[x][y].setValue(val); }

    void printGrid() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(this.grid[i][j].getValue());
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    int getGridPosNumPossibleValues(int x, int y) {
        return this.grid[x][y].getNumOpt();
    }

    void printNumOptGrid() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(this.grid[i][j].getNumOpt());
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    boolean fillPosition(int x, int y) {
        return this.grid[x][y].fillPosition();
    }

    void updateGrid(int x, int y) {
        rowUpdateGrid(x);
        colUpdateGrid(y);
        subgridUpdateGrid(x, y);
        // outros algoritimos para atualizar o grid...
    }

    private void rowUpdateGrid(int x) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grid[x][j].removePossibleValue(this.grid[x][i].getValue());
            }
        }

        int[] countMissingValues = new int[10];
        for (int i = 0 ; i < 10; i++) {
            countMissingValues[i] = 0;
        }

        boolean[] possibleValuesCopy;

        for (int i = 0; i < 9; i++) { // for each cell in the row x
            possibleValuesCopy = this.grid[x][i].getPossibleValues();
            for (int j = 0; j < 10; j++) { // for each possible value in cell i
                if (possibleValuesCopy[j]) {
                    countMissingValues[j]++;
                }
            }
        }

        for (int i = 0; i < 10; i++) { // for each possible value in the row
            if (countMissingValues[i] == 1) { // if there is only one place to put that value
                for (int j = 0; j < 9; j++) { // search for cell that accepts that vaue
                   if (grid[x][j].isValuePossible(i)) {
                       grid[x][j].clearPossibleValues();
                       grid[x][j].addPossibleValue(i);
                   }
                }

            }
        }
    }

    private void colUpdateGrid(int y) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.grid[j][y].removePossibleValue(this.grid[i][y].getValue());
            }
        }

        int[] countMissingValues = new int[10]; // init count of missing values in each column
        for (int i = 0 ; i < 10; i++) {
            countMissingValues[i] = 0;
        }

        boolean[] possibleValuesCopy;

        for (int i = 0; i < 9; i++) { // for each cell in the col y
            possibleValuesCopy = this.grid[i][y].getPossibleValues();
            for (int j = 0; j < 10; j++) { // for each possible value in cell i
                if (possibleValuesCopy[j]) {
                    countMissingValues[j]++;
                }
            }
        }

        for (int i = 0; i < 10; i++) { // for each possible value in the column
            if (countMissingValues[i] == 1) { // if there is only one place to put that value
                for (int j = 0; j < 9; j++) { // search for cell that accepts that value
                    if (grid[j][y].isValuePossible(i)) {
                        grid[j][y].clearPossibleValues();
                        grid[j][y].addPossibleValue(i);
                    }
                }

            }
        }

    }

    private void subgridUpdateGrid(int x, int y) {

        x = (int) (x/3);
        y = (int) (y/3);
        x*=3;
        y*=3;

        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                for (int k = x; k < x + 3; k++) {
                    for (int m = y; m < y + 3; m++) {
                        this.grid[k][m].removePossibleValue(this.grid[i][j].getValue());
                    }
                }
            }
        }

        int[] countMissingValues = new int[10]; // init count of missing values in each subgrid
        for (int i = 0 ; i < 10; i++) {
            countMissingValues[i] = 0;
        }

        boolean[] possibleValuesCopy;

        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                possibleValuesCopy = this.grid[i][j].getPossibleValues();
                for (int k = 0; k < 10; k++) { // for each possible value in cell
                    if (possibleValuesCopy[k]) {
                        countMissingValues[k]++;
                    }
                }
            }
        }


        for (int i = 0; i < 10; i++) { // for each possible value in the column
            if (countMissingValues[i] == 1) { // if there is only one place to put that value

                for (int j = x; j < x + 3; j++) {
                    for (int k = y; k < y + 3; k++) {
                        if (grid[j][k].isValuePossible(i)) {
                            grid[j][k].clearPossibleValues();
                            grid[j][k].addPossibleValue(i);
                        }
                    }
                }

            }
        }

    }
}
