package ast;

public class DecCell extends Dec{
    private String name;
    private String duration;
    private String color;
    private String score;

    public DecCell(String name, String duration, String color, String score) {
        this.name = name;
        this.duration = duration;
        this.color = color;
        this.score = score;
    }

    public String getName() {return name;}
    public String getDuration(){return duration;}
    public String getColor(){return color;}
    public String getScore(){return score;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
