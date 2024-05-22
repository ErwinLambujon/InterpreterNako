package Analysis.ParseTree.Statement;
//OK NA
import Analysis.SyntaxAnalyzer.Token;
import Analysis.ParseTree.Expression.ExpressionNode;
import Analysis.ParseTree.*;
import java.util.List;

public class ConditionalNode extends StatementNode {
    /* List is used so that it can handle nested or compound conditional statements
       Like in the tokens you can have if and else
    */
    private final List<Token> tokens;
    private final List<ExpressionNode> expressions;
    private final List<ProgramNode> stmt;

    public ConditionalNode(List<Token> tokens, List<ExpressionNode> expressions, List<ProgramNode> stmt) {
        this.tokens = tokens;
        this.expressions = expressions;
        this.stmt = stmt;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }

    public List<ProgramNode> getStmt() {
        return stmt;
    }
}
