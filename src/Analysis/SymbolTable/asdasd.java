package Analysis.SymbolTable;

import Analysis.SyntaxAnalyzer.Token;
import Analysis.TokenDataTypes.DataType;
import Analysis.TokenDataTypes.TokenType;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class asdasd {
    public static Token getWordToken(String input) throws  Exception{

        Map<String, TokenType> keywords = new HashMap<>();
        keywords.put("BEGIN", TokenType.BEGIN);
        keywords.put("END", TokenType.END);
        keywords.put("CODE", TokenType.CODE);
        keywords.put("IF", TokenType.IF);
        keywords.put("ELSE", TokenType.ELSE);
        keywords.put("WHILE", TokenType.WHILE);
        keywords.put("DISPLAY", TokenType.DISPLAY);
        keywords.put("SCAN", TokenType.SCAN);
        keywords.put("AND", TokenType.AND);
        keywords.put("OR", TokenType.OR);
        keywords.put("NOT", TokenType.NOT);

        Map<String, TokenType> dataTypes = new HashMap<>();
        dataTypes.put("FLOAT", TokenType.FLOAT);
        dataTypes.put("INT", TokenType.INT);
        dataTypes.put("CHAR", TokenType.CHAR);
        dataTypes.put("BOOL", TokenType.BOOL);

        String inputToUpperCase = input.toUpperCase();
        if(keywords.containsKey(inputToUpperCase)){
            if(keywords.containsKey(input)){
                return new Token(keywords.get(input), input, null);
            }else{
                throw new Exception("It is not a valid keyword");
            }
        }else if(dataTypes.containsKey(inputToUpperCase)){
            if(dataTypes.containsKey(input)){
                return new Token(dataTypes.get(input), input, null);
            }else{
                throw new Exception("It is not a valid data type");
            }
        }else{
            return new Token(TokenType.IDENTIFIER, input, null);
        }
    }

    public static int getBinaryPrecedence(TokenType tokenType){
        switch (tokenType){
            case OR:
                return 1;
            case AND:
                return 2;
            case GREATEREQUAL:
            case GREATERTHAN:
            case LESSEQUAL:
            case LESSTHAN:
            case EQUALTO:
            case NOTEQUAL:
                return 4;
            case PLUS:
            case MINUS:
                return 5;
            case MODULO:
                return 6;
            case STAR:
            case SLASH:
                return 7;
            default:
                return 0;
        }
    }

    public static DataType getDataType(Token token){
        TokenType tokenType = token.getTokenType();
        switch (tokenType){
            case INT:
            case INTLITERAL:
                return DataType.Int;
            case FLOAT:
            case FLOATLITERAL:
                return DataType.Float;
            case BOOL:
            case BOOLLITERAL:
                return DataType.Bool;
            case CHAR:
            case CHARLITERAL:
                return DataType.Char;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }
    }

    public static DataType getDataType(TokenType tokenType){
        switch (tokenType){
            case INT:
            case INTLITERAL:
                return DataType.Int;
            case FLOAT:
            case FLOATLITERAL:
                return DataType.Float;
            case BOOL:
            case BOOLLITERAL:
                return DataType.Bool;
            case CHAR:
            case CHARLITERAL:
                return DataType.Char;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }
    }

    public static DataType getDataType(Object val){
        if(val instanceof Integer){
            return DataType.Int;
        }else if(val instanceof Float){
            return DataType.Float;
        }else if(val instanceof Boolean){
            return DataType.Bool;
        }else if(val instanceof Character){
            return DataType.Char;
        }else{
            return DataType.String;
        }
    }

    public static boolean isArithmeticOperator(TokenType tokenType){
        return tokenType == TokenType.PLUS || tokenType == TokenType.MINUS ||
                tokenType == TokenType.STAR || tokenType == TokenType.SLASH ||
                tokenType == TokenType.MODULO;
    }

    public static boolean isComparisonOperator(TokenType tokenType){
        return tokenType == TokenType.EQUALTO || tokenType == TokenType.NOTEQUAL ||
                tokenType == TokenType.GREATERTHAN || tokenType == TokenType.GREATEREQUAL ||
                tokenType == TokenType.LESSTHAN || tokenType == TokenType.LESSEQUAL;
    }

    public static Object convertValue(String val){
        String floatPattern = "^(?:\\+|\\-)?\\d*\\.\\d+$";
        String intPattern = "^(?:\\+|\\-)?\\d+$";
        String charPattern = "^'(?:\\[[\\[\\]\\&\\$\\#']\\])'|'[\\[\\]\\&\\$\\#']'$";
        String boolPattern = "^\"TRUE\"$|^\"FALSE\"$";

        Pattern floatPatterRegex = Pattern.compile(floatPattern);
        Pattern intPatterRegex = Pattern.compile(intPattern);
        Pattern charPatterRegex = Pattern.compile(charPattern);
        Pattern boolPatterRegex = Pattern.compile(boolPattern);

        Matcher floatPatterMatcher = floatPatterRegex.matcher(val);
        Matcher intPatterMatcher = intPatterRegex.matcher(val);
        Matcher charPatterMatcher = charPatterRegex.matcher(val);
        Matcher boolPatterMatcher = boolPatterRegex.matcher(val);

        if(floatPatterMatcher.matches()){
            return Float.parseFloat(val);
        }else if(intPatterMatcher.matches()){
            return Integer.parseInt(val);
        }else if(charPatterMatcher.matches()){
            return val;
        }else if (boolPatterMatcher.matches()){
            return val.equals("\"TRUE\"") ? true : false;
        }else{
            throw new IllegalArgumentException("Not a valid conversion");
        }
    }

    public static boolean matchDataType(DataType ldt, DataType rdt){
        return (ldt == DataType.Float && rdt == DataType.Int) || ldt == rdt;
    }
}
