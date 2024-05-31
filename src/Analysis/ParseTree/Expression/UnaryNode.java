package Analysis.ParseTree.Expression;
//OK NA
//**DID THIS FOR THE SAKE OF USER AUTH IN COMMIT**
import Analysis.SyntaxAnalyzer.Token;

public class UnaryNode extends ExpressionNode {
    private final Token tokenOperator;
    private final ExpressionNode expression;

    public UnaryNode(Token tokenOperator, ExpressionNode expression) {
        this.tokenOperator = tokenOperator;
        this.expression = expression;
    }

    public Token getTokenOperator() {
        return tokenOperator;
    }

    public ExpressionNode getExpression() {
        return expression;
    }
}
