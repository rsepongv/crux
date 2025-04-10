grammar Crux;
program
: declList EOF
;

declList
: decl*
;

decl
: varDecl
| function
;

varDecl
: VAR IDENTIFIER type? SEMICOLON
;

function
: FUNC? IDENTIFIER OPEN_PAREN paramList? CLOSE_PAREN COLON? type? block
;

paramList
: param (COMMA param)*
;

param
: type? IDENTIFIER
;

type
: IDENTIFIER
|OPEN_BRACKET expression CLOSE_BRACKET type?
;

block
: OPEN_BRACE statement* CLOSE_BRACE
;

statement
: varDecl
| ifStmt
| loopStmt
| breakStmt
| continueStmt
| returnStmt
| expression SEMICOLON
| block
;

ifStmt
: IF expression statement (ELSE statement)?
;

loopStmt
: LOOP statement
;

breakStmt
: BREAK SEMICOLON
;

continueStmt
: CONTINUE SEMICOLON
;

returnStmt
: RETURN expression? SEMICOLON
;

expression
: IDENTIFIER (OPEN_BRACKET expression CLOSE_BRACKET)* ASSIGN expression
| orExp
;

orExp
: andExp (OR andExp)*
;

andExp
: eqExp (AND eqExp)*
;

eqExp
: relationalExp ((EQUAL | NOT_EQUAL) relationalExp)*
;

relationalExp
: addExp((GREATER_EQUAL | LESSER_EQUAL | GREATER_THAN | LESS_THAN) addExp)*
;

addExp
: mulExp ((ADD | SUB) mulExp)*
;

mulExp
: unaryExp ((MUL | DIV) unaryExp)*
;

unaryExp
: NOT unaryExp
| primaryExp
;

primaryExp
: IDENTIFIER OPEN_PAREN (expression (COMMA expression)*)? CLOSE_PAREN
|IDENTIFIER (OPEN_BRACKET expression CLOSE_BRACKET)*
| literal
| IDENTIFIER
| OPEN_PAREN expression CLOSE_PAREN
;


literal
: INTEGER
| TRUE
| FALSE
;

//PARSER ^
//LEXER v

//reserved rules
AND : '&&';
OR : '||';
NOT : '!';

VAR: 'var';

FUNC : 'func';
IF : 'if';
ELSE : 'else';
LOOP : 'loop';
BREAK : 'break';
CONTINUE : 'continue';
TRUE : 'true';
FALSE : 'false';
RETURN : 'return';

//special rules
OPEN_PAREN : '(';
CLOSE_PAREN : ')';
OPEN_BRACE : '{';
CLOSE_BRACE : '}';
OPEN_BRACKET : '[';
CLOSE_BRACKET : ']';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
GREATER_EQUAL : '>=';
LESSER_EQUAL : '<=';
NOT_EQUAL : '!=';
EQUAL : '==';
GREATER_THAN : '>';
LESS_THAN : '<';
ASSIGN : '=';
COMMA : ',';
COLON : ':';
SEMICOLON : ';';

INTEGER
: '0'
| [1-9] [0-9]*
;

IDENTIFIER
: [a-zA-Z] [a-zA-Z0-9_]*
;

WhiteSpaces
: [ \t\r\n]+ -> skip
;

Comment
: '//' ~[\r\n]* -> skip
;
