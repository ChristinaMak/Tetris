package tetris;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Created by Christina on 7/19/2016.
 */
public class TetrisTester {
    private TetrisBoard board;
    private Tetromino currPiece;

    @Before
    public  void setUp() {
        board = new TetrisBoard();
        currPiece = new Tetromino();
        int[][] landed = board.getLanded();

        // set up an initial testing board's landed array
        for (int i = 0; i < 7; i++)
        {
            landed[15][i] = 1;
        }
        landed[15][8] = 1;
        landed[15][9] = 1;
        for (int i = 1; i < 7; i++)
        {
            landed[14][i] = 1;
        }
        landed[14][9] = 1;
        for (int i = 1; i < 5; i++)
        {
            landed[13][i] = 1;
        }
        landed[13][6] = 1;
        landed[13][9] = 1;
        for (int i = 1; i < 4; i++)
        {
            landed[12][i] = 1;
        }
        landed[12][6] = 1;
        landed[11][2] = 1;
        landed[11][3] = 1;
        board.setLanded(landed);

        // set up a falling tetromino
        int[][] shapeArr = new int[4][4];
        shapeArr[0][1] = 1;
        shapeArr[1][1] = 1;
        shapeArr[2][1] = 1;
        shapeArr[2][0] = 1;
        currPiece.setMatrix(shapeArr);
    }

    @Test
    public void testRotate() {
        int[][] expArr = new int[4][4];
        expArr[0][1] = 1;
        expArr[1][1] = 1;
        expArr[1][2] = 1;
        expArr[1][3] = 1;

        assertArrayEquals(currPiece.rotate(), expArr);
    }

    @Test
    public void testLandTetromino() {
        // set up expected board
        TetrisBoard expBoard = new TetrisBoard();
        int[][] landed = new int[16][10];
        for (int i = 0; i < 7; i++)
        {
            landed[15][i] = 1;
        }
        landed[15][8] = 1;
        landed[15][9] = 1;
        for (int i = 1; i < 7; i++)
        {
            landed[14][i] = 1;
        }
        landed[14][9] = 1;
        for (int i = 1; i < 5; i++)
        {
            landed[13][i] = 1;
        }
        landed[13][6] = 1;
        landed[13][9] = 1;
        for (int i = 1; i < 4; i++)
        {
            landed[12][i] = 1;
        }
        landed[12][6] = 1;
        landed[11][2] = 1;
        landed[11][3] = 1;
        expBoard.setLanded(landed);

        // set up a falling tetromino
        int[][] shapeArr = new int[4][4];
        shapeArr[0][1] = 1;
        shapeArr[1][1] = 1;
        shapeArr[2][1] = 1;
        shapeArr[2][0] = 1;

        currPiece.setTopLeft(0, 4);

        // draw blocks from landed array

        // draw block from current tetromino

        //currPiece.setPotentialTopLeft(12, 4);
        //currPiece.setPotentialTopLeft(15, 4);
        currPiece.setPotentialTopLeft(1, 4);

        if (board.checkCollisions(currPiece) == 1)
        {
            //System.out.println("Before landing topLeft x: " + currPiece.getTopLeft().getKey());
            //System.out.println("Before landing topLeft y: " + currPiece.getTopLeft().getValue());
            // collision found, land tetromino
            board.landTetromino(currPiece);
        }
        else
        {
            // no collision, tetromino continues falling
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }

        assertArrayEquals("tetromino landing failure", expBoard.getLanded(), board.getLanded());
    }

    @Test
    public void testSpliceLine() {
        int[][] landed = new int[board.NUM_ROWS][board.NUM_COLS];
        for (int i = 0; i < 10; i++)
        {
            landed[15][i] = 1;
        }
        for (int i = 0; i < 10; i++)
        {
            landed[14][i] = 2;
        }
        board.setLanded(landed);

        int[][] expLanded = new int[board.NUM_ROWS - 1][board.NUM_COLS];
        for (int i = 0; i < 10; i++)
        {
            expLanded[14][i] = 1;
        }

        board.setLanded(board.spliceLine(board.getLanded(), 14));
        assertArrayEquals(board.getLanded(), expLanded);
    }

    @Test
    public void testAddNewLine() {
        int[][] landed = new int[board.NUM_ROWS][board.NUM_COLS];
        for (int i = 0; i < 10; i++)
        {
            landed[15][i] = 1;
        }
        for (int i = 0; i < 10; i++)
        {
            landed[14][i] = 2;
        }
        board.setLanded(landed);

        int[][] expLanded = new int[board.NUM_ROWS + 1][board.NUM_COLS];
        for (int i = 0; i < 10; i++)
        {
            expLanded[16][i] = 1;
        }
        for (int i = 0; i < 10; i++)
        {
            expLanded[15][i] = 2;
        }

        board.setLanded(board.addNewLine(board.getLanded()));
        assertArrayEquals(board.getLanded(), expLanded);
    }

    @Test
    public void testClearLineCheck() {
        
    }
}
