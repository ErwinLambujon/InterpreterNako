package Analysis.ParseTree.Expression;
//OK NA
import Analysis.SyntaxAnalyzer.*;

public class LiteralNode extends ExpressionNode {
    private final Token literalToken;
    private final Object literal;

    public LiteralNode(Token literalToken, Object literal) {
        /*
            literal is of type object so that it can accept any type of
            literal like INT, FLOAT, STRING, etc. Example is 1 + 5 you can
            do this literal because it is an object.
        */
        this.literalToken = literalToken;
        this.literal = literal;
    }

    public Token getLiteralToken() {
        return literalToken;
    }

    public Object getLiteral() {
        return literal;
    }
}
