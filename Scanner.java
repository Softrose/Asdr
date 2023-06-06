import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("FOR", TipoToken.FOR);
        palabrasReservadas.put("WHILE", TipoToken.WHILE);
        palabrasReservadas.put("IF", TipoToken.IF);
        palabrasReservadas.put("RETURN", TipoToken.RETURN);
        palabrasReservadas.put("SUPER", TipoToken.SUPER);
        palabrasReservadas.put("FUN", TipoToken.FUN);
        palabrasReservadas.put("VAR", TipoToken.VAR);
        palabrasReservadas.put("CLASS", TipoToken.CLASS);
        palabrasReservadas.put("TRUE", TipoToken.TRUE);
        palabrasReservadas.put("FALSE", TipoToken.FALSE);
        palabrasReservadas.put("THIS", TipoToken.THIS);
        palabrasReservadas.put("NULL", TipoToken.NULL);
        palabrasReservadas.put("PRINT", TipoToken.PRINT);
        palabrasReservadas.put("ELSE", TipoToken.ELSE);
        palabrasReservadas.put("AND", TipoToken.AND);
        palabrasReservadas.put("OR", TipoToken.OR);
        palabrasReservadas.put("CALL", TipoToken.CALL);
    }

    Scanner(String source){
        this.source = source + " ";
    }

    List<Token> scanTokens(){
        int estado = 0;
        char caracter = 0;
        String lexema = "";
        int inicioLexema = 0;

        for(int i=0; i<source.length(); i++){
            caracter = source.charAt(i);

            switch (estado){
                case 0:
                    if(caracter == '('){
                        tokens.add(new Token(TipoToken.Open_parent, "(", i + 1));
                    }
                    else if(caracter == ')'){
                        tokens.add(new Token(TipoToken.Close_parent, ")", i + 1));
                    }
                    else if(caracter == '{'){
                        tokens.add(new Token(TipoToken.Open_curly, "{", i + 1));
                    }
                    else if(caracter == '}'){
                        tokens.add(new Token(TipoToken.Close_curly, "}", i + 1));
                    }
                    else if(caracter == ','){
                        tokens.add(new Token(TipoToken.Comma, ",", i + 1));
                    }
                    else if(caracter == '.'){
                        tokens.add(new Token(TipoToken.Dot, ".", i + 1));
                    }
                    else if(caracter == ';'){
                        tokens.add(new Token(TipoToken.Dot_comma, ";", i + 1));
                    }
                    else if(caracter == '-'){
                        tokens.add(new Token(TipoToken.Hyphen, "-", i + 1));
                    }
                    else if(caracter == '+'){
                        tokens.add(new Token(TipoToken.Plus, "+", i + 1));
                    }
                    if (caracter == '/') {
                        if (source.charAt(i + 1) == '/') {
                            i = source.indexOf('\n', i) - 1;
                            break;
                        }
                        else if (source.charAt(i + 1) == '*') {
                            i = source.indexOf("*/", i) + 1;
                            break;
                        }else{
                            tokens.add(new Token(TipoToken.Slash, "/", i + 1));
                        }
                    }
                    else if(caracter == '*'){
                        tokens.add(new Token(TipoToken.Star, "*", i + 1));
                    }
                    else if(caracter == '!'){
                        if (source.charAt(i+ 1) == '=') {
                            tokens.add(new Token(TipoToken.Exclamation_equal, "!=", i + 1));
                            i++;
                        }else{
                            tokens.add(new Token(TipoToken.ExclamationMark, "!", i + 1));
                        }
                    }
                    else if(caracter == '='){
                        if(source.charAt(i+ 1) == '='){
                            tokens.add(new Token(TipoToken.Equal_equal, "==", i + 1));
                            i++;
                        }else{
                            tokens.add(new Token(TipoToken.Equal, "=", i + 1));
                        }
                    }
                    else if(caracter == '<'){
                        if(source.charAt(i+ 1) == '='){
                            tokens.add(new Token(TipoToken.Less_equal, "<=", i + 1));
                            i++;
                        }else{
                            tokens.add(new Token(TipoToken.LessThan, "<", i + 1));
                        }
                    }
                    else if(caracter == '>'){
                        if(source.charAt(i+ 1) == '='){
                            tokens.add(new Token(TipoToken.Greather_equal, ">=", i + 1));
                            i++;
                        }else{
                            tokens.add(new Token(TipoToken.LessThan, ">", i + 1));
                        }
                    }
                    else if(Character.isAlphabetic(caracter)){
                        estado = 1;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if (Character.isDigit(caracter)) {
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if (caracter == '"') {
                        estado = 3;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    break;

                case 1:
                    if(Character.isAlphabetic(caracter) || Character.isDigit(caracter) ){
                        lexema = lexema + caracter;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, inicioLexema + 1));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
                case 2:
                    if (Character.isDigit(caracter)) {
                        lexema = lexema + caracter;
                    } else {
                        tokens.add(new Token(TipoToken.NUMBER, lexema, inicioLexema + 1));
                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
                case 3:
                    if (caracter != '"') {
                        lexema = lexema + caracter;
                    } else {
                        lexema = lexema + caracter;
                        tokens.add(new Token(TipoToken.STRING, lexema, inicioLexema + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "", source.length()));

        return tokens;
    }

}
