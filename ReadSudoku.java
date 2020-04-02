import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ReadSudoku {

    ReadSudoku () {

    }

    static int[] readSudokuFromFile () throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("tall.txt"));
        int [] tall = new int [81];
        int i = 0;
        while(scanner.hasNextInt())
        {
            tall[i++] = scanner.nextInt();
        }
        return tall;
    }
}
