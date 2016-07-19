package tetris;

/**
 * Created by Christina on 7/14/2016.
 */
public class TetrisBoard {
    private final int NUM_ROWS = 16;
    private final int NUM_COLS = 10;

    private int[][] landed;

    public TetrisBoard() {
        landed = new int[NUM_ROWS][NUM_COLS];
    }

    public int[][] getLanded() {
        return landed;
    }

    public void setLanded(int[][] landed) {
        this.landed = landed;
    }

    public void printBoard() {
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                System.out.print(this.getLanded()[i][j]);
            }
            System.out.println();
        }
    }
}
