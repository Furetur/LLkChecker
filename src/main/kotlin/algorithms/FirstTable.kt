package algorithms

import model.*

fun Grammar.firstTable(k: Int, nullableTable: NullableTable = NullableTable(this)) =
    if (k == 1) OneFirstTable(this, nullableTable) else HigherFirstTable(this, k, nullableTable)

interface FirstTable {
    val k: Int
    fun firstOf(nonTerm: NonTerm): Lang

    fun firstOf(term: Term): Lang = setOf(listOf(term))

    fun firstOf(symbol: Symbol): Lang = when (symbol) {
        is Term -> firstOf(symbol)
        is NonTerm -> firstOf(symbol)
    }

    fun firstOf(symbolString: SymbolString): Lang = if (symbolString.isEmpty()) {
        setOf(EPSILON)
    } else {
        kProduct(firstOf(symbolString.first()), firstOf(symbolString.drop(1)), k)
    }

    fun firstOf(rule: Rule) = firstOf(rule.value)
}

abstract class BaseFirstTable(protected val grammar: Grammar, protected val nullableTable: NullableTable) :
    FirstTable {
    protected val first = mutableMapOf<NonTerm, MutableSet<TermString>>()

    protected fun start() {
        for (nonTerm in grammar.allNonTerms) {
            processNonTerm(nonTerm)
        }
    }

    protected fun processNonTerm(nonTerm: NonTerm): Set<TermString> {
        if (nullableTable.isNullable(nonTerm)) {
            first.getOrPut(nonTerm) { mutableSetOf() }.add(EPSILON)
        }
        for (rule in grammar.rules[nonTerm] ?: emptySet()) {
            processSymbols(rule.def, rule.value)
        }
        return first[nonTerm] ?: error("Internal error. First for $nonTerm did not get calculated")
    }

    protected abstract fun processSymbols(def: NonTerm, symbols: SymbolString)

    override fun firstOf(nonTerm: NonTerm): Lang =
        first[nonTerm] ?: error("Internal error. $nonTerm is not in first table")
}

private class OneFirstTable(grammar: Grammar, nullableTable: NullableTable) : BaseFirstTable(grammar, nullableTable) {
    override val k: Int = 1

    init {
        start()
    }

    override fun processSymbols(def: NonTerm, symbols: SymbolString) {
        val firstSymbol = symbols.firstOrNull() ?: return
        when (firstSymbol) {
            is Term -> {
                first.getOrPut(def) { mutableSetOf() }.add(listOf(firstSymbol))
            }
            is NonTerm -> {
                first.getOrPut(def) { mutableSetOf() }.addAll(processNonTerm(firstSymbol))
                if (nullableTable.isNullable(firstSymbol)) {
                    processSymbols(def, symbols.drop(1))
                }
            }
        }
    }
}

private class HigherFirstTable(
    grammar: Grammar,
    override val k: Int,
    nullableTable: NullableTable
) : BaseFirstTable(grammar, nullableTable) {

    init {
        require(k >= 2)
    }

    private val prevFirst = grammar.firstTable(k - 1, nullableTable)

    init {
        start()
    }

    override fun processSymbols(def: NonTerm, symbols: SymbolString) {
        val firstSymbol = symbols.firstOrNull() ?: return
        val rest = symbols.drop(1)
        when (firstSymbol) {
            is Term -> {
                val result = kProduct(firstSymbol, prevFirst.firstOf(rest), k)
                first.getOrPut(def) { mutableSetOf() }.addAll(result)
            }
            is NonTerm -> {
                first.getOrPut(def) { mutableSetOf() }.addAll(
                    kProduct(processNonTerm(firstSymbol) - setOf(EPSILON), prevFirst.firstOf(rest), k)
                )
                if (nullableTable.isNullable(firstSymbol)) {
                    processSymbols(def, symbols.drop(1))
                }
            }
        }
    }
}
