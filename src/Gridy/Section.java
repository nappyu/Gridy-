package Gridy;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Arrays;

public class Section {
    private int duration;
    private Cell[][][] section;
    private int x;
    private int y;
    private final int FPS = 30;


    // Need a way to find out x,y of the game, pass it as a parameter?
    /**
     * Creator method for Section
     * @param duration how long a section will last for
     */
    public Section(int duration, int x, int y) {
        //this.duration = duration * FPS;
        this.duration = duration;
        this.x = x;
        this.y = y;
        section = new Cell[duration][x][y];
    }

    /**
     * get function for duration
     * @return duration in seconds, how long the section will last for
     */
    public int getDuration() {
        return duration;
    }

    /**
     * get function for cell matrix
     * @return the cell matrix
     */
    public Cell[][][] getSection() {
        return section;
    }

    /**
     * adds repeat to the section at x,y coord and start the repeat at startime relative to the start
     * of the section
     * @param repeat the repeat to be added
     * @param x x coord
     * @param y y coord
     * @param startTime start time relative to the start of the section in T (unit step)
     */
    public void add(Repeat repeat, int x, int y, int startTime) {
        // int delay = repeat.getFrequency() * FPS;
        int delay = repeat.getFrequency();
        for (int t = startTime; t < duration; t += delay ) {
            section[t][x][y] = repeat.getCell();
        }
    }

    /**
     * adds cell to the section at x,y coord and start the repeat at startime relative to the start
     * of the section
     * @param cell the cell to be added
     * @param x x coord
     * @param y y coord
     * @param startTime start time relative to the start of the section in T (unit step)
     */
    public void add(Cell cell, int x, int y, int startTime) {
        section[startTime][x][y] = cell;
    }


    /**
     * adds f to the section at x,y coord and start the repeat at startime relative to the start
     * of the section
     * @param f the function to be added
     * @param startTime start time relative to the start of the section in unit T
     */
    public void add(Function f, int startTime) {

        int duration = f.getDuration();
        for (int t = 0; t < duration; t++) {
            Expression calcX = new ExpressionBuilder(f.getEquationX()).variable("t").build().setVariable("t", t);
            Expression calcY = new ExpressionBuilder(f.getEquationY()).variable("t").build().setVariable("t", t);
            double resX = calcX.evaluate();
            double resY = calcY.evaluate();
            int x = (int)resX;
            int y = (int)resY;
            x = x % this.x;   /// doing modulo on x and y here so we never get index out of bounds error
            y = y % this.y;
            int ind = startTime + t;
            if (ind < this.duration) {
                section[ind][x][y] = f.getCell();
            }
        }
    }


    // Probably need helper methods for adding repeats and functions, is it the sections job???

    // Seems like repeat is implemented already above (not sure if that's the final/correct version tho).
    // add(Function ... ) method is called once, and we want to fill out the section's matrix (which is a field within Section) based
    // on the function. So after parsing the function, we should populate the matrix here accordingly.. so it seems like the section's job.
    // But again, if you have a better method, please go ahead with any solution :)


}
