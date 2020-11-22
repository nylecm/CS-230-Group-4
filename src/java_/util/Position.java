package java_.util;

import java.util.Objects;

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
