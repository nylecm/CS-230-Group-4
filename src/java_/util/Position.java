package java_.util;

public class Position {

    private int rowNum;
    private int colNum;


    public Position(int rowNum, int colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public int getRowNum() {

        return rowNum;
    }

    public int getColNum() {

        return colNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public void setColNum(int colNum) {

        this.colNum = colNum;
    }

    public void incrementRowNum() {
        rowNum++;
    }

    public void incrementColNum() {
        colNum++;
    }

    public void decrementRowNum() {
        rowNum--;
    }

    public void decrementColNum() {
        colNum--;
    }

    @Override
    public String toString() {
        return "Position{" +
                "rowNum=" + rowNum +
                ", colNum=" + colNum +
                '}';
    }

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

    @Override
    public int hashCode() {
        return rowNum % 19 + colNum % 19;
    }
}
