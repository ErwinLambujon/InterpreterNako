package Analysis.SymbolTable;
//OK NA
/*String floatPattern = "^(?:\\+|\\-)?\\d*\\.\\d+$";
        String intPattern = "^(?:\\+|\\-)?\\d+$";
        String charPattern = "^'(?:\\[[\\[\\]\\&\\$\\#']\\])'|'[\\[\\]\\&\\$\\#']'$";
        String boolPattern = "^\"TRUE\"$|^\"FALSE\"$";*/
import Analysis.TokenDataTypes.DataType;
import java.util.HashMap;
import java.util.Map;

public class VariableTable {
    /*
        Object ang gigamit diri so that we can do pass
        by reference or referencing a value. Example is a = b
     */
    private Map<String, Map.Entry<DataType, Object>> variableTable;

    public VariableTable() {
        variableTable = new HashMap<>();
    }

    public void addVariable(String identifier, DataType dataType, Object val) {
        // Ensure val is not null, using a placeholder object if necessary
        // Variables are created as object so that it can hold references to objects of any class.
        Object valueToStore = val != null ? val : new Object(); // Placeholder object

        // Create a Map.Entry directly using Map.entry and add it to the variableTable
        variableTable.put(identifier, Map.entry(dataType, valueToStore));
    }

public void addIdentifier(String identifier, DataType dataType) {
    // Ensure variableTable is initialized, creating it as a HashMap if null
    if (this.variableTable == null) {
        this.variableTable = new HashMap<>();
    }

    // Check for duplicate identifier and throw exception if found
    if (this.variableTable.containsKey(identifier)) {
        throw new IllegalArgumentException("Identifier has been declared already.");
    }

    // Use a placeholder object instead of null
    Object placeholder = new Object();

    // Create a Map.Entry directly using Map.entry and add it to the variableTable
    this.variableTable.put(identifier, Map.entry(dataType, placeholder));
}


    public void addValue(String identifier, Object val) {
        Map.Entry<DataType, Object> entry = variableTable.get(identifier);
        if (entry != null) {
            variableTable.put(identifier, Map.entry(entry.getKey(), val));
        }
    }

    public DataType getType(String identifier) {
        Map.Entry<DataType, Object> entry = variableTable.get(identifier);
        return entry != null ? entry.getKey() : null;
    }

    public Object getValue(String identifier) {
        Map.Entry<DataType, Object> entry = variableTable.get(identifier);
        return entry != null ? entry.getValue() : null;
    }

    public boolean exists(String identifier) {
        return variableTable.containsKey(identifier);
    }
}