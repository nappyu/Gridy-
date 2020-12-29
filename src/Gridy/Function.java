package Gridy;

public class Function {
    private String equationX;
    private String equationY;
    private Cell cell;
    private int duration;

    // How do we bring in the universal time t into this function as it is relative to the entire game

    /**
     * generates a pattern in a function based on the inputted equation for x and y for a cell
     * @param x the equation we use for the x coordinate
     * @param y the equation we use for the y coordinate
     * @param c the cell we use for this function
     */
    public Function(String x, String y, Cell c, String duration) {
        equationX = x;
        // x = t % x.size
        equationY = y;
        cell = c;
        this.duration = Integer.parseInt(duration);
    }

    public String getEquationX() {return this.equationX;}

    public String getEquationY() {return this.equationY;}

    public Cell getCell() {return this.cell;}

    public int getDuration() {return this.duration;}

    // We want to parse the equation string to make something that we can actually use
}
