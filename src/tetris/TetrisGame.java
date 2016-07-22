package tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TetrisGame extends Application {
    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 15;
    private static final int GRID_HEIGHT = 20;
    private static final int TILE_GAP = 2;

    private double time;
    private GraphicsContext gContext;

    private List<Tetromino> tetrominos = new ArrayList<>();
    private Tetromino currPiece;
    private Tetromino prevPiece;
    private TetrisBoard board;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane grid = new GridPane();
        create(grid);
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

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        //Scene scene = new Scene(root, 450, 750);
        Scene scene = new Scene(grid, 450, 750);
        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void create(GridPane grid) {
        board = new TetrisBoard();
        drawGridSquares(grid);
        spawnTetromino(grid);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    time = 0;
                    update(grid);
                }
            }
        };
        timer.start();
        //currPiece = new Tetromino('o');
        //currPiece.setTopLeft(0, 4);
    }

    public void drawGridSquares(GridPane grid) {
        grid.setVgap(TILE_GAP);
        grid.setHgap(TILE_GAP);
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.ALICEBLUE);
                grid.add(block, j, i);
            }
        }
    }

    public void spawnTetromino(GridPane grid) {
        Random random = new Random();
        currPiece = new Tetromino(random.nextInt(7));
        currPiece.setTopLeft(0, 4);
    }

    public void update(GridPane grid) {
        int currX = currPiece.getTopLeft().getKey();
        int currY = currPiece.getTopLeft().getValue();
        currPiece.setPotentialTopLeft(currX + 1, currY);
        if (board.checkCollisions(currPiece)) {
            // collision found, land tetromino and spawn a new one
            System.out.println("collision: " + currX + ", " + currY);
            board.landTetromino(currPiece);
            updateBoardLandedGUI(grid);

            spawnTetromino(grid);
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

    public void updateBoardLandedGUI(GridPane grid) {
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                if (board.getLanded()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.RED);
                    grid.add(block, j, i);
                }
            }
        }
    }

    public void updateCurrPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREEN);
                    System.out.println("updateCurrPieceGUI " + j + currPiece.getTopLeft().getValue() + ", " + i + currPiece.getTopLeft().getKey());
                    grid.add(block, j + currPiece.getTopLeft().getValue(), i + currPiece.getTopLeft().getKey());
                }
            }
        }
    }

    public void removePrevPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.ALICEBLUE);
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

    public void removePrevRotatedPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getPrevMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.ALICEBLUE);
                    grid.add(block, j + currPiece.getTopLeft().getValue(), i + currPiece.getTopLeft().getKey());
                }
            }
        }
    }

    /*
    public void create() {
        GridPane pane = new GridPane();

        Canvas canvas = new Canvas(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        gContext = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    //update();
                    //render();
                    time = 0;
                }
            }
        };
        timer.start();
    }
    */



    public static void main(String[] args) {
        launch(args);
    }
}
