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
        /*
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
        */

        board.printBoard();
        System.out.println();

        int[][] shapeArr = new int[4][4];
        shapeArr[0][0] = 1;
        shapeArr[0][1] = 1;
        shapeArr[1][0] = 1;
        shapeArr[1][1] = 1;

        currPiece.setMatrix(shapeArr);
        //currPiece.setTopLeft(11, 4);
        currPiece.setTopLeft(14, 4);

        // draw blocks from landed array

        // draw block from current tetromino

        //currPiece.setPotentialTopLeft(12, 4);
        currPiece.setPotentialTopLeft(15, 4);

        if (checkCollisions(currPiece, landed))
        {
            //System.out.println("Before landing topLeft x: " + currPiece.getTopLeft().getKey());
            //System.out.println("Before landing topLeft y: " + currPiece.getTopLeft().getValue());
            // collision found, land tetromino
            landTetromino(currPiece, landed);
            board.setLanded(landed);
        }
        else
        {
            // no collision, tetromino continues falling
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }

        board.printBoard();

    }

    public static boolean checkCollisions(Tetromino currPiece, int[][] landed) {
        boolean collided = false;

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (currPiece.getMatrix()[i][j] != 0)
                {
                    /*
                    System.out.println("from checking collisions");
                    System.out.println("i: " + i + " row: " + currPiece.getPotentialTopLeft().getKey());
                    System.out.println("j: " + j + " col: " + currPiece.getPotentialTopLeft().getValue());
                    System.out.println(landed[i + currPiece.getPotentialTopLeft().getKey()]
                            [j + currPiece.getPotentialTopLeft().getValue()]);
                     */
                    if (i + currPiece.getPotentialTopLeft().getKey() >= landed.length)
                    {
                        // block would be below board
                        collided = true;
                    }
                    else if (landed[i + currPiece.getPotentialTopLeft().getKey()]
                            [j + currPiece.getPotentialTopLeft().getValue()] != 0)
                    {
                        // this space is taken
                        collided = true;
                    }
                }
            }
        }

        return collided;
    }

    public static void landTetromino(Tetromino currPiece, int[][] landed) {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (currPiece.getMatrix()[i][j] != 0)
                {
                    //System.out.println("Before: " + currPiece.getMatrix()[i][j]);
                    landed[i + currPiece.getTopLeft().getKey()][j + currPiece.getTopLeft().getValue()]
                        = currPiece.getMatrix()[i][j];

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

}
