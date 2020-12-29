package ast;

public class DecRepeat extends Dec{
    private String name;
    private String cellName;
    private String frequency;
    private String duration;

    public DecRepeat(String name, String cellName, String frequency, String duration) {
        this.name = name;
        this.cellName = cellName;
        this.frequency = frequency;
        this.duration = duration;
    }

    public String getName() {return name;}
    public String getCellName() {return cellName;}
    public String getFrequency() {return frequency;}
    public String getDuration() {return duration;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
