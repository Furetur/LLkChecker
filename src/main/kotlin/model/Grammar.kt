package model

import java.util.*

// Symbols

sealed class Symbol {
    abstract val name: String
}

data class Term(override val name: String) : Symbol() {
    override fun toString(): String = name
}

data class NonTerm(override val name: String): Symbol() {
    override fun toString(): String = name
}

typealias SymbolString = List<Symbol>
typealias TermString = List<Term>
typealias Lang = Set<TermString>

val EPSILON: TermString = emptyList()

// Rules

data class Rule(val def: NonTerm, val value: SymbolString) {
    override fun toString(): String = "$def := ${value.joinToString(" ")}"
}
data class Grammar(val main: NonTerm, val rules: Map<NonTerm, Set<Rule>>) {
    val allRules = rules.flatMap { it.value }
    val allNonTerms = rules.keys.toList()
    val allRightNonTerms = allRules.flatMap { rule -> rule.value.filterIsInstance<NonTerm>() }

    init {
        // verify that every mentioned nonterminal is defined
        require(main in rules) { "Main nonterminal $main is not defined" }
        for (nonTerm in allRightNonTerms) {
            require(nonTerm in rules) { "Nonterminal $nonTerm is referenced but is not defined" }
        }
    }

    fun removeUselessSymbols(): Grammar {
        val visitedNonTerms = mutableSetOf<NonTerm>()
        val queue: Queue<NonTerm> = LinkedList<NonTerm>().also { it.add(main) }

        while (queue.isNotEmpty()) {
            val cur = queue.poll()
            val neighbourNonTerms = rules[cur]?.flatMap { it.value.filterIsInstance<NonTerm>() }?.toSet() ?: emptySet()
            neighbourNonTerms.filter { it !in visitedNonTerms }.forEach { neighbourNonTerm -> queue.add(neighbourNonTerm)  }
            visitedNonTerms.add(cur)
        }
        return Grammar(main, rules.filter { it.key in visitedNonTerms })
    }

    override fun toString(): String = "Main nonterminal: $main\n" + allRules.joinToString("\n")
}
