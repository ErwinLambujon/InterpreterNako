package Analysis.ParseTree.Statement;
//OK NA
import Analysis.SyntaxAnalyzer.Token;
import Analysis.ParseTree.Expression.ExpressionNode;
import java.util.List;

public class AssignmentNode extends StatementNode {
    /* Used List to handle example x = y = z = 10.
    Also used if in the future we want to implement "+=", "-="*/
    private final List<String> identifiers;
    private final List<Token> equalsTokens;
    private final ExpressionNode expression;

    public AssignmentNode(List<String> identifiers, List<Token> equalsToken, ExpressionNode expression) {
        this.identifiers = identifiers;
        this.equalsTokens = equalsToken;
        this.expression = expression;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public List<Token> getEqualsTokens() {
        return equalsTokens;
    }

    public ExpressionNode getExpression() {
        return expression;
    }
}
