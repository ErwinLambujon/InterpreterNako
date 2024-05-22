package Analysis.SyntaxAnalyzer;

import Analysis.TokenDataTypes.TokenType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String code;
    private int position; // Current position in the source code

    public Lexer(String code) {
        this.code = code;
        this.position = 0;
    }

    private char current() {
        return peek(0);
    } // Returns the current character

    private char lookAhead() {
        return peek(1);
    } // Returns the next character

    private char peek(int offset) {
        int index = position + offset;
        if (index >= code.length())
            return '\0';   // Returns null character if end of code is reached
        return code.charAt(index); // Returns the character at the current position + offset
    }

    private void next(int offset) {
        position += offset;
    }

    private void newLine() {
        next(1);   // Moves the position forward by 1
    }

    public Token getToken() throws Exception {
        // Main method to get the next token from the source code
        while (position < code.length()) {
            // Handles different types of tokens based on the current character
            if (Character.isLetter(current()))
                return getKeywordOrDataTypeOrIdentifierToken();

            if (Character.isDigit(current()))
                return getNumberLiteralToken();

            switch (current()) {
                case ' ':
                case '\t':
                    next(1);
                    continue;
                case '\n':
                    Token newLineToken = new Token(TokenType.NEWLINE, "\n", null);
                    newLine();
                    return newLineToken;
                case '_':
                    return getKeywordOrDataTypeOrIdentifierToken();
                case '\'':
                    return getCharacterLiteralToken();
                case '\"':
                    return getBooleanOrStringLiteralToken();
                case '.':
                    return getNumberLiteralToken();
                case '#':
                    while (current() != '\n' && current() != '\0')
                        next(1);
                    continue;
                case '*':
                    next(1);
                    return new Token(TokenType.STAR, "*", null);
                case '/':
                    next(1);
                    return new Token(TokenType.SLASH, "/", null);
                case '%':
                    next(1);
                    return new Token(TokenType.MODULO, "%", null);
                case '+':
                    next(1);
                    return new Token(TokenType.PLUS, "+", null);
                case '-':
                    next(1);
                    return new Token(TokenType.MINUS, "-", null);
                case '>':
                    if (lookAhead() == '=') {
                        next(2);
                        return new Token(TokenType.GREATEREQUAL, ">=", null);
                    }
                    next(1);
                    return new Token(TokenType.GREATERTHAN, ">", null);
                case '<':
                    if (lookAhead() == '=') {
                        next(2);
                        return new Token(TokenType.LESSEQUAL, "<=", null);
                    } else if (lookAhead() == '>') {
                        next(2);
                        return new Token(TokenType.NOTEQUAL, "<>", null);
                    }
                    next(1);
                    return new Token(TokenType.LESSTHAN, "<", null);
                case '=':
                    if (lookAhead() == '=') {
                        next(2);
                        return new Token(TokenType.EQUALTO, "==", null);
                    }
                    next(1);
                    return new Token(TokenType.EQUAL, "=", null);
                case '$':
                    next(1);
                    return new Token(TokenType.DOLLAR, "$", null);
                case '&':
                    next(1);
                    return new Token(TokenType.AMPERSAND, "&", null);
                case '[':
                    return getEscapeCodeToken();
                case '(':
                    next(1);
                    return new Token(TokenType.OPENPARENTHESIS, "(", null);
                case ')':
                    next(1);
                    return new Token(TokenType.CLOSEPARENTHESIS, ")", null);
                case ',':
                    next(1);
                    return new Token(TokenType.COMMA, ",", null);
                case ':':
                    next(1);
                    return new Token(TokenType.COLON, ":", null);
                default:
                    next(1);
                    return new Token(TokenType.ERROR, Character.toString(current()), "Unknown symbol");
            }
        }
        return new Token(TokenType.ENDOFFILE, "\0", null);
    }
    
    private Token getKeywordOrDataTypeOrIdentifierToken() throws Exception {
        // Handles identifiers, keywords, and data types
        int start = position;

        while (Character.isLetter(current()) || current() == '_' || Character.isDigit(current()))
            next(1);
    
        int length = position - start;
        String text = code.substring(start, start + length);
    
        return Grammar.getWordToken(text);
    }
    
    private Token getCharacterLiteralToken() {
        // Handles character literals
        int start = position;

        next(1);
        while (current() != '\'' && !Character.isWhitespace(lookAhead()))
            next(1);
        next(1);
    
        String charPattern = "^'(?:\\[\\[\\]\\&\\$#']\\])'|'[^\\[\\]\\&\\$#']'$";
        Pattern charRegex = Pattern.compile(charPattern);
    
        int length = position - start;
        String text = code.substring(start, start + length);
        Object value = null;
    
        Matcher matcher = charRegex.matcher(text);
        if (matcher.matches()) {
            value = text.charAt(text.length() / 2);
            return new Token(TokenType.CHARLITERAL, text, value);
        }
        return new Token(TokenType.ERROR, text, "Lexical Error: Invalid CHAR literal '" + text + "'.");
    }
    private Token getBooleanOrStringLiteralToken() {
        // Handles boolean and string literals
        int start = position;

        next(1);
        while (current() != '\"' && !Character.isWhitespace(lookAhead()))
            next(1);
        next(1);
    
        String boolPattern = "^\"TRUE\"$|^\"FALSE\"$";
        String stringPattern = "^\"[^\"]*\"$";
        Pattern boolRegex = Pattern.compile(boolPattern);
        Pattern stringRegex = Pattern.compile(stringPattern);
    
        int length = position - start;
        String text = code.substring(start, start + length);
    
        Matcher boolMatcher = boolRegex.matcher(text);
        Matcher stringMatcher = stringRegex.matcher(text);
    
        if (boolMatcher.matches())
            return new Token(TokenType.BOOLLITERAL, text, text.equals("\"TRUE\""));
        else if (stringMatcher.matches())
            return new Token(TokenType.STRINGLITERAL, text, text.substring(1, text.length() - 1));
        else {
            String errorMessage = text.contains("TRUE") || text.contains("FALSE") ? "Lexical Error: Invalid BOOL literal '" + text + "'" : "Lexical Error: Invalid STRING literal '" + text + "'";
            return new Token(TokenType.ERROR, text, errorMessage);
        }
    }
      
    private Token getNumberLiteralToken() {
        // Handles number literals (integer and float)
        boolean isFloat = current() == '.';
    
        int start = position;

        while (Character.isDigit(current()) || current() == '.')
            next(1);
    
        int length = position - start;
        String text = code.substring(start, start + length);
    
        String floatPattern = "\\d*\\.\\d+";
        String intPattern = "\\d+";
        Pattern floatRegex = Pattern.compile(floatPattern);
        Pattern intRegex = Pattern.compile(intPattern);
    
        Object val = null;
    
        Matcher floatMatcher = floatRegex.matcher(text);
        Matcher intMatcher = intRegex.matcher(text);
    
        if (intMatcher.matches()) {
            val = Integer.parseInt(text);
            return new Token(TokenType.INTLITERAL, text, val);
        } else if (floatMatcher.matches()) {
            val = Float.parseFloat(text);
            return new Token(TokenType.FLOATLITERAL, text, val);
        }
        return new Token(TokenType.ERROR, text, "Lexical Error: Invalid number literal '" + text + "'.");
    }
    private Token getEscapeCodeToken() {
        // Handles escape codes
        int start = position;

        while (!Character.isWhitespace(current()))
            next(1);

        int length = position - start;
        String text = code.substring(start, start + length);
        Object val = null;

        String escapeSequencePattern = "^\\[[\\]\\[\\&\\$\\#]\\]$";
//        String escapeSequencePattern = "\\[\\[\\]\\&\\$#\\]\\]";
        Pattern escapeRegex = Pattern.compile(escapeSequencePattern);

        Matcher matcher = escapeRegex.matcher(text);
        if (matcher.matches()) {
            val = text.charAt(1);
            return new Token(TokenType.ESCAPE, text, val);
        }
        return new Token(TokenType.ERROR, text, "Lexical Error: Invalid escape sequence '" + text + "'.");
    }
}
