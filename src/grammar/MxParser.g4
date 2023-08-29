parser grammar MxParser;

options {
  tokenVocab=MxLexer;
}

fileAnalyze : (functionDeclaration | variableDeclaration | classDeclaration)* EOF;

functionDeclaration : returnTypename Identifier '(' parameterList? ')'  '{' blockStatement '}';

returnTypename : typename | Void;

parameterList : (typename Identifier) (Comma typename Identifier)*;

blockStatement : statement*;

classDeclaration : Class Identifier '{' (variableDeclaration | functionDeclaration | construction )* '}' SemiColon;

construction : Identifier '(' ')' '{' blockStatement '}';

variableDeclaration : typename declaration (Comma declaration)* SemiColon;

declaration : Identifier (OpAss expression)?;

typename : name ('[' ']')*;

basicVariable : Bool | Int | String ;

name : basicVariable | Identifier;

statement : '{' blockStatement '}' |
            variableDeclaration |
            expressionStatement |
            ifStatement |
            whileStatement |
            forStatement |
            breakStatement |
            continueStatement |
            returnStatement ;

ifStatement : If '(' expression ')' statement (Else statement)?;

whileStatement : While '(' expression ')' statement ;

forStatement : For '(' forVar expressionStatement expression? ')' statement ;

forVar : variableDeclaration | expressionStatement ;

breakStatement : Break SemiColon;

continueStatement : Continue SemiColon;

returnStatement : Return expression? SemiColon;

expressionStatement : expression? SemiColon;

expression :  '(' expression ')'                                                      #parent
            | New name (newArrayExpression)* ('(' ')')?                               #newEx
            | expression op=OpObj Identifier                                          #objEx
            | expression '[' expression ']'                                           #arrayEx
            | expression '(' functionCall?')'                                         #funEx
            | <assoc=right> expression op = (OpInc | OpDec)                           #unaryEx
            | op = (OpInc | OpDec) expression                                         #leftEx
            | <assoc=right> op = (OpLogNot | OpSub | OpNot | OpAdd ) expression       #unaryEx
            | expression op = ( OpMul | OpDiv | OpMod ) expression                    #binaryEx
            | expression op = ( OpAdd | OpSub ) expression                            #binaryEx
            | expression op = ( OpSr | OpSl ) expression                              #binaryEx
            | expression op = ( OpGt | OpLt | OpGe | OpLe | OpNe | OpEq ) expression  #binaryEx
            | expression op=OpAnd expression                                          #binaryEx
            | expression op=OpOr expression                                           #binaryEx
            | expression op=OpNor expression                                          #binaryEx
            | expression op=OpLogAnd expression                                       #binaryEx
            | expression op=OpLogOr expression                                        #binaryEx
            | <assoc=right> expression op=OpAss expression                            #assEx
            | <assoc=right> expression op=Ask expression op=Colon expression          #ternaryEx
            | basicExpression                                                         #basicEx ;

newArrayExpression : '[' expression? ']';

functionCall : expression (Comma expression)* ;

basicExpression : InterLiteral | StringLiteral | True | False | Null | Identifier | This ;