package ast;

public class AddSimp extends Add {
    private String sectionName;
    private String name;  // could be cell or repeat
    private String X;
    private String Y;
    private String startTime;

    public AddSimp(String sectionName, String name, String x, String y, String startTime) {
        this.sectionName = sectionName;
        this.name = name;
        X = x;
        Y = y;
        this.startTime = startTime;
    }

    public String getSectionName() {return sectionName;}
    public String getName() {return name;}
    public String getX() {return X;}
    public String getY() {return Y;}
    public String getStartTime() {return startTime;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
