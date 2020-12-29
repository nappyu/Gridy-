package ast;

public class DecFunction extends Dec{
    private String name;
    private String xEquation;
    private String yEquation;
    private String cellName;
    private String duration;

    public DecFunction(String name, String xEquation, String yEquation, String cellName, String duration) {
        this.name = name;
        this.xEquation = xEquation;
        this.yEquation = yEquation;
        this.cellName = cellName;
        this.duration = duration;
    }

    public String getName() {return name;}
    public String getxEquation() {return xEquation;}
    public String getyEquation() {return yEquation;}
    public String getCellName() {return cellName;}
    public String getDuration() {return duration;}

    @Override
    public <T> T accept(Visitor<T> v) {return v.visit(this);}
}
