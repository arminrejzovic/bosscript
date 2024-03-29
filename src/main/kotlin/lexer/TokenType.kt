package lexer

enum class TokenType{
    // Literals ---------------------------------
    Number,
    String,
    Identifier,
    // Symbols ----------------------------------
    OpenParen,      // (
    CloseParen,     // )
    OpenBracket,    // [
    CloseBracket,   // ]
    OpenBrace,      // {
    CloseBrace,     // }
    Comma,          // ,
    Dot,            // .
    Colon,          // :
    Semicolon,      // ;
    SimpleAssign,   // =
    DoubleQuote,    // "
    Exponent,       // ^
    LogicalAnd,     // &&
    LogicalOr,      // ||
    LogicalNot,     // !
    // KEYWORDS ---------------------------------
    //    Variables
    Var,
    Konst,
    //    Models
    Model,
    //    Loops
    Za,
    Svako,
    Od,
    Do,
    Korak,
    Dok,
    Radi,
    //    Functions
    Funkcija,
    Arrow,          // =>
    Vrati,
    Se,
    //    Package imports
    Paket,
    //    If-else
    Ako,
    Ili,
    Inace,
    Osim,
    //    Special
    Nedefinisano,
    Tacno,
    Netacno,
    // OPERATORS --------------------------------
    BinaryOperator,
    RelationalOperator,
    EqualityOperator,
    // SPECIAL TOKENS ---------------------------
    EOF,
    ComplexAssign,
    Break,
    UnaryIncrement,
    UnaryDecrement,
    Tip,
    This,
    Private,
    Public,
    Static,
    Try,
    Catch,
    Finally,
    Constructor,
    Javascript
}