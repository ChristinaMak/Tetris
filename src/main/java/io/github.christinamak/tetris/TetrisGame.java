package io.github.christinamak.tetris;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

/**
 * Class for a Tetris game application.
 * Created by Christina Mak
 * July 18, 2016
 */
public class TetrisGame extends Application {
    private static final int TILE_SIZE = 40;
    private static final int PRE_TILE_SIZE = 30;
    private static final int TILE_GAP = 1;
    private static final double VOL_START = 0.5;
    private static final int BTM_SPAN = 1;
    private static final int SCORE_SPAN = 1;
    private static final double BTM_TEXT_SIZE = 50;
    private static final double SIDE_TEXT_SIZE = 22;
    private static final double INFO_TEXT_SIZE = 13;
    private static final double PADDING = 25;
    private static final int GRID_WIDTH =
            new Double(3*PADDING + TetrisBoard.NUM_COLS*TILE_SIZE +
            Tetromino.MATRIX_SIZE*PRE_TILE_SIZE).intValue() + 9*TILE_GAP;
    private static final int GRID_HEIGHT = 750;

    private static final int SCORE_IDX = 2;
    private static final int RESTART_IDX = 3;
    private static final int PAUSE_IDX = 4;
    private static final int MUTE_IDX = 7;
    private static final int VOL_IDX = 6;
    private static final int INFO_IDX = 8;
    private static final int VOL_LBL_IDX = 5;

    private static final int SCORE_1 = 40;
    private static final int SCORE_2 = 100;
    private static final int SCORE_3 = 300;
    private static final int SCORE_4 = 1200;

    private double time;
    private boolean pauseGame = false;
    private boolean gameOver = false;
    private boolean muteSound;
    private boolean keepDropping = true;

    private AnimationTimer timer;
    private MediaPlayer mediaPlayer;
    private Tetromino currPiece;
    private Tetromino nextPiece;
    private TetrisBoard board;
    private int score = 0;

    private Text pauseText;
    private Text gameOverText;

