package ast;

public class AddFunc extends Add {
    private String sectionName;
    private String name;
    private String startTime;

    public AddFunc(String sectionName, String name, String startTime) {
        this.sectionName = sectionName;
        this.name = name;
        this.startTime = startTime;
    }

    public String getSectionName() {return sectionName;}
    public String getName() {return name;}
    public String getStartTime() {return startTime;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
