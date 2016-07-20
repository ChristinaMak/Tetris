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
        System.out.println("Testing rotate: before");
        shapeArr[0][1] = 1;
        shapeArr[1][1] = 1;
        shapeArr[2][1] = 1;
        shapeArr[2][0] = 1;
        currPiece.setMatrix(shapeArr);
        currPiece.printTetromino();
        System.out.println("Testing rotate: after");
        currPiece.rotate();;
        currPiece.printTetromino();
        System.out.println("Testing rotate: after");
        currPiece.rotate();;
        currPiece.printTetromino();
        System.out.println("Testing rotate: after");
        currPiece.rotate();;
        currPiece.printTetromino();
        System.out.println("Testing rotate: after");
        currPiece.rotate();;
        currPiece.printTetromino();


        //currPiece.setTopLeft(11, 4);
        //currPiece.setTopLeft(14, 4);
        currPiece.setTopLeft(0, 4);

        // draw blocks from landed array

        // draw block from current tetromino

        //currPiece.setPotentialTopLeft(12, 4);
        //currPiece.setPotentialTopLeft(15, 4);
        currPiece.setPotentialTopLeft(1, 4);

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

                    if (i + currPiece.getPotentialTopLeft().getKey() >= landed.length)
                    {
                        // block would be below board
                        collided = true;
                    }
                    else if (j + currPiece.getPotentialTopLeft().getValue() < 0)
                    {
                        // block would be to left of board
                        collided = true;
                    }
                    else if (j + currPiece.getPotentialTopLeft().getValue() >= landed[0].length)
                    {
                        // block would be to right of board
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

    public static boolean checkRotateCollisions(Tetromino currPiece, int[][] landed) {
        boolean collided = false;

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (currPiece.getPotentialMatrix()[i][j] != 0)
                {

                    if (i + currPiece.getTopLeft().getKey() >= landed.length)
                    {
                        // block would be below board
                        collided = true;
                    }
                    else if (j + currPiece.getTopLeft().getValue() < 0)
                    {
                        // block would be to left of board
                        collided = true;
                    }
                    else if (j + currPiece.getTopLeft().getValue() >= landed[0].length)
                    {
                        // block would be to right of board
                        collided = true;
                    }
                    else if (landed[i + currPiece.getTopLeft().getKey()]
                            [j + currPiece.getTopLeft().getValue()] != 0)
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

    public static void moveTetromino(Tetromino currPiece, int[][] landed, int direction) {
        currPiece.setPotentialTopLeft(currPiece.getTopLeft().getKey(), currPiece.getTopLeft().getValue() + direction);

        if (!checkCollisions(currPiece, landed))
        {
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }
    }

    public static void rotateTetromino(Tetromino currPiece, int[][] landed) {
        currPiece.setPotentialMatrix(currPiece.rotate());
        if (!checkRotateCollisions(currPiece, landed))
        {
            currPiece.setMatrix(currPiece.rotate());
        }
    }
}
