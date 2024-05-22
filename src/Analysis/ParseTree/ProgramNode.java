package Analysis.ParseTree;
//OK NA
import Analysis.ParseTree.Statement.StatementNode;
import java.util.List;

public class ProgramNode extends ASTNode {
    /*
        Since statements is of type StatementNode it can be an instance
        of its sub-classes like DisplayNode, LoopNode, etc.
        statements can indeed contain instances of ConditionalNode,
        LoopNode, VariableDeclarationNode, and any other subclass of
        StatementNode that represents a valid statement in the language
        being parsed.
     */
    private final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }
}
