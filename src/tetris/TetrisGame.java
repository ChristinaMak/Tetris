package tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Random;

public class TetrisGame extends Application {
    private static final int TILE_SIZE = 40;
    private static final int PRE_TILE_SIZE = 30;
    private static final int TILE_GAP = 1;
    private static final int BOTTOM_SPAN = 1;
    private static final int SCORE_SPAN = 1;
    private static final double GAME_OVER_TEXT_SIZE = 50;
    private static final double SIDE_TEXT_SIZE = 22;
    private static final double PADDING = 25;
    private static final int GRID_WIDTH =
            new Double(3*PADDING + TetrisBoard.NUM_COLS*TILE_SIZE + Tetromino.MATRIX_SIZE*PRE_TILE_SIZE).intValue() + 9*TILE_GAP;
    private static final int GRID_HEIGHT = 750;

    private double time;
    private boolean pauseGame = false;

    AnimationTimer timer;
    private Tetromino currPiece;
    private Tetromino nextPiece;
    private TetrisBoard board;
    private int score = 0;

    private Text pauseText;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #c0c0c0");
        GridPane sideGrid = new GridPane();
        Text nextText = new Text("Next");
        styleText(nextText);
        sideGrid.add(nextText, 0, 0);
        GridPane preview = new GridPane();
        preview.setHgap(TILE_GAP);
        preview.setVgap(TILE_GAP);
        preview.setStyle("-fx-background-color: #c0c0c0");
        sideGrid.setHgap(PADDING);

        GridPane layoutGrid = new GridPane();
        layoutGrid.add(grid, 0, 0);
        layoutGrid.add(sideGrid, 1, 0);
        layoutGrid.setStyle("-fx-background-color: #9b978e");
        layoutGrid.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        layoutGrid.setHgap(PADDING);

        primaryStage.setTitle("Tetris");
        Scene scene = new Scene(layoutGrid, GRID_WIDTH, GRID_HEIGHT);
        Text scoreLabel = createScore(sideGrid);
        create(grid, scene, scoreLabel, sideGrid, layoutGrid, preview);

        class KeyHandler implements EventHandler<KeyEvent> {
            @Override
            public void handle(KeyEvent e) {
                switch (e.getCode()) {
                    case UP:
                        if (!pauseGame) {
                            System.out.println("handle up");
                            board.rotateTetromino(currPiece);
                            removePrevRotatedPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                            break;
                    case DOWN:
                        if (!pauseGame) {
                            System.out.println("handle down");
                            update(grid, sideGrid, preview, scoreLabel, false);
                        }
                        break;
                    case LEFT:
                        if (!pauseGame) {
                            System.out.println("handle move left");
                            board.moveTetromino(currPiece, -1);
                            removePrevPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                        break;
                    case RIGHT:
                        if (!pauseGame) {
                            System.out.println("handle right");
                            board.moveTetromino(currPiece, 1);
                            removePrevPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                        break;
                    case P:
                        //pauseGame = true;
                        pauseGame = !pauseGame;
                        pauseGame(layoutGrid);
                        break;
                }
            }
        }

        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void create(GridPane grid, Scene scene, Text scoreLabel, GridPane sideGrid, GridPane layoutGrid, GridPane preview) {
        board = new TetrisBoard();
        pauseText = new Text("Paused");
        styleTextBottom(pauseText);
        drawGridSquares(grid);
        currPiece = spawnTetromino();
        nextPiece = spawnTetromino();
        updatePreviewBox(preview, sideGrid, nextPiece);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.015;
                if (time >= 0.5 && !board.checkBoardFull()) {
                    time = 0;
                    update(grid, sideGrid, preview, scoreLabel, false);
                }
                if (board.checkBoardFull()) {
                    scene.setOnKeyPressed(null);
                    displayGameOver(layoutGrid, this);
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

    private Tetromino spawnTetromino() {
        Tetromino newPiece;
        Random random = new Random();
        int code = random.nextInt(7);
        newPiece = new Tetromino(code);
        newPiece.setTopLeft(0, 4);
        System.out.println("spawn: " + code);
        return newPiece;
    }

    private void update(GridPane grid, GridPane sideGrid, GridPane preview, Text scoreLabel, boolean pause) {
        int currX = currPiece.getTopLeft().getKey();
        int currY = currPiece.getTopLeft().getValue();
        currPiece.setPotentialTopLeft(currX + 1, currY);
        if (board.checkCollisions(currPiece) == 1) {
            // collision found, land tetromino and spawn a new one
            System.out.println("collision: " + currX + ", " + currY);
            board.landTetromino(currPiece);
            int linesCleared = board.clearLineCheck();
            updateScore(linesCleared, scoreLabel);
            updateBoardLandedGUI(grid);

            currPiece = nextPiece;
            nextPiece = spawnTetromino();
            updatePreviewBox(preview, sideGrid, nextPiece);
        }
        else {
            // no collision, tetromino continues falling
            if (!pause) {
                currPiece.setPrevTopLeft(currPiece.getTopLeft());
                currPiece.setTopLeft(currPiece.getPotentialTopLeft());
            }
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
        Color color;
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
            default:
                color = Color.SILVER;
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

    private void displayGameOver(GridPane grid, AnimationTimer timer)
    {
        //the game over text
        Text gameOverText = new Text("Game Over");
        styleTextBottom(gameOverText);
        grid.add(gameOverText, 0, TetrisBoard.NUM_ROWS, BOTTOM_SPAN, BOTTOM_SPAN);

        timer.stop();
    }

    private void displayPause(GridPane grid) {
        if (pauseGame) {
            grid.add(pauseText, 0, TetrisBoard.NUM_ROWS, BOTTOM_SPAN, BOTTOM_SPAN);
        }
        else {
            grid.getChildren().remove(pauseText);
        }
    }

    private Text createScore(GridPane grid) {
        Text scoreLabel = new Text("\nScore\n " + score);
        styleText(scoreLabel);
        grid.add(scoreLabel, 0, 2, SCORE_SPAN, SCORE_SPAN);

        return scoreLabel;
    }

    private void styleTextBottom(Text text) {
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, GAME_OVER_TEXT_SIZE));
        text.setFill(Color.BLACK);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
    }

    private void styleText(Text text) {
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, SIDE_TEXT_SIZE));
        text.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(text, HPos.CENTER);
    }

    private void updateScore(int linesCleared, Text scoreLabel) {
        switch (linesCleared) {
            case 1:
                score += 40;
                break;
            case 2:
                score += 100;
                break;
            case 3:
                score += 300;
                break;
            case 4:
                score += 1200;
                break;
        }

        System.out.println("updating score: " + score);
        scoreLabel.setText("\nScore\n " + score);
    }

    private void updatePreviewBox(GridPane preview, GridPane sideGrid, Tetromino piece) {
        sideGrid.getChildren().remove(preview);
        System.out.println("called update pre");
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                Rectangle tile;
                if (piece != null) {
                    tile = new Rectangle(PRE_TILE_SIZE, PRE_TILE_SIZE, findColor(piece.getMatrix()[i][j]));
                    System.out.println("pre: " + piece.getMatrix()[i][j]);
                }
                else {
                    System.out.println("called else");
                    tile = new Rectangle(PRE_TILE_SIZE, PRE_TILE_SIZE, Color.SILVER);
                }
                preview.add(tile, j, i);
            }
        }

        sideGrid.add(preview, 0, 1);
    }

    private void pauseGame(GridPane grid) {
        displayPause(grid);
        if (pauseGame) {
            timer.stop();
        }
        else {
            timer.start();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
