package Analysis.SyntaxAnalyzer;

import Analysis.ParseTree.*;
import Analysis.ParseTree.Expression.ExpressionNode;
import Analysis.ParseTree.Expression.IdentifierNode;
import Analysis.ParseTree.Expression.LiteralNode;
import Analysis.ParseTree.Expression.ParenthesisNode;
import Analysis.ParseTree.Expression.UnaryNode;
import Analysis.ParseTree.Expression.BinNode;
import Analysis.SymbolTable.VariableTable;
import Analysis.ParseTree.Statement.*;
import Analysis.TokenDataTypes.*;
import java.util.List;

public class Semantic {
    private VariableTable variableTable;

    public Semantic() {
        variableTable = new VariableTable();
    }

    /*
        This method checks what type of statement it is like
        if it is of type LoopNode, etc.
     */
    public void analyze(ProgramNode program) {
        /*
            The List is getting what type of StatementNode is the program.
         */
        List<StatementNode> statements = program.getStatements();
        for (StatementNode statement : statements) {
            if (statement instanceof VariableDeclarationNode) {
                analyzeVariableDeclaration((VariableDeclarationNode) statement);
            } else if (statement instanceof AssignmentNode) {
                analyzeAssignment((AssignmentNode) statement);
            } else if (statement instanceof DisplayNode) {
                analyzeDisplay((DisplayNode) statement);
            } else if (statement instanceof ScanNode) {
                analyzeScan((ScanNode) statement);
            } else if (statement instanceof ConditionalNode) {
                analyzeCondition((ConditionalNode) statement);
            } else if (statement instanceof LoopNode) {
                analyzeLoop((LoopNode) statement);
            }
        }
    }

    private void analyzeVariableDeclaration(VariableDeclarationNode statement) {
        /*
            This method 'statement.getDataTypeToken():' call on the VariableDeclarationNode
            object statement retrieves the Token object representing the data type of the
            variables being declared.
            'getTokenType():' This method call on the Token object retrieves the TokenType
            enum value which indicates the specific data type (such as INT, FLOAT, CHAR, etc.).
         */
        DataType dataType = Grammar.getDataType(statement.getDataTypeToken().getTokenType());
        /*
            This iterates over the set of variable identifiers (names) declared in the statement.
            keySet() returns the set of variable names.
         */
        for (String identifier : statement.getVariables().keySet()) {
            /*
                For each variable identifier, the method checks if it already exists in the
                variableTable (which presumably keeps track of all declared variables).
                Basically it will iterate through each variable in the declaration and check
                if it exists already or the data type does not match to the declared data type.

             */
            if (!variableTable.exists(identifier)) {
                ExpressionNode value = statement.getVariables().get(identifier);
                //Here it checks if the variable is initialized or not.
                if (value != null) {
                    /*
                        It calls analyzeExpression(value) to determine the data type of the expression.
                        Analyzes the type of the expression being assigned to the variable using analyzeExpression().
                        This method determines the data type of the right-hand side expression of the assignment.
                        Example: x = 5; statement.getExpression() retrieves the right-hand side of the assignment
                        (5 in this example). The method analyzeExpression is then called to determine the data type
                        of this expression.
                     */
                    DataType expressionType = analyzeExpression(value);
                    /*
                        Here it compares if the data type of the expression matches the declared data type
                        of the variable.
                     */
                    if (!Grammar.matchDataType(dataType, expressionType)) {
                        throw new RuntimeException("The Data Type of the variable does not match.");
                    }
                }
                //Here it added the variable to the variableTable if it passes all checking.
                variableTable.addIdentifier(identifier, dataType);
            } else {
                throw new RuntimeException("Variable is already declared.");
            }
        }
    }

    private void analyzeAssignment(AssignmentNode statement) {
        /*
            Iterates over each variable identifier in the assignment statement.
            Since statement is of AssignmentNode it can use the getIdentifiers()
            and use the size function to iterate until its size.
         */
        for (int i = 0; i < statement.getIdentifiers().size(); i++) {
            //Retrieves the variable identifier at the current index 'i'.
            String identifier = statement.getIdentifiers().get(i);
            //Checks if the variable identified by identifier exists in the variableTable.
            if (variableTable.exists(identifier)) {
                //If the variable exists, retrieves its data type from the variableTable.
                DataType dataType = variableTable.getType(identifier);
                /*
                    Analyzes the type of the expression being assigned to the variable using analyzeExpression().
                    This method determines the data type of the right-hand side expression of the assignment.
                    Example: x = 5; statement.getExpression() retrieves the right-hand side of the assignment (5 in this example).
                    The method analyzeExpression is then called to determine the data type of this expression.
                 */
                DataType expressionType = analyzeExpression(statement.getExpression());
                /*
                    Checks if the data type of the expression (expressionType) matches the data type
                    of the variable (dataType) using Grammar.matchDataType().
                 */
                if (!Grammar.matchDataType(dataType, expressionType)) {
                    throw new RuntimeException(String.format("Cannot assign %s to \"%s\".",
                            expressionType, identifier));
                }
            } else {
                throw new RuntimeException(String.format("Variable \"%s\" does not exist.", identifier));
            }
        }
    }

