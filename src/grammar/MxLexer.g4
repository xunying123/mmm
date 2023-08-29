lexer grammar MxLexer;

LineComment : '//' .*? '\r'? ('\n' | EOF) -> channel(HIDDEN);
BlockComment : '/*'.*? '*/' ->channel(HIDDEN);

Void:     'void';
Bool:     'bool';
Int:      'int';
String:   'string';
New:      'new';
Class:    'class';
Null:     'null';
True:     'true';
False:    'false';
This:     'this';
If:       'if';
Else:     'else';
For:      'for';
While:    'while';
Break:    'break';
Continue: 'continue';
Return:   'return';

OpInc : '++';
OpDec : '--';
OpSr : '>>';
OpSl : '<<';

OpLogAnd : '&&';
OpLogOr : '||';

OpAnd : '&';
OpOr :  '|';
OpNor : '^';
OpNot : '~';

OpAdd : '+';
OpSub : '-';
OpMul : '*';
OpDiv : '/';
OpMod : '%';
OpGt : '>';
OpLt : '<';
OpGe : '>=';
OpLe : '<=';
OpNe : '!=';
OpEq : '==';

Ask : '?';
Colon : ':';

OpLogNot : '!';

OpAss : '=';

OpObj : '.';

LSbrac : '[';
RSbrac : ']';
Lbrac : '(';
Rbrac : ')';
LBbrac : '{';
RBbrac : '}';

Comma : ',';
SemiColon : ';';

fragment Digit : [0-9];
fragment Symbol : [!"#$%&'()*+,\-./:;<=>?@[\]^_`{|}~];
fragment DigitEx0 : [1-9];
fragment Letter : [a-zA-Z];
fragment IdentiChara : [a-zA-Z0-9_];
fragment EscapeChara : '"' | 'n' | '\\';
fragment StringChara : ~["\\\n\r\u2028\u2029] | '\\' EscapeChara;

Identifier : Letter IdentiChara*;

InterLiteral : '0' | (DigitEx0 Digit*);
StringLiteral : '"' StringChara* '"';

Space : (' ' | '\t' | '\u000B' | '\u000C' | '\u00A0') ->channel(HIDDEN);
Line : ('\r' | '\n' | '\u2028' | '\u2029') ->channel(HIDDEN);
