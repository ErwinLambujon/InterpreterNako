package Analysis.ParseTree.Statement;
//OK NA
import Analysis.SyntaxAnalyzer.Token;
import java.util.List;

public class ScanNode extends StatementNode {
    private final Token scanToken;
    /*  This uses List type so that
        we can do multiple scanning.
        Example below:
            SCAN: x, y
     */
    private final List<String> identifiers;

    public ScanNode(Token scanToken, List<String> identifiers) {
        this.scanToken = scanToken;
        this.identifiers = identifiers;
    }

    public Token getScanToken() {
        return scanToken;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }
}
