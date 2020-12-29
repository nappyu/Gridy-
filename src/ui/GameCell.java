package ui;

import javafx.scene.paint.Color;

public class GameCell {
    private double startTime;
    private double fadeoutTime;
    private int points;
    private Color color;
    private int xCoord;
    private int yCoord;

    public GameCell(double startTime, double fadeoutTime, int points, Color color, int xCoord, int yCoord) {
        this.startTime = startTime;
        this.fadeoutTime = fadeoutTime;
        this.points = points;
        this.color = color;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getFadeoutTime() {
        return fadeoutTime;
    }

    public void setFadeoutTime(double fadeoutTime) {
        this.fadeoutTime = fadeoutTime;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
