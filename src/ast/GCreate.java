package ast;

public class GCreate extends GEXP {
    private String X;
    private String Y;
    private String duration;
    private String fileName;

    public GCreate(String x, String y, String duration) {
        X = x;
        Y = y;
        this.duration = duration;
    }

    public GCreate(String x, String y, String duration, String directory) {
        X = x;
        Y= y;
        this.duration = duration;
        this.fileName = directory;
    }

    public String getX() {return X;}
    public String getY() {return Y;}
    public String getDuration() {return duration;}
    public String getFileName() {return fileName;}

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
