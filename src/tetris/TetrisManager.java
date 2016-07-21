package tetris;

/**
 * Created by Christina on 7/14/2016.
 */
public class TetrisManager {
    //private static int[][] board;

    public static void main(String[] args) {
        TetrisBoard board = new TetrisBoard();
        Tetromino currPiece = new Tetromino();
        int[][] landed = board.getLanded();

        // set up an initial testing board
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

        board.printBoard();
        System.out.println();

        int[][] shapeArr = new int[4][4];
        /*
        // square
        shapeArr[0][0] = 1;
        shapeArr[0][1] = 1;
        shapeArr[1][0] = 1;
        shapeArr[1][1] = 1;
        */
        // J
        shapeArr[0][1] = 1;
        shapeArr[1][1] = 1;
        shapeArr[2][1] = 1;
        shapeArr[2][0] = 1;
        currPiece.setMatrix(shapeArr);

        //currPiece.setTopLeft(11, 4);
        //currPiece.setTopLeft(14, 4);
        currPiece.setTopLeft(0, 4);

        // draw blocks from landed array

        // draw block from current tetromino

        //currPiece.setPotentialTopLeft(12, 4);
        //currPiece.setPotentialTopLeft(15, 4);
        currPiece.setPotentialTopLeft(1, 4);


        if (board.checkCollisions(currPiece))
        {
            //System.out.println("Before landing topLeft x: " + currPiece.getTopLeft().getKey());
            //System.out.println("Before landing topLeft y: " + currPiece.getTopLeft().getValue());
            // collision found, land tetromino
            board.landTetromino(currPiece);
            board.setLanded(landed);
        }
        else
        {
            // no collision, tetromino continues falling
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }


        board.printBoard();

    }


}
