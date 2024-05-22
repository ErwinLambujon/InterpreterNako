package Analysis.ParseTree.Expression;
//OK NA
import Analysis.SyntaxAnalyzer.Token;

public class BinNode extends ExpressionNode {
    private final ExpressionNode leftHandSide;
    private final Token tokenOperator;
    private final ExpressionNode rightHandSide;

    public BinNode(ExpressionNode leftHandSide, Token tokenOperator, ExpressionNode rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.tokenOperator = tokenOperator;
        this.rightHandSide = rightHandSide;
    }

    public ExpressionNode getLeftHandSide() {
        return leftHandSide;
    }

    public Token getTokenOperator() {
        return tokenOperator;
    }

    public ExpressionNode getRightHandSide() {
        return rightHandSide;
    }
}
