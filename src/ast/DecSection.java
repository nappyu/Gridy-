package ast;

public class DecSection extends Dec{
    private String name;
    private String duration;

    public DecSection(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {return name;}
    public String getDuration() {return duration;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
