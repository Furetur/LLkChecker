package parser

import CFGrammarBaseVisitor
import CFGrammarLexer
import CFGrammarParser
import model.NonTerm
import model.Symbol
import model.Term
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.Vocabulary

interface Node {
    val children: List<Node>
}

data class GrammarNode(val rules: List<RuleNode>) : Node {
    override val children: List<Node> = rules
}

data class RuleNode(val def: SymbolNode, val symbols: List<SymbolNode>) : Node {
    override val children = listOf(def) + symbols
}

data class SymbolNode(val symbol: Symbol) : Node {
    override val children: List<Node> = emptyList()
}
