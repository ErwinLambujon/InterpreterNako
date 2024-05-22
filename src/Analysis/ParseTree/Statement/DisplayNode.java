package Analysis.ParseTree.Statement;

import Analysis.SyntaxAnalyzer.Token;
import Analysis.ParseTree.Expression.ExpressionNode;
import java.util.List;
//OK NA
public class DisplayNode extends StatementNode {
    // This uses Token type since we are only expecting the DISPLAY token
    private final Token displayToken;
    // This uses List so that we can perform multiple expressions like A + B & 1 + 5, etc.
    //Inside the '< >' is the type of data that the expression can contain.
    private final List<ExpressionNode> expressions;

    public DisplayNode(Token displayToken, List<ExpressionNode> expressions) {
        this.displayToken = displayToken;
        this.expressions = expressions;
    }

    public Token getDisplayToken() {
        return displayToken;
    }

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }
}
