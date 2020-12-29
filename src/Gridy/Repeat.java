package Gridy;


public class Repeat {

    private Cell cell;
    private int frequency;
    private int duration;

    /**
     * Creator method for Repeat
     * NOTE: the number of times a cell repeats is frequency/duration FLOOR
     * @param cell the cell we want to use for this repeat function
     * @param frequency the frequency in which a cell appears
     * @param duration how long we want to repeat for
     */
    public Repeat(Cell cell, int frequency, int duration) {
        this.cell = cell;
        this. frequency = frequency;
        this.duration = duration;
    }

    /**
     * getter function for Cell
     * @return the cell to be repeated
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * getter function for frequency in seoonds
     * @return the frequency in seconds between appearances
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * getter function for  duration
     * @return how long to repeat for in seconds
     */
    public int getDuration() {
        return duration;
    }

}
