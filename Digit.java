public class Digit {
    private int value;
    private boolean[] possibleValues;


    Digit(int val) {
        this.value = val;
        this.possibleValues = new boolean[10];
        for (int i = 0; i < 10; i++) {
            this.possibleValues[i] = (val == 0);
        }
        this.possibleValues[0] = false;
    }

    boolean[] getPossibleValues() {
        return this.possibleValues;
    }

    void setValue(int val) {
        if (val != 0) {
            this.value = val;
        }
    }

    int getValue() {
        return this.value;
    }

    int getNumOpt() {
        if (this.value != 0) { // in case its already filled with a number
            return 0;
        }
        int counter = 0;
        for (int i = 1; i < 10; i++) {
            if (possibleValues[i]) {
                counter++;
            }
        }
//        System.out.print("counter = ");
//        System.out.println(counter);
        return counter;
    }

    boolean fillPosition() {
        for (int i = 1; i < 10; i++) {
            if (this.possibleValues[i]) {
                this.value = i;
                break;
            }
        }
        for (int i = 0; i < 10; i++) {
            this.possibleValues[i] = false;
        }

        return true;
    }

    void removePossibleValue(int val) {
        this.possibleValues[val] = false;
    }

    void addPossibleValue(int val) {
        this.possibleValues[val] = true;
    }

    boolean isValuePossible(int val) {
        return possibleValues[val];
    }

    void clearPossibleValues() {
        for (int i = 0; i < 10; i++) {
            this.possibleValues[i] = false;
        }
    }
}
