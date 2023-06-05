import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token IDENTIFICADOR = new Token(TipoToken.IDENTIFICADOR, "");
    private final Token NUMBER = new Token(TipoToken.NUMBER, "");
    private final Token STRING = new Token(TipoToken.STRING, "");
    private final Token FOR = new Token(TipoToken.FOR, "FOR");
    private final Token FUN = new Token(TipoToken.FUN, "FUN");
    private final Token WHILE = new Token(TipoToken.WHILE, "WHILE");
    private final Token IF = new Token(TipoToken.IF, "IF");
    private final Token RETURN = new Token(TipoToken.RETURN, "RETURN");
    private final Token SUPER = new Token(TipoToken.SUPER, "SUPER");
    private final Token VAR = new Token(TipoToken.VAR, "VAR");
    private final Token CLASS = new Token(TipoToken.CLASS, "CLASS");
    private final Token FALSE = new Token(TipoToken.FALSE, "FALSE");
    private final Token TRUE = new Token(TipoToken.TRUE, "TRUE");
    private final Token PRINT = new Token(TipoToken.PRINT, "PRINT");
    private final Token THIS = new Token(TipoToken.THIS, "THIS");
    private final Token NULL = new Token(TipoToken.NULL, "NULL");
    private final Token ELSE = new Token(TipoToken.ELSE, "ELSE");
    private final Token AND = new Token(TipoToken.AND, "AND");
    private final Token OR = new Token(TipoToken.OR, "OR");
    private final Token CALL = new Token(TipoToken.CALL, "CALL");
    private final Token EOF = new Token(TipoToken.EOF, "");

    private final Token COMMA = new Token(TipoToken.Comma, ",");
    private final Token DOT = new Token(TipoToken.Dot, ".");
    private final Token OPEN_PARENT = new Token(TipoToken.Open_parent, "(");
    private final Token CLOSE_PARENT = new Token(TipoToken.Close_parent, ")");
    private final Token OPEN_CURLY = new Token(TipoToken.Open_curly, "{");
    private final Token CLOSE_CURLY = new Token(TipoToken.Close_curly, "}");
    private final Token DOT_COMMA = new Token(TipoToken.Dot_comma, ";");
    private final Token HYPHEN = new Token(TipoToken.Hyphen, "-");
    private final Token PLUS = new Token(TipoToken.Plus, "+");
    private final Token STAR = new Token(TipoToken.Star, "*");
    private final Token SLASH = new Token(TipoToken.Slash, "/");
    private final Token EXCLAMATIONMARK = new Token(TipoToken.ExclamationMark, "!");
    private final Token EXCLAMATION_EQUAL = new Token(TipoToken.Exclamation_equal, "!=");
    private final Token EQUAL = new Token(TipoToken.Equal, "=");
    private final Token EQUAL_EQUAL = new Token(TipoToken.Equal_equal, "==");
    private final Token LESSTHAN = new Token(TipoToken.LessThan, "<");
    private final Token LESS_EQUAL = new Token(TipoToken.Less_equal, "<=");
    private final Token GREATHERTHAN = new Token(TipoToken.GreatherThan, ">");
    private final Token GREATHER_EQUAL = new Token(TipoToken.Greather_equal, ">=");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();

        if(!hayErrores && !preanalisis.equals(EOF)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(EOF)){
            System.out.println("Sentencia válida");
        }

    }

    void PROGRAM(){
        DECLARATION();
    }


    void DECLARATION(){
        if (preanalisis.equals(CLASS)) {
            CLASS_DECL();
        }else if(preanalisis.equals(FUN)){
            FUN_DECL();
        }else if(preanalisis.equals(VAR)){
            VAR_DECL();
        }else if(preanalisis.equals(IF) || preanalisis.equals(FOR) || preanalisis.equals(PRINT) || preanalisis.equals(RETURN)
         || preanalisis.equals(WHILE) || preanalisis.equals(OPEN_CURLY) || preanalisis.equals(IDENTIFICADOR)){
            STATEMENT();
        }else{
           // hayErrores = true;
           // System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba CLASS, FUN, VAR, IF, WHILE PRINT, RETURN, WHILE, IDENTIFICADOR");
        }
    } 

    void CLASS_DECL(){
        if(hayErrores) return;

        if(preanalisis.equals(CLASS)){
            coincidir(CLASS);
            coincidir(IDENTIFICADOR);
            CLASS_INHER();
            coincidir(OPEN_CURLY);
            FUNCTIONS();
            coincidir(CLOSE_CURLY);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba CLASS");
        }
    }

    void CLASS_INHER(){
        if(hayErrores) return;

        if(preanalisis.equals(LESSTHAN)){
            coincidir(LESSTHAN);
            coincidir(IDENTIFICADOR);
        }
        else{
       //     hayErrores = true;
       //     System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba <");
        }
    }

    void FUN_DECL(){
        if(hayErrores) return;

        if(preanalisis.equals(FUN)){
            coincidir(FUN);
            FUNCTION();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba FUN");
        }
    }

    void VAR_DECL(){
        if(hayErrores) return;

        if(preanalisis.equals(VAR)){
            coincidir(VAR);
            coincidir(IDENTIFICADOR);
            VAR_INIT();
            coincidir(DOT_COMMA);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba VAR");
        }
    }

    void VAR_INIT(){
        if(hayErrores) return;

        if(preanalisis.equals(EQUAL)){
            coincidir(EQUAL);
            EXPRESSION();
        }
        else{
            //hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba =");
        }
    }

    void STATEMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(DOT_COMMA)){
            coincidir(DOT_COMMA);
        }else if(preanalisis.equals(FOR)){
            FOR_STMT();
        }else if(preanalisis.equals(IF)){
            IF_STMT();
        }else if(preanalisis.equals(PRINT)){
            PRINT_STMT();
        }else if(preanalisis.equals(RETURN)){
            RETURN_STMT();
        }else if(preanalisis.equals(WHILE)){
            WHILE_STMT();
        }else if(preanalisis.equals(OPEN_CURLY)){
            BLOCK();
        }else if(preanalisis.equals(IDENTIFICADOR)){
            EXPR_STMT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ;, FOR, IF, PRINT, RETURN, WHILE, IDENTIFICADOR, {");
        }
    }

    void EXPR_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(SUPER) ){
            EXPRESSION();
            coincidir(DOT_COMMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void FOR_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(FOR)){
            coincidir(FOR);
            coincidir(OPEN_PARENT);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            coincidir(CLOSE_PARENT);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba FOR");
        }
    }

    void FOR_STMT_1(){
        if(hayErrores) return;

        if(preanalisis.equals(VAR)){
            VAR_DECL();
        }else if(preanalisis.equals(DOT_COMMA)){
            coincidir(DOT_COMMA);
        }else if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER) ){
            EXPR_STMT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void FOR_STMT_2(){
        if(hayErrores) return;

        if(preanalisis.equals(DOT_COMMA)){
            coincidir(DOT_COMMA);
        }else if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER) ){
            EXPRESSION();
            coincidir(DOT_COMMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void FOR_STMT_3(){
        if(hayErrores) return;

        if(!preanalisis.equals(CLOSE_PARENT)){
            EXPRESSION();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba )");
        }
    }

    void IF_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(IF)){
            coincidir(IF);
            coincidir(OPEN_PARENT);
            EXPRESSION();
            coincidir(CLOSE_PARENT);
            STATEMENT();
            ELSE_STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IF");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(ELSE)){
            coincidir(ELSE);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ELSE");
        }
    }

    void PRINT_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(PRINT)){
            coincidir(PRINT);
            EXPRESSION();
            coincidir(DOT_COMMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba PRINT");
        }
    }

    void RETURN_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(RETURN)){
            coincidir(RETURN);
            RETURN_EXP_OPC();
            coincidir(DOT_COMMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba RETURN");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores) return;

        if(!preanalisis.equals(DOT_COMMA)){
            EXPRESSION();
        }else{
         //   hayErrores = true;
         //   System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ;");
        }
    }

    void WHILE_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(WHILE)){
            coincidir(WHILE);
            coincidir(OPEN_PARENT);
            EXPRESSION();
            coincidir(CLOSE_PARENT);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba WHILE");
        }
    }

    void BLOCK(){
        if(hayErrores) return;

        if(preanalisis.equals(OPEN_CURLY)){
            coincidir(OPEN_CURLY);
            BLOCK_DECL();
            coincidir(CLOSE_CURLY);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba {");
        }
    }

    void BLOCK_DECL(){
        if(hayErrores) return;

        if(preanalisis.equals(CLASS) || preanalisis.equals(FUN) || preanalisis.equals(VAR) || preanalisis.equals(IF)
            || preanalisis.equals(PRINT) || preanalisis.equals(RETURN) || preanalisis.equals(WHILE) || preanalisis.equals(IDENTIFICADOR)){
            DECLARATION();
            BLOCK_DECL();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba CLASS, FUN, VAR, IF, WHILE PRINT, RETURN, WHILE, IDENTIFICADOR");
        }
    }

    void EXPRESSION(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            ASSIGNMENT();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void ASSIGNMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            LOGIC_OR();
            ASSIGNMENT_OPC();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void ASSIGNMENT_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(EQUAL)){
            coincidir(EQUAL);
            EXPRESSION();
         }else{
           // hayErrores = true;
           // System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba =");
        }
    }

    void LOGIC_OR(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            LOGIC_AND();
            LOGIC_OR_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void LOGIC_OR_2(){
        if(hayErrores) return;

        if(preanalisis.equals(OR)){
            coincidir(OR);
            LOGIC_AND();
            LOGIC_OR_2();
         }else{
            //hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba OR");
        }
    }

    void LOGIC_AND(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            EQUALITY();
            LOGIC_AND_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void LOGIC_AND_2(){
        if(hayErrores) return;

        if(preanalisis.equals(AND)){
            coincidir(AND);
            EQUALITY();
            LOGIC_AND_2();
         }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba AND");
        }
    }

    void EQUALITY(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(NUMBER) || preanalisis.equals(STRING) 
         || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            COMPARISON();
            EQUALITY_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void EQUALITY_2(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATION_EQUAL) || preanalisis.equals(EQUAL_EQUAL)){
            //AQUI PUEDE HABER DUDA
            coincidir(preanalisis);
            COMPARISON();
            EQUALITY_2();
         }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !=, ==");
        }
    }

    void COMPARISON(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            TERM();
            COMPARISON_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void COMPARISON_2(){
        if(hayErrores) return;

        if(preanalisis.equals(GREATHERTHAN) || preanalisis.equals(GREATHER_EQUAL) || preanalisis.equals(LESSTHAN)
         || preanalisis.equals(LESS_EQUAL)){
            //OTRA DUDA
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
         }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba <, <=, >, >=");
        }
    }

    void TERM(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            FACTOR();
            TERM_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void TERM_2(){
        if(hayErrores) return;

        if(preanalisis.equals(HYPHEN) || preanalisis.equals(PLUS) ){
            //OTRA DUDA
            coincidir(preanalisis);
            FACTOR();
            TERM_2();
         }else{
         //   hayErrores = true;
         //   System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba +, -");
        }
    }

    void FACTOR(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            UNARY();
            FACTOR_2();
         }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -, NUMERO, CADENA, VERDADERO, FALSO," + 
                "NULO, THIS, (, IDENTIFICADOR, SUPER");
        }
    }

    void FACTOR_2(){
        if(hayErrores) return;

        if(preanalisis.equals(STAR) || preanalisis.equals(SLASH)){
            //OTRA DUDA
            coincidir(preanalisis);
            FACTOR();
            TERM_2();
         }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba *, /");
        }
    }

     void UNARY(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN)){
            //DUDAA
            coincidir(preanalisis);
            UNARY();
       // }else if(preanalisis.equals(CALL)){
         //   CALL();
         }else{
            CALL();
           // hayErrores = true;
           // System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba !, -");
        }
    }

    void CALL(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR) || preanalisis.equals(NUMBER) || preanalisis.equals(STRING)
         || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(NULL) || preanalisis.equals(THIS)
         || preanalisis.equals(OPEN_PARENT) || preanalisis.equals(SUPER)){
            PRIMARY();
            CALL_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR, NUMERO, CADENA, VERDADERO" +
                "FALSO, NULO, THIS, (, SUPER");
        }
    }

    void CALL_2(){
        if(hayErrores) return;

        if(preanalisis.equals(OPEN_PARENT)){
            coincidir(OPEN_PARENT);
            ARGUMENTS_OPC();
            CALL_2();
        }else if(preanalisis.equals(DOT)){
            coincidir(DOT);
            coincidir(IDENTIFICADOR);
            CALL_2();
        }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba (, .");
        }
    }

    void CALL_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(DOT)){
            coincidir(DOT);
            coincidir(IDENTIFICADOR);
            CALL_OPC();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba .");
        }
    }

    void PRIMARY(){
        if(hayErrores) return;

        if(preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(NULL) || preanalisis.equals(THIS) 
            || preanalisis.equals(NUMBER) || preanalisis.equals(STRING) || preanalisis.equals(IDENTIFICADOR)){
            //NO SE
            coincidir(preanalisis);
        }else if(preanalisis.equals(OPEN_PARENT)){
            coincidir(OPEN_PARENT);
            EXPRESSION();
            coincidir(CLOSE_PARENT);
        }else if(preanalisis.equals(SUPER)){
            coincidir(SUPER);
            coincidir(DOT);
            coincidir(IDENTIFICADOR);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba VERDADERO, FALSO, NULO, THIS, NUMERO, CADENA, IDENTIFICADOR");
        }
    }

    void FUNCTION(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            coincidir(IDENTIFICADOR);
            coincidir(OPEN_PARENT);
            PARAMETERS_OPC();
            coincidir(CLOSE_PARENT);
            BLOCK();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR");
        }
    }

    void FUNCTIONS(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            FUNCTION();
            FUNCTIONS();
        }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR.");
        }
    }

    void PARAMETERS_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            PARAMETERS();
        }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR");
        }
    }

    void PARAMETERS(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            coincidir(IDENTIFICADOR);
            PARAMETERS_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR");
        }
    }

    void PARAMETERS_2(){
        if(hayErrores) return;

        if(preanalisis.equals(COMMA)){
            coincidir(COMMA);
            coincidir(IDENTIFICADOR);
            PARAMETERS_2();
        }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ,");
        }
    }

    void ARGUMENTS_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(NULL)
            || preanalisis.equals(NUMBER) || preanalisis.equals(STRING) || preanalisis.equals(OPEN_PARENT) || preanalisis.equals(SUPER)){
            ARGUMENTS();
        }else{
           // hayErrores = true;
           // System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR, VERDADERO, FALSO, NULO, NUMERO, " +
           //     "CADENA, (, SUPER");
        }
    }

    void ARGUMENTS(){
        if(hayErrores) return;

        if(preanalisis.equals(EXCLAMATIONMARK) || preanalisis.equals(HYPHEN) || preanalisis.equals(NUMBER)
         || preanalisis.equals(STRING) || preanalisis.equals(TRUE) || preanalisis.equals(FALSE) || preanalisis.equals(IDENTIFICADOR)
         || preanalisis.equals(NULL) || preanalisis.equals(THIS) || preanalisis.equals(OPEN_PARENT)
         || preanalisis.equals(SUPER)){
            EXPRESSION();
            ARGUMENTS_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba IDENTIFICADOR, NUMERO, CADENA, VERDADERO" +
                "FALSO, NULO, THIS, (, SUPER");
        }
    }

    void ARGUMENTS_2(){
        if(hayErrores) return;

        if(preanalisis.equals(COMMA)){
            coincidir(COMMA);
            EXPRESSION();
            ARGUMENTS_2();
        }else{
          //  hayErrores = true;
          //  System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ,");
        }
    }

    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un  " + t.tipo);

        }
    }

}
