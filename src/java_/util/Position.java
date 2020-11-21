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
}
