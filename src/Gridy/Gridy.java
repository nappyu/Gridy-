package Gridy;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import javafx.scene.media.MediaPlayer;
import ui.GameCell;
import javafx.scene.paint.Color;

public class Gridy {

    private int x;
    private int y;
    private int time;
    private int currInd;
    private String fileName;
    private ArrayList<Section> list;
    private int difficulty;
    private final static double t_step = 0.5; // unit time step in seconds
    private int FPS = 2;
    private Cell[][][] fullMatrix;
    private PriorityQueue<GameCell> gameCellQueue = new PriorityQueue<GameCell>(new Comparator<GameCell>() {
        @Override
        public int compare(GameCell o1, GameCell o2) {
            if (o1.getStartTime() >= o2.getStartTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    });

    // some datastructure needs empty datastructure when gridy is initalized

    /**
     * creator method for gridy in the case of no music
     * @param x how many blocks wide the game will be
     * @param y how many blocks long the game will be
     * @param time how long the game is
     */
    public Gridy(int x, int y, int time) {
        this.x = x;
        this.y = y;
        this.time = time;
        currInd = 0;
        this.difficulty = 1;
        this.list = new ArrayList<>();
        fullMatrix = new Cell[time][x][y];
    }

    /**
     * creator method for gridy in the case of a filepath to a music file
     * @param x how many blocks wide the game will be
     * @param y how many blocks long the game will be
     * @param time how long the game is
     * @param fileName the absolute file path to the music file to be played during the game
     */
    public Gridy(int x, int y, int time, String fileName) {
        this.x = x;
        this.y = y;
        this.time = time;
        currInd = 0;
        this.fileName = fileName;
        this.difficulty = 1;
        this.list = new ArrayList<>();
        fullMatrix = new Cell[time][x][y];
    }

    private int currTime = 0;
    /**
     * adds a section to the game in sequential order
     * @param s the section to be added
     */
    public void addSection(Section s) {
        /**
        list.add(s);
        // add the matrix directly to it
        int s_length = s.getDuration();
        for (int i = 0; i < s_length; i++) {
            if (currInd >= time) {break;}
            for (int x = 0; x < this.x; x++) {
                for (int y = 0; y < this.y; y++) {
                    // copy over cell only if it's not null
                    if (s.getSection()[i][x][y] != null) {
                        fullMatrix[currInd][x][y] = s.getSection()[i][x][y];
                    }
                }
            }
            currInd++;
        }*/
        for (int t = 0; t < s.getDuration(); t ++) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (s.getSection()[t][i][j] != null) {
                        Cell c = s.getSection()[t][i][j];
                        double startTime = t * t_step;
                        double fadeOut = c.getFadeout();
                        gameCellQueue.add(new GameCell(startTime += currTime, fadeOut, c.getScore(), c.getColor(), i, j));
                    }
                }
            }
        }
        currTime += s.getDuration();
    }

    /**
     * adds a section to the game at specified point in time
     * @param s the section to be added
     * @param startTime the starting index of fullMatrix (relative to the whole game)
     */
    public void addSection(Section s, int startTime) {
        list.add(s);
        // add the matrix directly to it
        int s_length = s.getDuration();
        int index = startTime;
        for (int i = 0; i < s_length; i++) {
            if (index >= time) {break;}
            for (int x = 0; x < this.x; x++) {
                for (int y = 0; y < this.y; y++) {
                    // copy over cell only if it's not null
                    if (s.getSection()[i][x][y] != null) {
                        fullMatrix[index][x][y] = s.getSection()[i][x][y];
                    }
                }
            }
            index++;
        }
        // check at the end if index is greater than currInd, set currInd to index
        if (index > currInd) {
            currInd = index;
        }
    }

    /**
     * Sets the game's difficulty by setting the unit time.
     * Defaulted to 1 upon game's creation.
     * @param t unit time
     */
    public void setDifficulty(int t) {this.difficulty = t;}

    public int getDifficulty() {return difficulty;}

    public int getX() {return this.x;}

    public int getY() {return this.y;}

    public PriorityQueue<GameCell> getGameCellQueue(){return this.gameCellQueue;}

    public String getFileName(){return  this.fileName;}

    public int getTime(){return this.time;}
}

