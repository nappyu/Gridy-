package ast;

public interface Visitor<T> {
    // One visit method for each concrete AST node subclasses
    T visit(Program p);
    T visit(GCreate gCreate);
    T visit(AddSection addSection);
    T visit(Difficulty difficulty);
    T visit(DecCell dCell);
    T visit(DecRepeat dRepeat);
    T visit(DecFunction dFunction);
    T visit(DecSection dSection);
    T visit(AddSimp addSimp);
    T visit(AddFunc addFunc);
}
