package Analysis;
import Analysis.ParseTree.*;
import Analysis.ParseTree.Expression.ExpressionNode;
import Analysis.ParseTree.Expression.BinNode;
import Analysis.ParseTree.Expression.UnaryNode;
import Analysis.ParseTree.Expression.ParenthesisNode;
import Analysis.ParseTree.Expression.IdentifierNode;
import Analysis.ParseTree.Expression.LiteralNode;
import Analysis.ParseTree.Statement.*;
import Analysis.TokenDataTypes.TokenType;
import Analysis.SymbolTable.VariableTable;
import java.util.List;
import Analysis.SyntaxAnalyzer.*;
import java.util.Map;
//OK NA
public class Interpreter {
    private VariableTable variableTable;
    private ProgramNode program;

    public Interpreter(String code) throws Exception{
        Lexer lex = new Lexer(code);

        Parser parser = new Parser(lex);
        Semantic semantic = new Semantic();

        TokenType tokenType = TokenType.CODE; // Assuming your program starts with CODE token
        program = parser.parseProgram(tokenType);

        semantic.analyze(program);
        variableTable = new VariableTable();

    }

    public void execute(ProgramNode statementBlock) {
        ProgramNode prog = statementBlock == null ? program : statementBlock;

        for (StatementNode stmt : prog.getStatements()) {
            if (stmt instanceof VariableDeclarationNode)
                runVariableDeclaration((VariableDeclarationNode) stmt);
            else if (stmt instanceof AssignmentNode)
                runAssignment((AssignmentNode) stmt);
            else if (stmt instanceof DisplayNode)
                runDisplay((DisplayNode) stmt);
            else if (stmt instanceof ScanNode)
                runScan((ScanNode) stmt);
            else if (stmt instanceof ConditionalNode)
                runCondition((ConditionalNode) stmt);
            else if (stmt instanceof LoopNode)
                runLoop((LoopNode) stmt);
        }
    }

    private void runVariableDeclaration(VariableDeclarationNode statement) {
        // Loop through the map of variables
        for (Map.Entry<String, ExpressionNode> entry : statement.getVariables().entrySet()) {
            // Get the variable name
            String identifier = entry.getKey();

            // Set default value to null
            Object value = null;

            // If the variable value is not null (e.g., INT a = 5)
            if (entry.getValue() != null) {
                value = runExpression(entry.getValue());
            }

            // Add variable to table of variables
            variableTable.addVariable(identifier, Grammar.getDataType(statement.getDataTypeToken().getTokenType()), value);
        }
    }
    private void runAssignment(AssignmentNode statement) {
        Object value = null;
        for (String identifier : statement.getIdentifiers()) {
            value = runExpression(statement.getExpression());
            variableTable.addValue(identifier, value);
        }
    }

    private void runDisplay(DisplayNode statement) {
        StringBuilder result = new StringBuilder();
        for (ExpressionNode expression : statement.getExpressions())
            result.append(runExpression(expression));

        System.out.print(result.toString());
    }

    private void runScan(ScanNode stmt) {
        List<String> values = null;
        List<String> identifiers = stmt.getIdentifiers();
        String inputted = "";

        System.out.print("");
        inputted = new java.util.Scanner(System.in).nextLine();
        values = java.util.Arrays.asList(inputted.replace(" ", "").split(","));

        if (values.size() != identifiers.size())
            throw new RuntimeException("Incomplete/Missing inputs.");

        Object value;
        int index = 0;
        for (String val : values) {
            value = Grammar.convertValue(val);

            if (!Grammar.matchDataType(variableTable.getType(identifiers.get(index)), Grammar.getDataType(value)))
                throw new RuntimeException("Data Type Mismatch " + Grammar.getDataType(value) + " to \"" + identifiers.get(index) + "\".");

            variableTable.addValue(identifiers.get(index), value);
            index++;
        }
    }

    private void runCondition(ConditionalNode statement) {
        boolean displayed = false;
        int index = 0;

        for (ExpressionNode expression : statement.getExpressions()) {
            if (expression != null) {
                if ((boolean) runExpression(expression)) {
                    displayed = true;
                    execute(statement.getStmt().get(index));
                    break;
                }
            } else

                break;

            List<Token> tokens = statement.getTokens();
            if (tokens.size() > 0 && tokens.get(tokens.size() - 1).getTokenType() == TokenType.ELSE) {
                index++;
            }

        }

        if (statement.getExpressions().get(index) == null)

            if (!displayed)
                execute(statement.getStmt().get(index));
    }

    private void runLoop(LoopNode statement) {
        while ((boolean) runExpression(statement.getExpression()))
            execute(statement.getStatement());
    }

    private Object runExpression(ExpressionNode expression) {
        if (expression instanceof BinNode)
            return checkBinaryExpression((BinNode) expression);
        else if (expression instanceof UnaryNode)
            return checkUnaryExpression((UnaryNode) expression);
        else if (expression instanceof ParenthesisNode)
            return runExpression(((ParenthesisNode) expression).getExpression());
        else if (expression instanceof IdentifierNode)
            return checkIdentifierExpression((IdentifierNode) expression);
        else if (expression instanceof LiteralNode)
            return ((LiteralNode) expression).getLiteral();
        else
            throw new RuntimeException("Unknown expression.");
    }

    private Object checkBinaryExpression(BinNode expression) {
        Object left = runExpression(expression.getLeftHandSide());
        Object right = runExpression(expression.getRightHandSide());
        Object binResult;

        switch (expression.getTokenOperator().getTokenType()) {
            case PLUS:
                binResult = (int) left + (int) right;
                return binResult;
            case MINUS:
                binResult = (int) left - (int) right;
                return binResult;
            case STAR:
                binResult = (int) left * (int) right;
                return binResult;
            case SLASH:
                binResult = (int) left / (int) right;
                return binResult;
            case MODULO:
                binResult = (int) left % (int) right;
                return binResult;
            case LESSTHAN:
                binResult = (int) left < (int) right;
                return binResult;
            case GREATERTHAN:
                binResult = (int) left > (int) right;
                return binResult;
            case LESSEQUAL:
                binResult = (int) left <= (int) right;
                return binResult;
            case GREATEREQUAL:
                binResult = (int) left >= (int) right;
                return binResult;
            case EQUALTO:
                binResult = (int) left == (int) right;
                return binResult;
            case NOTEQUAL:
                binResult = (int) left != (int) right;
                return binResult;
            case AND:
                binResult = (boolean) left && (boolean) right;
                return binResult;
            case OR:
                binResult = (boolean) left || (boolean) right;
                return binResult;
            default:
                throw new RuntimeException("Unknown operator.");
        }
    }

    private Object checkUnaryExpression(UnaryNode expression) {
        Object unaryValue = runExpression(expression.getExpression());
        if (expression.getTokenOperator().getTokenType() == TokenType.MINUS)
            return -(int) unaryValue;
        else if (expression.getTokenOperator().getTokenType() == TokenType.NOT)
            return !((String) unaryValue).contains("TRUE") ? true : false;
        else
            return unaryValue;
    }

    private Object checkIdentifierExpression(IdentifierNode expression) {
        if (variableTable.getValue(expression.getName()) == null)
            throw new RuntimeException("Variable '" + expression.getName() + "' is null.");

        Object result = variableTable.getValue(expression.getName());

        if (result instanceof Boolean)
            return ((boolean) result) ? "TRUE" : "FALSE";
        return result;
    }
}