    private void analyzeDisplay(DisplayNode statement) {
        /*
            We use for each for iteration purposes so that we can do multiple DISPLAY.
            Example: DISPLAY: a & b & c
         */
        for (ExpressionNode expression : statement.getExpressions()) {
            if (expression instanceof IdentifierNode) {
                /*
                    Here you typecast it to IdentifierNode if it is its instance.
                    This is logical since when you use DISPLAY you used the variable name
                    which is an identifier that is why you check if it is an instance of it.
                 */
                IdentifierNode identifierNode = (IdentifierNode) expression;
                if (!variableTable.exists(identifierNode.getName())) {
                    throw new RuntimeException(String.format("Variable \"%s\" does not exist.", identifierNode.getName()));
                }
            }
        }
    }

    private void analyzeScan(ScanNode statement) {
        /*
            We use for each so that we can do multiple SCAN.
            Example: SCAN: a & b
         */
        for (String identifier : statement.getIdentifiers()) {
            //Check if the identifer/variable exist in the variableTable.
            if (!variableTable.exists(identifier)) {
                throw new RuntimeException(String.format("Variable \"%s\" does not exist.", identifier));
            }
        }
    }

    private void analyzeCondition(ConditionalNode statement) {
    int index = 0;
    /*
        Iterates over a list of expressions obtained from the ConditionalNode instance.
        These expressions correspond to the conditions in the conditional statement.
     */
    for (ExpressionNode expression : statement.getExpressions()) {
        /*
                Verifies whether the current expression is not null, which indicates
                that the current statement is either an if or an else if statement.
                This check ensures that the else block won't be analyzed for a null expression.
                The else block doesn't have a condition associated with it, so there's no expression
                to evaluate. Therefore, the expression being null signifies the presence of the else block.
             */
        if (expression != null) {
            if (analyzeExpression(expression) != DataType.Bool) {
                throw new RuntimeException("Incorrect Data Type. Data Type should be Bool.");
            }
        }

        /*
            Calls the analyzed method to analyze the statement block corresponding to the
            current expression. This could be the body of an if, else if, or else block,
            depending on the iteration. Each index is one analyzation for example,
            at index = 0 the if block is analyzed and in index = 1 is the else if bock.
        */
        analyze(statement.getStmt().get(index));

        //Increments the index to proceed to the next expression and its corresponding statement block.
        index++;
    }
}
    private void analyzeLoop(LoopNode statement) {
        //Here it get the Data Type of the statement.
        DataType expressionType = analyzeExpression(statement.getExpression());
        if (expressionType != DataType.Bool) {
            throw new RuntimeException("Incorrect Data Type. Data Type should be Bool.");
        }
        /*
            Here you call the analyze() method to execute the statements
            inside the loop.
         */
        analyze(statement.getStatement());
    }

    /*
        This method checks the type of ExpressionNode is the expression
        i.e. BinNode, UnaryNode, etc. Also you typecast it to his expression type.
     */
    private DataType analyzeExpression(ExpressionNode expression) {
        if (expression instanceof BinNode) {
            return analyzeBinaryExpression((BinNode) expression);
        } else if (expression instanceof UnaryNode) {
            return analyzeUnaryExpression((UnaryNode) expression);
        } else if (expression instanceof ParenthesisNode) {
            return analyzeExpression(((ParenthesisNode) expression).getExpression());
        } else if (expression instanceof IdentifierNode) {
            return analyzeIdentifierExpression((IdentifierNode) expression);
        } else if (expression instanceof LiteralNode) {
            return analyzeLiteralExpression((LiteralNode) expression);
        } else {
            throw new RuntimeException("Unknown expression.");
        }
    }

