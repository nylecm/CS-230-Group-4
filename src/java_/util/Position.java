package java_.util;

public class Position {

    private int rowNum;
    private int colNum;


    public Position(int rowNum, int colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }

    public int getrowNum() {

        return rowNum;
    }

    public int getcolNum() {

        return colNum;
    }

    public void setrowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public void setcolNum(int colNum) {

        this.colNum = colNum;
    }
}
