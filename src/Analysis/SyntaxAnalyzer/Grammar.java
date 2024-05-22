package Analysis.SyntaxAnalyzer;
//OK NA
/*
11 keywords
4 data types
 */
import Analysis.TokenDataTypes.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

    public static Token getWordToken(String input) throws Exception{
        /*
            Here ang gihimo kay gi sud ra sa map ang each token example ang
            "BEGIN" kay gi butang sa BEGIN TokenType
            The same goes sa dataTypes ang "INT" kay gi butang sa INT TokenType
         */
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
        dataTypes.put("INT", TokenType.INT);
        dataTypes.put("FLOAT", TokenType.FLOAT);
        dataTypes.put("CHAR", TokenType.CHAR);
        dataTypes.put("BOOL", TokenType.BOOL);

        /*
            Here it will check if the input exist as a key in the Map.
            For example, input is "int" then it proceeds to check if it
            exists as a key in the map. In this case it will return the
            TokenType ERROR since there is no "int" in the map because in
            the Map it is "INT". Basically "int" is not a token.
            The purpose of having the uppercase so that case sensitivity is
            not an issue.
         */
        String inputToUpperCase = input.toUpperCase();
        if (keywords.containsKey(inputToUpperCase)) {
            if (keywords.containsKey(input))
                return new Token(keywords.get(input), input, null);
            else
                throw new Exception(" '" + input + "' is not a valid token for Keyword");
        } else if (dataTypes.containsKey(inputToUpperCase)) {
            if (dataTypes.containsKey(input))
                return new Token(dataTypes.get(input), input, null);
            else
                throw new Exception(" '" + input + "' is not a valid token for Data Type.");
        } else
            return new Token(TokenType.IDENTIFIER, input, null);
    }
    /*
        This method determines the precedence of difference operations.
        The return value is the precedence number. Cases without explicit
        return statements are grouped together with cases that share the
        same return value and are terminated by a break statement or the
        end of the switch statement. Example is PLUS and MINUS they both
        have the same return value of 5. This is how switch case is designed
        in java.
     */
    public static int getBinaryPrecedence(TokenType tokenType) {
        switch (tokenType) {
            case OR:
                return 1;
            case AND:
                return 2;
            case LESSTHAN:
            case LESSEQUAL:
            case GREATERTHAN:
            case GREATEREQUAL:
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

    /*
        This method will return the Data Type depending on
        the tokenType given.
        Example below:
        Token intToken = new Token(TokenType.INTLITERAL, "123", null);
        DataType dataType = getDataType(intToken);
        This will return DataType.Int since the token type is INTLITERAL.
     */
    public static DataType getDataType(Token token) {
        TokenType tokenType = token.getTokenType();
        switch (tokenType) {
            case INT:
            case INTLITERAL:
                return DataType.Int;
            case FLOAT:
            case FLOATLITERAL:
                return DataType.Float;
            case CHAR:
            case CHARLITERAL:
                return DataType.Char;
            case BOOL:
            case BOOLLITERAL:
                return DataType.Bool;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }
    }
    /*
        The difference here to the above method is that this does not
        extract the TokenType from a Token object since its parameter
        is the TokenType class itself.
        Example below:
        DataType dataType = getDataType(TokenType.FLOAT);
     */
    public static DataType getDataType(TokenType tokenType) {
        switch (tokenType) {
            case INT:
            case INTLITERAL:
                return DataType.Int;
            case FLOAT:
            case FLOATLITERAL:
                return DataType.Float;
            case CHAR:
            case CHARLITERAL:
                return DataType.Char;
            case BOOL:
            case BOOLLITERAL:
                return DataType.Bool;
            default:
                throw new IllegalArgumentException("Unknown data type");
        }
    }
    /*
        Example below:
        DataType dataType = getDataType(123);
        This will return DataType.Int since the input value is an Integer.
     */
    public static DataType getDataType(Object val) {
        if (val instanceof Integer)
            return DataType.Int;
        else if (val instanceof Double)
            return DataType.Float;
        else if (val instanceof Character)
            return DataType.Char;
        else if (val instanceof Boolean)
            return DataType.Bool;
        else
            return DataType.String;
    }

    /*
        These methods provide a convenient way to determine
        whether a given token type represents an arithmetic
        operator or a comparison operator. They encapsulate
        the logic for checking the type of the token and
        return a boolean value indicating whether the token
        type matches the specified category of operators.
     */
    public static boolean isArithmeticOperator(TokenType tokenType) {
        return tokenType == TokenType.PLUS || tokenType == TokenType.MINUS ||
               tokenType == TokenType.STAR || tokenType == TokenType.SLASH ||
               tokenType == TokenType.MODULO;
    }

    public static boolean isComparisonOperator(TokenType tokenType) {
        return tokenType == TokenType.LESSTHAN || tokenType == TokenType.GREATERTHAN ||
               tokenType == TokenType.LESSEQUAL || tokenType == TokenType.GREATEREQUAL ||
               tokenType == TokenType.EQUALTO || tokenType == TokenType.NOTEQUAL;
    }

    public static Object convertValue(String val) {
        /*
            The first 4 lines here are regular expressions
            representing INT, FLOAT, CHAR, and BOOL.
         */
        String floatPattern = "^(?:\\+|\\-)?\\d*\\.\\d+$";
        String intPattern = "^(?:\\+|\\-)?\\d+$";
        String charPattern = "^'(?:\\[[\\[\\]\\&\\$\\#']\\])'|'[\\[\\]\\&\\$\\#']'$";
        String boolPattern = "^\"TRUE\"$|^\"FALSE\"$";

        /*
            Here it is compiled using the compile function in java.
         */
        Pattern floatPatternRegex = Pattern.compile(floatPattern);
        Pattern intPatternRegex = Pattern.compile(intPattern);
        Pattern charPatternRegex = Pattern.compile(charPattern);
        Pattern boolPatternRegex = Pattern.compile(boolPattern);

        /*
            Here you used the matcher function to match it in the next
            line using the matches function in java. Here its like you
            compare the variable val and the regular expression for each
            data type.
         */
        Matcher floatMatcher = floatPatternRegex.matcher(val);
        Matcher intMatcher = intPatternRegex.matcher(val);
        Matcher charMatcher = charPatternRegex.matcher(val);
        Matcher boolMatcher = boolPatternRegex.matcher(val);

        /*
            Here you manually check if the matcher matches then return
            the parsed value of the string.
         */
        if (intMatcher.matches())
            return Integer.parseInt(val);
        else if (floatMatcher.matches())
            return Double.parseDouble(val);
        else if (charMatcher.matches())
            return val;
        else if (boolMatcher.matches())
            return val.equals("\"TRUE\"") ? true : false;
        else
            throw new IllegalArgumentException("Invalid input " + val);
    }
    /*
        This method is used to handle cases where implicit conversion between
        float and int are allowed. For example, in some contexts, a float value
        can be assigned to an int variable, with the fractional part of the float
        being truncated. This is known as narrowing conversion.
     */
    public static boolean matchDataType(DataType ldt, DataType rdt) {
        return (ldt == DataType.Float && rdt == DataType.Int) || ldt == rdt;
    }
}
