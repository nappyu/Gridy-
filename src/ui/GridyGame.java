package ui;
import Gridy.Gridy;
import ast.Evaluator;
import ast.Parser;
import ast.Program;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import libs.SimpleTokenizer;
import libs.Tokenizer;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

/**
 * JavaFX setup for IntelliJ
 * https://www.jetbrains.com/help/idea/javafx.html
 */
public class GridyGame extends Application {
    private static Gridy game;
    private static final String DEFAULT_BUTTON_COLOR = "-fx-background-color: white";
    private static final int CLICK_PENALTY = 100;
    //Between [0,1], if the user clicks the button when opacity is above threshold they get max points
    private static final double MAX_POINT_THRESHOLD = 0.75;
    private static int gridWidthInSquares = 3;
    private static int gridHeightInSquares = 3;
    private static int squareWidthPx = 100;
    private static final double DIFFICULTY_INCREMENTS = 0.2;
    private boolean gameStarted = false;
    private static String bgmFile;
    private static double gameDuration;
    private static int difficulty;
    private static double timeMultiplier;
    private MediaPlayer mediaPlayer;
    int score = 0;
    GridPane gp;
    private Map<Button, GameCell> activeCells = new HashMap<>();
    private static PriorityQueue<GameCell> gameCellQueue = new PriorityQueue<GameCell>(new Comparator<GameCell>() {
        @Override
        public int compare(GameCell o1, GameCell o2) {
            if (o1.getStartTime() >= o2.getStartTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    });

    public static void main(String args[]) {
        try {
            Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("input.tcts");
            System.out.println("Done tokenizing");

            Parser p = Parser.getParser(tokenizer);
            Program program = p.parseProgram();
            System.out.println("Done parsing");

            Evaluator e = new Evaluator();
            program.accept(e);
            System.out.println("Completed language processing successfully");

            System.out.println("========================= Starting Gridy Game . . . ========================");
            System.out.println(e.getGame());

            difficulty = e.getGame().getDifficulty() - 1;
            timeMultiplier = 1 + (DIFFICULTY_INCREMENTS * difficulty);
            gameCellQueue = e.getGame().getGameCellQueue();
            gridWidthInSquares = e.getGame().getX();
            gridHeightInSquares = e.getGame().getY();
            bgmFile = e.getGame().getFileName();
            gameDuration = e.getGame().getTime();

            launch(args);

        } catch (RuntimeException e) {
            System.err.println("!!! ============ GRIDY FAILED TO COMPILE ============ !!!");
            System.err.println("COMPILE ERROR: The language written in input.tcts failed to compile. Double check the syntax, " +
                    "if any of the parameters are out of bounds (i.e., over time limit, over grid size, etc.), or check " +
                    "for undeclared or uninitialized variables.");
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        calculateSquareWidthPx();
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(gridWidthInSquares * squareWidthPx, gridHeightInSquares * squareWidthPx);
        gridPane.getStyleClass().add("gameGrid");
        gridPane.toBack();
        gp = gridPane;

        // fill scene with default buttons
        for (int i = 0; i < gridWidthInSquares; i++) {
            for (int j = 0; j < gridHeightInSquares; j++) {
                Button button = getDefaultPlaceholderButton();
                gridPane.add(button, i, j);
            }
        }

        Label sampleLabel = new Label("CLICK HERE TO BEGIN");
        sampleLabel.getStyleClass().add("score");

        VBox vBox = new VBox(sampleLabel, gridPane);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(getClass().getResource("stylesheets/Gridy.css").toExternalForm());
        stage.setTitle("Gridy");
        stage.setScene(scene);

        class GameTimer extends AnimationTimer{
            private long nanoStartTime;
            public GameTimer(long startTime){
                this.nanoStartTime = startTime;
            }
            public void handle(long currentNanoTime) {
                double timeElapsed = (currentNanoTime - this.nanoStartTime) * timeMultiplier / 1000000000.0; // multiplying the time for difficulty

                // stop the game loop once there are no more active or future cells
                // activeCells.isEmpty() && gameCellQueue.isEmpty()
                if (timeElapsed > gameDuration) {
                    gameStarted = false;
                    this.stop();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    Label gameOverLabel = new Label("Game Over");
                    gameOverLabel.getStyleClass().add("score");
                    vBox.getChildren().remove(1);
                    vBox.getChildren().add(gameOverLabel);
                }

                updateActiveCells(timeElapsed);
                drawNewCells(timeElapsed, gridPane);

                // draw current score
                String pointsText = "Score: " + score;
                sampleLabel.setText(pointsText);
            }
        }

        //Listen for mouse click to start the game
        sampleLabel.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!gameStarted) {
                    gameStarted = true;
                    if (bgmFile != null) {
                        startBackgroundMusic();
                    }
                    AnimationTimer gameTimer = new GameTimer(System.nanoTime());
                    gameTimer.start();
                }
            }
        });
        stage.show();
        System.out.println("scene height: " + stage.getHeight());
        System.out.println("Cell width: " + squareWidthPx);
    }

    private void startBackgroundMusic() {
        try{
            //no copyright music - lofi type beat biscuit free vlog music prod. by lukrembo.mp3
            String pathToMusicFolder = "bgm/";
            Media bgm = new Media(new File(pathToMusicFolder + bgmFile).toURI().toString());
            mediaPlayer = new MediaPlayer(bgm);
            mediaPlayer.play();
        } catch (Exception e){
            System.out.println("Error with playing background music: " + e.getMessage());
        }

    }

    private void calculateSquareWidthPx() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();
        //Setting the size of each cell based on user's screen width/height and x,y of game grid
        if (gridHeightInSquares >= gridWidthInSquares || screenWidth / gridWidthInSquares * gridHeightInSquares >= screenHeight){
            squareWidthPx = (int)(screenHeight / (gridHeightInSquares + 1));
        }
        else{
            squareWidthPx = (int)(screenWidth / (gridWidthInSquares + 1));
        }
        System.out.println(squareWidthPx);
    }

    /**
     * Draw new active cells if they're ready
     * @param t Elapsed time in milliseconds
     * @param gridPane The grid on which to draw cells
     */
    private void drawNewCells(double t, GridPane gridPane) {
        GameCell cell = gameCellQueue.peek();
        if (cell != null && Double.compare(t, cell.getStartTime()) > 0) {
            cell = gameCellQueue.poll();
            Button b = new Button();
            b.setMinSize(squareWidthPx, squareWidthPx);
            b.setStyle(String.format("-fx-background-color: #%s", cell.getColor().toString().substring(2)));
            b.setOnMouseClicked(clickHandler);
            activeCells.put(b, cell);
            gridPane.add(b, cell.getxCoord(), cell.getyCoord());
        }
    }

    /**
     * Update the opacity of currently active cells in a thread safe way
     * Expired cells are replaced with default buttons and their points are deducted from the score
     * @param t Elapsed time in seconds
     */
    private void updateActiveCells(double t) {
        Iterator<Map.Entry<Button, GameCell>> iterator = activeCells.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Button, GameCell> entry = iterator.next();
            Button b = entry.getKey();
            GameCell c = entry.getValue();
            double currentOpacity = (c.getFadeoutTime() - (t - c.getStartTime())) / c.getFadeoutTime();
            if (currentOpacity <= 0.0) {
                // the button has expired
                b.setOpacity(0.0);
                score -= c.getPoints();
                iterator.remove();
            } else {
                b.setOpacity(currentOpacity);
            }
        }
    }

    private EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Button b = (Button) mouseEvent.getSource();
            Label pointsEarned = new Label();

            if (activeCells.containsKey(b)) {
                double opacity = b.getOpacity();
                int earned;
                if (opacity > MAX_POINT_THRESHOLD){
                    earned = activeCells.get(b).getPoints();
                }
                else{
                    earned = (int)(activeCells.get(b).getPoints() * opacity / MAX_POINT_THRESHOLD);
                }
                score += earned;
                pointsEarned = new Label("+" + earned);
                pointsEarned.setTextFill(Color.GREEN);
                removeActiveCell(b);
            } else {
                if (gameStarted) {
                    score -= CLICK_PENALTY;
                    pointsEarned = new Label("-" + CLICK_PENALTY);
                    pointsEarned.setTextFill(Color.RED);
                }
            }

            double labelOffsetY = 0.507*squareWidthPx + 46.324;
            pointsEarned.setTranslateX(mouseEvent.getSceneX() - 5);
            pointsEarned.setTranslateY(mouseEvent.getSceneY() - labelOffsetY);
            pointsEarned.toFront();
            pointsEarned.setFont(new Font("Arial", 14));

            //Fade out time of the points earned label
            FadeTransition fadeOut = new FadeTransition(Duration.millis(1000));
            fadeOut.setNode(pointsEarned);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setCycleCount(1);
            fadeOut.playFromStart();
            Label finalPointsEarned = pointsEarned;
            fadeOut.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent actionEvent) {
                    gp.getChildren().remove(finalPointsEarned);
                }
            });

            gp.getChildren().add(pointsEarned);

