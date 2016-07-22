package tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Christina on 7/14/2016.
 */
public class TetrisBoard {
    public static final int NUM_ROWS = 16;
    public static final int NUM_COLS = 10;

    private int[][] landed;

    public TetrisBoard() {
        landed = new int[NUM_ROWS][NUM_COLS];
    }

    /**
     * Checks whether a tetromino will collide with another tetromino or the ground in the next move
     * @param currPiece the tetromino for which collisions will be checked
     * @return whether or not the tetromino had a collision
     */
    public boolean checkCollisions(Tetromino currPiece) {
        boolean collided = false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {

                    if (i + currPiece.getPotentialTopLeft().getKey() >= landed.length) {
                        // block would be below board
                        collided = true;
                    }
                    else if (j + currPiece.getPotentialTopLeft().getValue() < 0) {
                        // block would be to left of board
                        collided = true;
                    }
                    else if (j + currPiece.getPotentialTopLeft().getValue() >= landed[0].length) {
                        // block would be to right of board
                        collided = true;
                    }
                    else if (landed[i + currPiece.getPotentialTopLeft().getKey()]
                            [j + currPiece.getPotentialTopLeft().getValue()] != 0) {
                        // this space is taken
                        collided = true;
                    }
                }
            }
        }

        return collided;
    }

    /**
     * Checks whether a tetromino will have a collision when rotated
     * @param currPiece the tetromino for which collisions will be checked
     * @return whether
     */
    public boolean checkRotateCollisions(Tetromino currPiece) {
        boolean collided = false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (currPiece.getPotentialMatrix()[i][j] != 0) {

                    if (i + currPiece.getTopLeft().getKey() >= landed.length) {
                        // block would be below board
                        collided = true;
                    }
                    else if (j + currPiece.getTopLeft().getValue() < 0) {
                        // block would be to left of board
                        collided = true;
                    }
                    else if (j + currPiece.getTopLeft().getValue() >= landed[0].length) {
                        // block would be to right of board
                        collided = true;
                    }
                    else if (landed[i + currPiece.getTopLeft().getKey()]
                            [j + currPiece.getTopLeft().getValue()] != 0) {
                        // this space is taken
                        collided = true;
                    }
                }
            }
        }

        return collided;
    }

    public void landTetromino(Tetromino currPiece) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    //System.out.println("Before: " + currPiece.getMatrix()[i][j]);
                    landed[i + currPiece.getTopLeft().getKey()][j + currPiece.getTopLeft().getValue()]
                        = currPiece.getMatrix()[i][j];
                    this.setLanded(landed); // TODO redundant?

                    //System.out.println("currPiece " + currPiece.getMatrix()[i][j]);
                    //System.out.println("lt_i: " + i);
                    //System.out.println(i + currPiece.getTopLeft().getKey());
                    //System.out.println("lt_j: " + j);
                    //System.out.println(j + currPiece.getTopLeft().getValue());
                    //System.out.println("from landTetromino");
                    //System.out.println("Begin");
                    //System.out.println(i + currPiece.getTopLeft().getKey());
                    //System.out.println(j + currPiece.getTopLeft().getValue());
                    //System.out.println("End");
                }
            }
        }
    }

    public void moveTetromino(Tetromino currPiece, int direction) {
        currPiece.setPotentialTopLeft(currPiece.getTopLeft().getKey(), currPiece.getTopLeft().getValue() + direction);

        if (!checkCollisions(currPiece)) {
            currPiece.setPrevTopLeft(currPiece.getTopLeft());
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }
    }

    public void rotateTetromino(Tetromino currPiece) {
        currPiece.setPotentialMatrix(currPiece.rotate());
        if (!checkRotateCollisions(currPiece)) {
            currPiece.setPrevMatrix(currPiece.getMatrix());
            currPiece.setMatrix(currPiece.rotate());
        }
    }

    public void clearLineCheck () {
        for (int i = 0; i < NUM_ROWS; i++) {
            boolean filled = true;
            for (int j = 0; j < NUM_COLS; j++) {
                if (this.getLanded()[i][j] == 0) {
                    filled = false;
                }
            }

            if (filled) {
                System.out.println("clear line");
                // remove filled line
                setLanded(spliceLine(this.getLanded(), i));

                // add new empty line
                setLanded(addNewLine(this.getLanded()));
            }
        }
    }

    public boolean checkLine() {
        boolean fullLine = false;

        boolean filled = true;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (this.getLanded()[i][j] == 0) {
                    filled = false;
                }
            }
            fullLine = filled;
        }

        return fullLine;
    }

    public int[][] spliceLine(int[][] grid, int row) {
        List<int[]> list = new ArrayList<int[]>(Arrays.asList(grid));
        list.remove(row);
        return list.toArray(new int[][]{});
    }

    public int[][] addNewLine(int[][] grid) {
        List<int[]> list = new ArrayList<int[]>(Arrays.asList(grid));
        list.add(0, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        return list.toArray(new int[][]{});
    }

    public int[][] getLanded() {
        return landed;
    }

    public void setLanded(int[][] landed) {
        this.landed = landed;
    }

    public void printBoard() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                System.out.print(this.getLanded()[i][j]);
            }
            System.out.println();
        }
    }
}
