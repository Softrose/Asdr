public enum TipoToken {
    IDENTIFICADOR, NUMBER, STRING,

    // Palabras reservadas
    //SELECT, FROM, DISTINCT,

    FOR, WHILE, IF, RETURN, SUPER, FUN, VAR,
    CLASS, TRUE, FALSE, THIS, NULL, PRINT,
    ELSE, AND, OR, CALL,

    // Caracteres
    //COMA, PUNTO, ASTERISCO,

    Open_parent, Close_parent, Open_curly, Close_curly,
    Comma, Dot, Dot_comma, Hyphen, Plus, Star,
    ExclamationMark, Exclamation_equal, Equal, Equal_equal,
    LessThan, Less_equal, GreatherThan, Greather_equal,

    // Final de cadena
    EOF
}

//COMENTARIO DE PRUEBA