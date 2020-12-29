package Gridy;

import javafx.scene.paint.Color;

public class Cell {

    private double fadeout;
    private int score;
    private Color color;


    /**
     * Creator method for the Cell object
     * @param fadeout fadeout time in seconds, how long a block lasts on the screen for
     * @param color the color of the cell
     * @param score the score of the cell, how much it is worth when a player hits it on time
     */
    public Cell(double fadeout, Color color, int score) {
        this.fadeout = fadeout;
        this.score = score;
        this.color = color;
    }

    /**
     * getter function for fadeout
     * @return the fadeout time
     */
    public double getFadeout() {
        return fadeout;
    }

    /**
     * getter function for color
     * @return the color of the cell
     */
    public Color getColor() {
        return color;
    }

    /**
     * getter function for score
     * @return the score value for the cell
     */
    public int getScore() {
        return score;
    }
}
