package Analysis.ParseTree.Statement;
//OK NA
import Analysis.SyntaxAnalyzer.Token;
import Analysis.ParseTree.Expression.ExpressionNode;
import java.util.Map;

public class VariableDeclarationNode extends StatementNode {
    private final Token TokenType;
    /*
        The String is the key which is the variable name
        while the ExpressionNode is the value which is the
        initialized value of the variable.
        Example below:
        INT x = 5
        x = key / String
        5 = value / initialized value
     */
    private final Map<String, ExpressionNode> variables;


    public VariableDeclarationNode(Token TokenType, Map<String, ExpressionNode> variables) {
        this.TokenType = TokenType;
        this.variables = variables;
    }
    //Returns the TokenType, which represents the data type of the variables.
    public Token getDataTypeToken() {
        return TokenType;
    }

    //Returns the variables map, which contains the variable names and their initialization values.
    public Map<String, ExpressionNode> getVariables() {
        return variables;
    }
}