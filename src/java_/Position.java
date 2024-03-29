package java_;

/**
 * A position is a pair of row number and column number coordinates used to represent the specific location of an object.
 */
public class Position {

    private int rowNum;
    private int colNum;

    /**
     * Instantiates a position giving it values for both the row number and column number coordinates.
     *
     * @param rowNum The value to be used for the row number.
     * @param colNum The value to be used for the column number.
     */
    public Position(int rowNum, int colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    /**
     * Returns the value of the row number coordinate.
     *
     * @return The position's row number value.
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * Returns the value of the column number coordinate.
     *
     * @return The position's column number value.
     */
    public int getColNum() {
        return colNum;
    }

    /**
     * Sets a value for the row number of the position.
     *
     * @param rowNum The value to be set as the row number of the position.
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * Sets a value for the column number of the position.
     *
     * @param colNum The value to be set as the column number of the position.
     */
    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    /**
     * Prints the row number and column number of the position.
     *
     * @return The string containing the coordinates of the position.
     */
    @Override
    public String toString() {
        return "java.Position{" +
                "rowNum=" + rowNum +
                ", colNum=" + colNum +
                '}';
    }

    /**
     * Checks if the position has the same coordinates of another position specified.
     *
     * @param o The object with coordinates to be compared to the position coordinates.
     * @return True if both objects have the exact same column number and row number.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return rowNum == position.rowNum &&
                colNum == position.colNum;
    }

    /**
     * Returns the hashcode of the position generated by a hashing algorithm.
     *
     * @return The integer value generated by the hashing algorithm.
     */
    @Override
    public int hashCode() {
        return rowNum % 19 + colNum % 19;
    }
}