    private DataType analyzeBinaryExpression(BinNode expression) {
        //This get the operator
        Token operatorToken = expression.getTokenOperator();
        //This get the left side expression
        DataType leftDataType = analyzeExpression(expression.getLeftHandSide());
        //This get the right side expression
        DataType rightDataType = analyzeExpression(expression.getRightHandSide());
        //You check if the left side and right side has the same Data Type.
        if (!matchExpressionDataType(leftDataType, rightDataType)) {
            throw new RuntimeException(String.format("Left side expression is of type '%s' while right side expression " +
                    "is '%s'; operator '%s' cannot be applied.", leftDataType, rightDataType, operatorToken.getCode()));
            //left side expression and right side expression has different Data Type therefore operator '%s' cannot be applied.
        }
        /*
            Here you check if the Data Type of left and right side expression is of type Char, Bool, and String.
            Since you cannot apply arithmetic operations to this Data Type.
         */
        if (Grammar.isArithmeticOperator(operatorToken.getTokenType()) &&
                ((leftDataType == DataType.Char || leftDataType == DataType.String || leftDataType == DataType.Bool) &&
                        (rightDataType == DataType.Char || rightDataType == DataType.String || rightDataType == DataType.Bool))) {
            throw new RuntimeException("Cannot apply arithmetic operations to data type 'CHAR', 'BOOL', and 'STRING'.");
        /*
            Here you check if left and right side expression is of the same type.
            Since Comparison Operator can only be applied to the Data Type.
         */
        } else if (Grammar.isComparisonOperator(operatorToken.getTokenType()) &&
                !matchExpressionDataType(leftDataType, rightDataType)) {
            throw new RuntimeException(String.format("Left side expression is of type '%s' while right side expression is '%s'; " +
                    "comparison operator '%s' cannot be applied.", leftDataType, rightDataType, operatorToken.getCode()));
        }
        /*
            If the operator is a comparison operator, the result of the binary expression is a boolean (DataType.Bool).
            Otherwise, the result of the binary expression is the data type of the left-hand side expression (leftDataType).
            It does not matter if we return left or right side expression in the else block since we checked above if they
            match Data Type.
        */
        return Grammar.isComparisonOperator(operatorToken.getTokenType()) ? DataType.Bool : leftDataType;
    }

    private DataType analyzeUnaryExpression(UnaryNode expression) {
        //This will retrieve the token of the operator i.e. PLUS, MINUS, etc.
        Token operatorToken = expression.getTokenOperator();
        //This will retrieve the Data Type of the expression.
        DataType expressionDataType = analyzeExpression(expression.getExpression());
        /*
            It checks if the operator is 'NOT' then it checks if the Data Type is 'BOOL'
            If the data type is Bool, it returns 'DataType.Bool' because applying the 'NOT'
            operator to a boolean expression results in a boolean value.
            Ka gets nako since the 'NOT' here since it will invert the boolean value of it
            like for example the 'true' becomes 'false' and vice versa that is why it should it
            BOOL.
         */
        if (operatorToken.getTokenType() == TokenType.NOT) {
            if (expressionDataType != DataType.Bool) {
                throw new RuntimeException(String.format("Operator '%s' can only be applied to '%s'.",
                        operatorToken.getCode(), expressionDataType));
            }
            return DataType.Bool;
        }
        //Returns the Data Type of the expression if the unary operator is not 'NOT'
        return expressionDataType;
    }

    private DataType analyzeIdentifierExpression(IdentifierNode expression) {
        /*
            Here it check if the Name of the identifier exists in the variableTable
            and if not it will return an exception.
         */
        if (!variableTable.exists(expression.getName())) {
            throw new RuntimeException(String.format("Variable \"%s\" does not exist.", expression.getName()));
        }
        //Here it will return the Data Type of the identifier if it exists in the variableTable.
        return variableTable.getType(expression.getName());
    }

    private DataType analyzeLiteralExpression(LiteralNode expression) {
        //The method retrieves the actual value of the literal from the LiteralNode object.
        Object value = expression.getLiteral();
        /*
            Here it checks on what instance is the value object.
            By using instanceof we can ensure that only supported
            types are processed correctly.
         */
        if (value instanceof Integer) {
            return DataType.Int;
        } else if (value instanceof Float || value instanceof Double) {
            return DataType.Float;
        } else if (value instanceof Character) {
            return DataType.Char;
        } else if (value instanceof Boolean) {
            return DataType.Bool;
        } else if (value instanceof String) {
            return DataType.String;
        } else {
            throw new RuntimeException(String.format("Unknown Data Type %s", expression.getLiteral()));
        }
    }

    private boolean matchExpressionDataType(DataType ldt, DataType rdt) {
        /*
            This line checks if one of the data type is 'DataType.Int' and the other one
            is 'DataType.Float'. This method basically allows operations between integers
            and floats.
         */
        if ((ldt == DataType.Int && rdt == DataType.Float) || (ldt == DataType.Float && rdt == DataType.Int))
            return true;
        /*
            This check if the two expression has the same Data Type since we can only perform operations
            to the same Data Type except 'Float' and 'Int'. If they have the same Data Type then this will
            return true else false.
         */
        return ldt == rdt;
    }
}