    /**
     * Starts the application
     * @param primaryStage the application stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // create board section
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #c0c0c0");

        // initialize sidebar items
        GridPane sideGrid = new GridPane();
        GridPane preview = new GridPane();
        Text scoreLabel = createScore(sideGrid);

        GridPane layoutGrid = new GridPane();
        createLayout(layoutGrid, grid, sideGrid);
        createSideBar(layoutGrid, grid, sideGrid, preview, scoreLabel);
        primaryStage.setTitle("Tetris");
        Scene scene = new Scene(layoutGrid, GRID_WIDTH, GRID_HEIGHT);
        create(grid, scoreLabel, sideGrid, layoutGrid, preview);

        /**
         * Class to handle key presses to control game
         */
        class KeyHandler implements EventHandler<KeyEvent> {
            @Override
            public void handle(KeyEvent e) {
                if (gameOver) {
                    // only key press allowed is to restart game
                    if (e.getCode() == KeyCode.N) {
                        restart(grid, scoreLabel, sideGrid, preview, layoutGrid);
                    }
                    return;
                }

                switch (e.getCode()) {
                    case W:
                    case UP:
                        if (!pauseGame) {
                            board.rotateTetromino(currPiece);
                            removePrevRotatedPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                            break;
                    case S:
                    case DOWN:
                        if (!pauseGame) {
                            update(grid, sideGrid, preview, scoreLabel, false);
                        }
                        break;
                    case A:
                    case LEFT:
                        if (!pauseGame) {
                            board.moveTetromino(currPiece, -1);
                            removePrevPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                        break;
                    case D:
                    case RIGHT:
                        if (!pauseGame) {
                            board.moveTetromino(currPiece, 1);
                            removePrevPieceGUI(grid);
                            update(grid, sideGrid, preview, scoreLabel, true);
                        }
                        break;
                    case P:
                        pauseGame = !pauseGame;
                        pauseGame(layoutGrid);
                        break;
                    case M:
                        muteSound = !muteSound;
                        mediaPlayer.setMute(muteSound);
                        break;
                    case N:
                        restart(grid, scoreLabel, sideGrid, preview, layoutGrid);
                        break;
                    case SPACE:
                        if (!pauseGame) {
                            while(keepDropping) {
                                update(grid, sideGrid, preview, scoreLabel, false);
                            }
                            keepDropping = true;
                        }
                        break;
                }
            }
        }

        scene.setOnKeyPressed(new KeyHandler());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Initializes the layout elements and music. Starts the game timer.
     * @param grid the board gridpane
     * @param scoreLabel the Text for the score
     * @param sideGrid the sidebar gridpane
     * @param layoutGrid the layout gridpane
     * @param preview the preview box gridpane
     */
    private void create(GridPane grid, Text scoreLabel, GridPane sideGrid,
                        GridPane layoutGrid, GridPane preview) {
        board = new TetrisBoard();
        drawGridSquares(grid);
        pauseText = new Text("Paused");
        styleTextBottom(pauseText);
        setMusic();
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
                    displayGameOver(layoutGrid, this);
                }
            }
        };
        timer.start();
    }

    /**
     * Creates the application layout
     * @param layoutGrid the layout gridpane
     * @param grid the board gridpane
     * @param sideGrid the sidebar gridpane
     */
    private void createLayout(GridPane layoutGrid, GridPane grid,
                              GridPane sideGrid) {
        layoutGrid.add(grid, 0, 0);
        layoutGrid.add(sideGrid, 1, 0);
        layoutGrid.setStyle("-fx-background-color: #9b978e");
        layoutGrid.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        layoutGrid.setHgap(PADDING);
    }


    /**
     * Styles the preview box's grid gaps and background color
     * @param preview the preview box gridpane
     */
    private void stylePreview(GridPane preview) {
        preview.setHgap(TILE_GAP);
        preview.setVgap(TILE_GAP);
        preview.setStyle("-fx-background-color: #c0c0c0");
    }

    /**
     * Creates the sidebar elements
     * @param layoutGrid the layout gridpane
     * @param grid the board gridpane
     * @param sideGrid the sidebar gridpane
     * @param preview the preview box gridpane
     * @param scoreLabel the Text for the score
     */
    private void createSideBar(GridPane layoutGrid, GridPane grid,
                               GridPane sideGrid, GridPane preview,
                               Text scoreLabel) {
        sideGrid.setHgap(PADDING);

        // next text
        Text nextText = new Text("Next");
        styleText(nextText);
        sideGrid.add(nextText, 0, 0);

        // preview box
        stylePreview(preview);

        // restart button
        BoxButton restartBtn = new BoxButton("Restart");
        GridPane.setMargin(restartBtn, new Insets(5, 0, 5, 0));
        restartBtn.setOnAction(e -> {
            restart(grid, scoreLabel, sideGrid, preview, layoutGrid);
        });
        sideGrid.add(restartBtn, 0, RESTART_IDX);

        // pause button
        BoxButton pauseBtn = new BoxButton("Pause");
        GridPane.setMargin(pauseBtn, new Insets(5, 0, 5, 0));
        pauseBtn.setOnAction(e -> {
            if (!gameOver) {
                pauseGame = !pauseGame;
                pauseGame(layoutGrid);
            }
        });
        sideGrid.add(pauseBtn, 0, PAUSE_IDX);

        // mute sound button
        BoxButton muteBtn = new BoxButton("Mute");
        GridPane.setMargin(muteBtn, new Insets(5, 0, 5, 0));
        muteBtn.setOnAction(e -> {
            muteSound = !muteSound;
            mediaPlayer.setMute(muteSound);
        });
        sideGrid.add(muteBtn, 0, MUTE_IDX);

        // volume slider for music
        Slider volSlider = new Slider(0, 1, VOL_START);
        setUpSlider(volSlider, grid);
        sideGrid.add(volSlider, 0, VOL_IDX);

        // volume label
        Text volLabel = new Text("Volume");
        styleText(volLabel);
        GridPane.setMargin(volLabel, new Insets(20, 0, 0, 0));
        sideGrid.add(volLabel, 0, VOL_LBL_IDX);

        // information box
        Text info = new Text("Use the arrow\n keys or WASD\n to move." +
                "\n\nUP rotates\nSPACE drops\n\n" +
                "N = restart\nP = pause\nM = mute\n");
        styleInfo(info);
        sideGrid.add(info, 0, INFO_IDX);
    }

    /**
     * Sets up the volume slider for the music
     * @param volSlider the volume slider
     * @param grid the board gridpane
     */
    private void setUpSlider(Slider volSlider, GridPane grid) {
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                mediaPlayer.setVolume(newValue.doubleValue());
                grid.requestFocus();
            }
        });
        volSlider.setFocusTraversable(false);
        GridPane.setMargin(volSlider, new Insets(10, 0, 5, 0));
    }

    /**
     * Styles the sidebar information box for controls
     * @param info the information textbox
     */
    private void styleInfo(Text info) {
        info.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD,
                INFO_TEXT_SIZE));
        info.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(info, HPos.CENTER);
        GridPane.setMargin(info, new Insets(20, 0, 0, 0));
    }

    /**
     * Draws the initial background squares for the board
     * @param grid the board gridpane
     */
    private void drawGridSquares(GridPane grid) {
        grid.setVgap(TILE_GAP);
        grid.setHgap(TILE_GAP);
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE,
                        Color.SILVER);
                grid.add(block, j, i);
            }
        }
    }

    /**
     * Creates and spawns a new tetromino in the game after a piece has landed.
     * @return the tetromino created
     */
    private Tetromino spawnTetromino() {
        Tetromino newPiece;
        Random random = new Random();
        int code = random.nextInt(7);
        newPiece = new Tetromino(code);
        newPiece.setTopLeft(0, 4);
        return newPiece;
    }

    /**
     * Updates the game for each unit of time passed
     * @param grid the board gridpane
     * @param sideGrid the sidebar gridpane
     * @param preview the preview box for the next tetromino
     * @param scoreLabel the Text for the score
     * @param pause whether or not the game is paused
     */
    private void update(GridPane grid, GridPane sideGrid, GridPane preview,
                        Text scoreLabel, boolean pause) {
        int currX = currPiece.getTopLeft().getKey();
        int currY = currPiece.getTopLeft().getValue();
        currPiece.setPotentialTopLeft(currX + 1, currY);
        if (board.checkCollisions(currPiece) == 1) {
            // collision found, land tetromino and spawn a new one
            keepDropping = false;
            board.landTetromino(currPiece);
            int linesCleared = board.clearLineCheck();
            updateScore(linesCleared, scoreLabel);
            updateBoardLandedGUI(grid);

            currPiece = nextPiece;
            nextPiece = spawnTetromino();
            updatePreviewBox(preview, sideGrid, nextPiece);
        }
        else {
            // no collision or collision with sides, tetromino continues falling
            if (!pause) {
                currPiece.setPrevTopLeft(currPiece.getTopLeft());
                currPiece.setTopLeft(currPiece.getPotentialTopLeft());
            }
        }

        removePrevPieceGUI(grid);
        updateCurrPieceGUI(grid);
    }

    /**
     * Updates the display of the landed blocks on the board
     * @param grid the board gridpane
     */
    private void updateBoardLandedGUI(GridPane grid) {
        for (int i = 0; i < TetrisBoard.NUM_ROWS; i++) {
            for (int j = 0; j < TetrisBoard.NUM_COLS; j++) {
                Rectangle block;
                if (board.getLanded()[i][j] != 0) {
                    block = new Rectangle(TILE_SIZE, TILE_SIZE,
                            findColor(board.getLanded()[i][j]));

                }
                else {
                    block = new Rectangle(TILE_SIZE, TILE_SIZE, Color.SILVER);
                }
                grid.add(block, j, i);
            }
        }
    }

    /**
     * Determines the color the block depending on the code of the block of the
     *     landed array
     * @param code the code of the block in the landed array
     * @return the color of the block
     */
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

    /**
     * Draws the blocks for the current tetromino
     * @param grid the board gridpane
     */
    private void updateCurrPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE,
                            currPiece.getColor());
                    grid.add(block, j + currPiece.getTopLeft().getValue(), i +
                            currPiece.getTopLeft().getKey());
                }
            }
        }
    }

    /**
     * Removes drawn blocks after tetromino falls
     * @param grid the board gridpane
     */
    private void removePrevPieceGUI(GridPane grid) {
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE,
                            Color.SILVER);
                    // bounds checking to prevent going off board
                    if (currPiece.hasPrevTopLeft() &&
                        j + currPiece.getPrevTopLeft().getValue() <
                        TetrisBoard.NUM_COLS &&
                        j + currPiece.getPrevTopLeft().getValue() >= 0 &&
                        i + currPiece.getPrevTopLeft().getKey() <
                        TetrisBoard.NUM_ROWS) {
                            // add background color blocks at previous positions
                            grid.add(block, j + currPiece.getPrevTopLeft().getValue(),
                                i + currPiece.getPrevTopLeft().getKey());
                    }
                }
            }
        }
    }

    /**
     * Removes drawn blocks after a rotation
     * @param grid the board gridpane
     */
    private void removePrevRotatedPieceGUI(GridPane grid) {
        if (currPiece.getPrevMatrix() == null) {
            return;
        }

        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                if (currPiece.getPrevMatrix()[i][j] != 0) {
                    Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE,
                            Color.SILVER);
                    // bounds checking to prevent going off board
                    if (j + currPiece.getTopLeft().getValue() <
                            TetrisBoard.NUM_COLS &&
                            i + currPiece.getTopLeft().getKey() <
                                    TetrisBoard.NUM_ROWS) {
                        // add background-colored blocks at previous positions
                        grid.add(block, j + currPiece.getTopLeft().getValue(),
                                i + currPiece.getTopLeft().getKey());
                    }
                }
            }
        }
    }

    /**
     * Displays the game over text at the bottom of the screen and
     *     stops the game
     * @param grid the layout grid
     * @param timer the timer for the game
     */
    private void displayGameOver(GridPane grid, AnimationTimer timer)
    {
        //the game over text
        gameOverText = new Text("Game Over");
        styleTextBottom(gameOverText);
        grid.add(gameOverText, 0, TetrisBoard.NUM_ROWS, BTM_SPAN, BTM_SPAN);

        // stops the game
        gameOver = true;
        timer.stop();
    }

    /**
     * Displays or removes the bottom pause message depending on whether or not
     *     the game is paused
     * @param grid the layout grid
     */
    private void displayPause(GridPane grid) {
        if (pauseGame) {
            grid.add(pauseText, 0, TetrisBoard.NUM_ROWS, BTM_SPAN, BTM_SPAN);
        }
        else {
            grid.getChildren().remove(pauseText);
        }
    }

    /**
     * Creates the Text for the score and adds it the sidebar
     * @param grid the sidebar gridpane
     * @return the created Text for the score
     */
    private Text createScore(GridPane grid) {
        Text scoreLabel = new Text("Score\n " + score);
        styleText(scoreLabel);
        GridPane.setMargin(scoreLabel, new Insets(20, 0, 15, 0));
        grid.add(scoreLabel, 0, SCORE_IDX, SCORE_SPAN, SCORE_SPAN);

        return scoreLabel;
    }

    /**
     * Sets the style of the text for messages appearing at the bottom
     * @param text the text being styled
     */
    private void styleTextBottom(Text text) {
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD,
                BTM_TEXT_SIZE));
        text.setFill(Color.BLACK);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
    }

    /**
     * Sets the style of the text for the sidebar
     * @param text the Text being styled
     */
    private void styleText(Text text) {
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD,
                SIDE_TEXT_SIZE));
        text.setTextAlignment(TextAlignment.CENTER);
        GridPane.setHalignment(text, HPos.CENTER);
    }

    /**
     * Updates the score and its display based on number of lines cleared
     * @param linesCleared the number of lines cleared, -1 for a game restart
     * @param scoreLabel the Text for the score
     */
    private void updateScore(int linesCleared, Text scoreLabel) {
        switch (linesCleared) {
            case 1:
                score += SCORE_1;
                break;
            case 2:
                score += SCORE_2;
                break;
            case 3:
                score += SCORE_3;
                break;
            case 4:
                score += SCORE_4;
                break;
            case -1:
                score = 0;
                break;
        }

        scoreLabel.setText("Score\n " + score);
    }

    /**
     * Updates the display of the nex block preview box
     * @param preview the preview gridpane
     * @param sideGrid the sidebar gridpane
     * @param piece the next tetromino
     */
    private void updatePreviewBox(GridPane preview, GridPane sideGrid,
                                  Tetromino piece) {
        sideGrid.getChildren().remove(preview);
        for (int i = 0; i < Tetromino.MATRIX_SIZE; i++) {
            for (int j = 0; j < Tetromino.MATRIX_SIZE; j++) {
                Rectangle tile;
                if (piece != null) {
                    tile = new Rectangle(PRE_TILE_SIZE, PRE_TILE_SIZE,
                            findColor(piece.getMatrix()[i][j]));
                }
                else {
                    tile = new Rectangle(PRE_TILE_SIZE, PRE_TILE_SIZE,
                            Color.SILVER);
                }
                preview.add(tile, j, i);
            }
        }

        sideGrid.add(preview, 0, 1);
    }

    /**
     * Pauses or unpauses the game depending on the state of pauseGame
     * @param grid the board gridpane
     */
    private void pauseGame(GridPane grid) {
        displayPause(grid);
        if (pauseGame) {
            timer.stop();
        }
        else {
            timer.start();
        }
    }

    /**
     * Restarts the game
     * @param grid the board gridpane
     * @param scoreLabel the Text for the score
     * @param sideGrid the sidebar gridpane
     * @param preview the preview box gridpane
     * @param layoutGrid the layout gridpane
     */
    private void restart(GridPane grid, Text scoreLabel, GridPane sideGrid,
                         GridPane preview, GridPane layoutGrid) {
        // reset board
        board = new TetrisBoard();
        updateBoardLandedGUI(grid);

        // reset falling tetromino and sidebar
        currPiece = spawnTetromino();
        nextPiece = spawnTetromino();
        updatePreviewBox(preview, sideGrid, nextPiece);
        updateScore(-1, scoreLabel);

        // reset variables and bottom texts
        gameOver = false;
        pauseGame = false;
        layoutGrid.getChildren().remove(pauseText);
        layoutGrid.getChildren().remove(gameOverText);

        timer.start();
    }

    /**
     * Sets the music
     */
    private void setMusic() {
        String musicFile = "src/main/resources/Vicious.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
        });
        mediaPlayer.setVolume(VOL_START);
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
