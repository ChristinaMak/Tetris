package tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

public class TetrisGame extends Application {
    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 450;
    private static final int GRID_HEIGHT = 750;
    private static final int TILE_GAP = 1;

    private double time;

    private Tetromino currPiece;
    private TetrisBoard board;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #c0c0c0");
        primaryStage.setTitle("Tetris");
        Scene scene = new Scene(grid, GRID_WIDTH, GRID_HEIGHT);
        create(grid, scene);

        class KeyHandler implements EventHandler<KeyEvent> {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case UP:
                        System.out.println("handle up");
                        board.rotateTetromino(currPiece);
                        removePrevRotatedPieceGUI(grid);
                        update(grid);
                        break;
                    case DOWN:
                        System.out.println("handle down");
                        update(grid);
                        break;
                    case LEFT:
                        System.out.println("handle move left");
                        board.moveTetromino(currPiece, -1);
                        removePrevPieceGUI(grid);
                        update(grid);
                        break;
                    case RIGHT:
                        System.out.println("handle right");
                        board.moveTetromino(currPiece, 1);
                        removePrevPieceGUI(grid);
                        update(grid);
                        break;
                }
            }
        }

        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void create(GridPane grid, Scene scene) {
        board = new TetrisBoard();
        drawGridSquares(grid);
        spawnTetromino();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.015;
                if (time >= 0.5 && !board.checkBoardFull()) {
                    time = 0;
                    update(grid);
                }
                if (board.checkBoardFull()) {
                    scene.setOnKeyPressed(null);
                }
            }
        };
        timer.start();
    }

    private void drawGridSquares(GridPane grid) {
        grid.setVgap(TILE_GAP);
        grid.setHgap(TILE_GAP);
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.SILVER);
                grid.add(block, j, i);
            }
        }
    }

    private void spawnTetromino() {
        Random random = new Random();
        currPiece = new Tetromino(random.nextInt(7));
        currPiece.setTopLeft(0, 4);
    }

    private void update(GridPane grid) {
        int currX = currPiece.getTopLeft().getKey();
        int currY = currPiece.getTopLeft().getValue();
        currPiece.setPotentialTopLeft(currX + 1, currY);
        if (board.checkCollisions(currPiece)) {
            // collision found, land tetromino and spawn a new one
            System.out.println("collision: " + currX + ", " + currY);
            board.landTetromino(currPiece);
            board.clearLineCheck();
            updateBoardLandedGUI(grid);

            spawnTetromino();
        }
        else {
            // no collision, tetromino continues falling
            currPiece.setPrevTopLeft(currPiece.getTopLeft());
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }

        removePrevPieceGUI(grid);
        updateCurrPieceGUI(grid);

        board.printBoard();
        System.out.println();
    }

    private void updateBoardLandedGUI(GridPane grid) {
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                Rectangle block;
                if (board.getLanded()[i][j] != 0) {
                    block = new Rectangle(TILE_SIZE, TILE_SIZE, findColor(board.getLanded()[i][j]));
                }
                else {
                    block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.SILVER);
                }
                grid.add(block, j, i);
            }
        }
    }

    private Color findColor(int code) {
        Color color = Color.BLACK;
        switch (code) {
            case 1:
                color = Color.DARKTURQUOISE;
                break;
            case 2:
                color = Color.GOLD;
                break;
            case 3:
                color = Color.DARKMAGENTA;
                break;
            case 4:
                color = Color.CHARTREUSE;
                break;
            case 5:
                color = Color.RED;
                break;
            case 6:
                color = Color.NAVY;
                break;
            case 7:
                color = Color.ORANGE;
                break;
        }
        return color;
    }

    private void updateCurrPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, currPiece.getColor());
                    System.out.println("updateCurrPieceGUI " + j + currPiece.getTopLeft().getValue() + ", " + i + currPiece.getTopLeft().getKey());
                    grid.add(block, j + currPiece.getTopLeft().getValue(), i + currPiece.getTopLeft().getKey());
                }
            }
        }
    }

    private void removePrevPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.SILVER);
                    //System.out.println("removePrevPieceGUI " + j + currPiece.getPrevTopLeft().getValue() + ", " + i + currPiece.getPrevTopLeft().getKey());
                    try {
                        grid.add(block, j + currPiece.getPrevTopLeft().getValue(), i + currPiece.getPrevTopLeft().getKey());
                    }
                    catch (NullPointerException e) {
                    }
                }
            }
        }
    }

    private void removePrevRotatedPieceGUI(GridPane grid) {
        if (currPiece.getPrevMatrix() == null) {
            return;
        }

        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getPrevMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.SILVER);
                    grid.add(block, j + currPiece.getTopLeft().getValue(), i + currPiece.getTopLeft().getKey());
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
