package Analysis.SymbolTable;

import Analysis.TokenDataTypes.DataType;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class varTable {
    private Map<String, Map.Entry<DataType, Object>> varTable;

    public varTable(){
        varTable = new HashMap<>();
    }

    public void addVariable(String identifier, DataType dataType, Object val){
        Object valueToStore = val != null ? val : new Object();

        varTable.put(identifier, Map.entry(dataType, valueToStore));
    }

    public void addIdentifier(String identifier, DataType dataType, Object val){
        if(this.varTable == null){
            this.varTable = new HashMap<>();
        }

        if(this.varTable.containsKey(identifier)){
            throw new IllegalArgumentException("Identifier already declared");
        }

        Object placeholder = new Object();

        varTable.put(identifier, Map.entry(dataType, placeholder));
    }

    public void addValue(String identifier, Object val){
        Map.Entry<DataType,Object> entry = varTable.get(identifier);

        if(entry != null){
            varTable.put(identifier, Map.entry(entry.getKey(), val));
        }
    }

    public DataType getType(String identifier){
        Map.Entry<DataType, Object> entry = varTable.get(identifier);
        return entry != null ? entry.getKey() : null;
    }

    public Object getValue(String identifier){
        Map.Entry<DataType, Object> entry = varTable.get(identifier);
        return entry != null ? entry.getValue() : null;
    }

    public boolean exist(String identifier){
        return varTable.containsKey(identifier);
    }
}
