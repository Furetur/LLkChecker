package parser

import CFGrammarBaseVisitor
import CFGrammarLexer
import CFGrammarParser
import model.Grammar
import model.NonTerm
import model.Rule
import model.Term
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.Vocabulary

fun parseGrammar(text: String): Grammar {
    val grammarNode = parseGrammarNode(text)

    // build rules
    val rules = mutableMapOf<NonTerm, MutableSet<Rule>>()

    for (rule in grammarNode.rules) {
        val def = rule.def.symbol as NonTerm
        rules.getOrPut(def) { mutableSetOf() }.add(rule.toRule())
    }
    return Grammar(grammarNode.rules.first().def.symbol as NonTerm, rules)
}

private fun RuleNode.toRule() = Rule(def.symbol as NonTerm, symbols.map { it.symbol })

private fun parseGrammarNode(text: String): GrammarNode {
    val charStream = CharStreams.fromString(text + "\n")
    val lexer = CFGrammarLexer(charStream)
    val parser = CFGrammarParser(CommonTokenStream(lexer))
    return GrammarParserVisitor(lexer.vocabulary).visitGram(parser.gram())
}

private class GrammarParserVisitor(private val vocabulary: Vocabulary) : CFGrammarBaseVisitor<Node>() {
    override fun visitGram(ctx: CFGrammarParser.GramContext?): GrammarNode =
        GrammarNode(ctx?.grammarRule()?.map { visitGrammarRule(it) } ?: emptyList())

    override fun visitGrammarRule(ctx: CFGrammarParser.GrammarRuleContext?): RuleNode =
        RuleNode(ctx!!.def.toSymbolNode(), ctx.symbol().map { visit(it) as SymbolNode })

    override fun visitTerm(ctx: CFGrammarParser.TermContext?): Node = SymbolNode(Term(ctx!!.text))

    override fun visitNonTerm(ctx: CFGrammarParser.NonTermContext?): Node = SymbolNode(NonTerm(ctx!!.text))

    private fun Token.toSymbolNode() = when (vocabulary.getSymbolicName(type)) {
        "TERM" -> SymbolNode(Term(text))
        "NONTERM" -> SymbolNode(NonTerm(text))
        else -> error("Internal Error. Unknown token type $this")
    }
}