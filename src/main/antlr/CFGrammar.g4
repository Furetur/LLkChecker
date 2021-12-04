grammar CFGrammar;

// Tokens
TERM: [a-z]+;
NONTERM: [A-Z]+;
DEF: ':=';
SEMI: ';';
WHITESPACE: [ \r\n\t]+ -> skip;

// Rules
gram : grammarRule+ EOF;

grammarRule : def=NONTERM DEF symbol* SEMI;

symbol
  : TERM # term
  | NONTERM # nonTerm
  ;

