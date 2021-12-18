# LL(k) Checker

> Checks if the given grammar is LL(k) for the given k

## Grammars

Currently this project supports grammars like this:
```
S := a B;
S := b A;
A := a;
A := b A A;
A := a S;
B := b;
B := a B B;
B := b S;
```
And Ɛ rules too:
```
A := A a;
A := ; 
```

The grammar must be a correctly formatted sequence of rules.
Each rule must start with a nonterminal followed by the := symbol
and an optional sequence of symbols (terminals or nonterminals).

**Terminal** -- a sequence of lowercase latin characters.

**Nonterminal** -- a sequence of uppercase latin characters.

[More about syntax](src/main/antlr/CFGrammar.g4)

## How to use

1. Download the executable file from the _Releases_ page
2. Write a grammar file (`grammar.txt`)
3. Then run
```bash
java -jar llkchecker 1 grammar.txt
```

This will check if `grammar.txt` is LL(1).

To check LL(3) run `java -jar llkchecker 3 grammar.txt`.

**Advanced** You can add a `-v` option to see all the intermediate calculations.
