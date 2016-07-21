package tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
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
    private Pair<Integer, Integer> topLeft;
    private Pair<Integer, Integer> potentialTopLeft;
    private Pair<Integer, Integer> prevTopLeft;

    public static final int MATRIX_SIZE = 4;

    /**
     * Constructor for a tetromino unit
     */
    public Tetromino() {
        matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    public Tetromino(int[][] shape) {
        matrix = shape;
    }

    public Tetromino(char shape) {
        shape = Character.toUpperCase(shape);
        int[][] shapeMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];
        switch(shape) {
            case 'I':
                shapeMatrix[0][0] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[2][0] = 1;
                shapeMatrix[3][0] = 1;
                break;
            case 'O':
                shapeMatrix[0][0] = 1;
                shapeMatrix[0][1] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[1][1] = 1;
                break;
            case 'T':
                shapeMatrix[0][1] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[1][1] = 1;
                shapeMatrix[1][2] = 1;
                break;
            case 'S':
                shapeMatrix[0][1] = 1;
                shapeMatrix[0][2] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[1][1] = 1;
                break;
            case 'Z':
                shapeMatrix[0][0] = 1;
                shapeMatrix[0][1] = 1;
                shapeMatrix[1][1] = 1;
                shapeMatrix[1][2] = 1;
                break;
            case 'J':
                shapeMatrix[0][1] = 1;
                shapeMatrix[1][1] = 1;
                shapeMatrix[2][1] = 1;
                shapeMatrix[2][0] = 1;
                break;
            case 'L':
                shapeMatrix[0][0] = 1;
                shapeMatrix[1][0] = 1;
                shapeMatrix[2][0] = 1;
                shapeMatrix[2][1] = 1;
                break;
            default: break;
        }
        setMatrix(shapeMatrix);
    }

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

    public void setPotentialMatrix(int[][] potentialMatrix) {
        this.potentialMatrix = potentialMatrix;
    }

    /**
     *  Returns the matrix of the tetromino rotated clockwise
     */
    public int[][] rotate() {
        int[][] newMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                //newMatrix[i][j] = transform(matrix[i][j]);
                if (this.getMatrix()[i][j] == 1) {
                    int newRow = j;
                    int newCol = transform(i);
                    newMatrix[newRow][newCol] = 1;
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

    /** Draws tetromino */
    /*
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        for (Block block : blocks) {
            gc.fillRect(75, 75, 100, 100);
        }
    }
    */

    public void printTetromino() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(this.getMatrix()[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

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
