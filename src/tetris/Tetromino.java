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
    private int x, y;
    private List<Block> blocks;

    private javafx.scene.paint.Color color;
    private int[][] matrix;
    private Pair<Integer, Integer> topLeft;
    private Pair<Integer, Integer> potentialTopLeft;

    private final int MATRIX_SIZE = 4;

    /**
     * Constructor for a tetromino unit
     */
    public Tetromino() {
        matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
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

    /**
     *  Rotates the tetromino clockwise
     */
    public void rotate() {
        int[][] newMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++)
        {
            for (int j = 0; j < MATRIX_SIZE; j++)
            {
                newMatrix[i][j] = transform(matrix[i][j]);
            }
        }
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

    /** Moves tetromino around */

    /** Draws tetromino */
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        for (Block block : blocks) {
            gc.fillRect(75, 75, 100, 100);
        }
    }

}