//            System.out.format("screen Mouse pressed at x=%f , y=%f\n",mouseEvent.getScreenX(),mouseEvent.getScreenY());
//            System.out.format("scene Mouse pressed at x=%f , y=%f\n",mouseEvent.getSceneX(),mouseEvent.getSceneY());
            System.out.println(String.format("Button clicked at: column=%d, row=%d", GridPane.getColumnIndex(b), GridPane.getRowIndex(b)));
        }
    };

    /**
     * removes the given active cell from the board and replaces it with the default button
     * WARNING: NOT THREAD SAFE
     * If you need to remove a button from the activeCells list while iterating over it, use an Iterator
     * @param activeButton the Button to remove and replace
     */
    private void removeActiveCell(Button activeButton) {
        int x = GridPane.getColumnIndex(activeButton);
        int y = GridPane.getRowIndex(activeButton);

        GridPane gridPane = (GridPane) activeButton.getParent();
        gridPane.getChildren().remove(activeButton);
        activeCells.remove(activeButton);
        Button b = getDefaultPlaceholderButton();

        gridPane.add(b, x, y);
    }

    /**
     * Used to fill the board with default buttons in the beginning
     * Also used to replace active buttons after they're clicked or after they expire
     * @return a default blank button
     */
    private Button getDefaultPlaceholderButton() {
        Button b = new Button();
        b.setMinSize(squareWidthPx, squareWidthPx);
        b.setOnMouseClicked(clickHandler);
        b.getStyleClass().add("defaultCell");
        return b;
    }
}
