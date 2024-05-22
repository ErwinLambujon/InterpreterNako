package Analysis.ParseTree.Expression;
//OK NA
import Analysis.SyntaxAnalyzer.Token;

public class IdentifierNode extends ExpressionNode {
    private final Token identifierToken;
    private final String name;

    public IdentifierNode(Token identifierToken, String name) {
        this.identifierToken = identifierToken;
        this.name = name;
    }

    public Token getIdentifierToken() {
        return identifierToken;
    }

    public String getName() {
        return name;
    }
}
