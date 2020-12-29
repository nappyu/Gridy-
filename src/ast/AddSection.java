package ast;

public class AddSection extends GEXP {
    private String name;
    private String startTime;

    public AddSection(String name) {this.name = name;}
    public AddSection(String name, String startTime) {
        this.name = name;
        this.startTime = startTime;
    }

    public String getName(){return name;}
    public String getStartTime(){return startTime;}

    public void setName(String name){this.name = name;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
