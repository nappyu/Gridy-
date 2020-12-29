package ast;

public class Difficulty extends GEXP {
    private String level;

    public Difficulty(String level) {
        this.level = level;
    }

    public String getLevel() {return level;}

    public void setLevel(String level) {this.level = level;}

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }
}
