package Analysis.ParseTree.Statement;
//OK NA
import Analysis.SyntaxAnalyzer.Token;
import Analysis.ParseTree.Expression.ExpressionNode;
import Analysis.ParseTree.ProgramNode;

public class LoopNode extends StatementNode {
    //This uses Token type because we are only expecting the WHILE token
    private final Token loopToken;
    /*This uses ExpressionNode type because in
        WHILE loop you can only do single expression.
        Example below:
            while (expression) {
                statements
            }
    */
    private final ExpressionNode expression;
    private final ProgramNode stmt;

    public LoopNode(Token loopToken, ExpressionNode expression, ProgramNode stmt) {
        this.loopToken = loopToken;
        this.expression = expression;
        this.stmt = stmt;
    }

    public Token getLoopToken() {
        return loopToken;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public ProgramNode getStatement() {
        return stmt;
    }
}
