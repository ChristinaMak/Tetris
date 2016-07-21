package tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TetrisGame extends Application {

    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 15;
    private static final int GRID_HEIGHT = 20;

    private double time;
    private GraphicsContext gContext;

    private List<Tetromino> tetrominos = new ArrayList<>();
    private Tetromino currPiece;
    private int[][] boardMatrix;

    private TetrisBoard board;

    @Override
    public void start(Stage primaryStage) throws Exception{
        create();

        class KeyHandler implements EventHandler<KeyEvent> {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case UP:
                        System.out.println("handle up");
                        board.rotateTetromino(currPiece);
                        break;
                    case DOWN:
                        System.out.println("handle down");
                        update();
                        break;
                    case LEFT:
                        System.out.println("handle move left");
                        board.moveTetromino(currPiece, -1);
                        break;
                    case RIGHT:
                        System.out.println("handle right");
                        board.moveTetromino(currPiece, 1);
                        break;
                }
            }
        }

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void create() {
        board = new TetrisBoard();
        spawnTetromino();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    time = 0;
                    update();
                }
            }
        };
        timer.start();
        //currPiece = new Tetromino('o');
        //currPiece.setTopLeft(0, 4);
    }

    public void spawnTetromino() {
        Random random = new Random();
        currPiece = new Tetromino(random.nextInt(7));
        currPiece.setTopLeft(0, 4);
    }

    public void update() {
        int currX = currPiece.getTopLeft().getKey();
        int currY = currPiece.getTopLeft().getValue();
        currPiece.setPotentialTopLeft(currX + 1, currY);
        if (board.checkCollisions(currPiece)) {
            // collision found, land tetromino
            System.out.println("collision: " + currX + ", " + currY);
            board.landTetromino(currPiece);
        }
        else {
            // no collision, tetromino continues falling
            currPiece.setTopLeft(currPiece.getPotentialTopLeft());
        }
        board.printBoard();
        System.out.println();
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
