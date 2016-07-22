package tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.awt.*;
import java.util.List;

/**
 * Created by Christina on 7/12/2016.
 */
public class Tetromino {
    private javafx.scene.paint.Color color;
    private int[][] matrix;
    private int[][] potentialMatrix;
    private int[][] prevMatrix;
    private Pair<Integer, Integer> topLeft;
    private Pair<Integer, Integer> potentialTopLeft;
    private Pair<Integer, Integer> prevTopLeft;

    public static final int MATRIX_SIZE = 4;

    /**
     * No argument constructor for a tetromino
     */
    public Tetromino() {
        matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    public Tetromino(int[][] shape) {
        matrix = shape;
    }

    /**
     * Constructor for a tetromino with shape given in char
     * @param shape the shape of the tetromino given as char
     */
    public Tetromino(char shape) {
        shape = Character.toUpperCase(shape);
        int[][] shapeMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
        switch(shape) {
            case 'I':
                shapeMatrix[0][0] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[2][0] = 1;
                shapeMatrix[3][0] = 1;
                color = Color.DARKTURQUOISE;
                break;
            case 'O':
                shapeMatrix[0][0] = 2;
                shapeMatrix[0][1] = 2;
                shapeMatrix[1][0] = 2;
                shapeMatrix[1][1] = 2;
                color = Color.GOLD;
                break;
            case 'T':
                shapeMatrix[0][1] = 3;
                shapeMatrix[1][0] = 3;
                shapeMatrix[1][1] = 3;
                shapeMatrix[1][2] = 3;
                color = Color.DARKMAGENTA;
                break;
            case 'S':
                shapeMatrix[0][1] = 4;
                shapeMatrix[0][2] = 4;
                shapeMatrix[1][0] = 4;
                shapeMatrix[1][1] = 4;
                color = Color.CHARTREUSE;
                break;
            case 'Z':
                shapeMatrix[0][0] = 5;
                shapeMatrix[0][1] = 5;
                shapeMatrix[1][1] = 5;
                shapeMatrix[1][2] = 5;
                color = Color.RED;
                break;
            case 'J':
                shapeMatrix[0][1] = 6;
                shapeMatrix[1][1] = 6;
                shapeMatrix[2][1] = 6;
                shapeMatrix[2][0] = 6;
                color = Color.NAVY;
                break;
            case 'L':
                shapeMatrix[0][0] = 7;
                shapeMatrix[1][0] = 7;
                shapeMatrix[2][0] = 7;
                shapeMatrix[2][1] = 7;
                color = Color.ORANGE;
                break;
            default: break;
        }
        setMatrix(shapeMatrix);
    }

    /**
     * Constructor for a tetromino with shape given in int
     * @param shape the shape of the tetromino given as int
     */
    public Tetromino(int shape) {
        this(intToChar(shape));
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix(int[][] shape) {
        this.matrix = shape;
    }

    public Pair<Integer, Integer> getTopLeft() {
        return this.topLeft;
    }

    public void setTopLeft(int row, int col) {
        this.topLeft = new Pair<Integer, Integer>(row, col);
    }

    public void setTopLeft(Pair<Integer, Integer> newTopLeft) {
        this.topLeft = newTopLeft;
    }

    public Pair<Integer, Integer> getPotentialTopLeft() {
        return this.potentialTopLeft;
    }

    public void setPotentialTopLeft(int row, int col) {
        this.potentialTopLeft = new Pair<Integer, Integer>(row, col);
    }

    public Pair<Integer, Integer> getPrevTopLeft() {
        return prevTopLeft;
    }

    public void setPrevTopLeft(Pair<Integer, Integer> prevTopLeft) {
        this.prevTopLeft = prevTopLeft;
    }

    public int[][] getPotentialMatrix() {
        return potentialMatrix;
    }

    public int[][] getPrevMatrix() {
        return prevMatrix;
    }

    public void setPrevMatrix(int[][] prevMatrix) {
        this.prevMatrix = prevMatrix;
    }

    public void setPotentialMatrix(int[][] potentialMatrix) {
        this.potentialMatrix = potentialMatrix;
    }

    public Color getColor() {
        return color;
    }

    /**
     *  Returns the matrix of the tetromino rotated clockwise
     */
    public int[][] rotate() {
        int[][] newMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                //newMatrix[i][j] = transform(matrix[i][j]);
                if (this.getMatrix()[i][j] != 0) {
                    int newRow = j;
                    int newCol = transform(i);
                    newMatrix[newRow][newCol] = this.getMatrix()[i][j];
                }
            }
        }
        return newMatrix;
    }

    /** Helper for rotate method to transform index positioning */
    private int transform(int index) {
        int newIndex = index;
        switch (index) {
            case 0:  newIndex = 3; break;
            case 1: newIndex = 2; break;
            case 2: newIndex = 1; break;
            case 3: newIndex = 0; break;
        }
        return newIndex;
    }

    public void printTetromino() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(this.getMatrix()[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /** Helper to turn int code of shape from random generator to char code of shape */
    private static char intToChar(int shape) {
        char shapeChar = 'O';
        switch (shape) {
            case 0:
                shapeChar = 'I';
                break;
            case 1:
                shapeChar = 'O';
                break;
            case 2:
                shapeChar = 'T';
                break;
            case 3:
                shapeChar = 'S';
                break;
            case 4:
                shapeChar = 'Z';
                break;
            case 5:
                shapeChar = 'J';
                break;
            case 6:
                shapeChar = 'L';
                break;
            default: break;
        }
        return shapeChar;
    }

}
