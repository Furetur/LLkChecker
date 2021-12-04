package algorithms

import algorithms.common.PointerBasedAlgorithm
import algorithms.common.transitiveClosure
import model.*

fun potentiallyStartsWith(grammar: Grammar, nullableTable: NullableTable = NullableTable(grammar)): Relation<NonTerm> =
    directlyStartsWith(grammar, nullableTable).transitiveClosure()

fun directlyStartsWith(grammar: Grammar, nullableTable: NullableTable = NullableTable(grammar)): Relation<NonTerm> =
    Relation(grammar.allNonTerms.toSet(), DirectlyStartsWith(grammar, nullableTable).data)

private class DirectlyStartsWith(grammar: Grammar, private val nullableTable: NullableTable) :
    PointerBasedAlgorithm(grammar) {

    val data = mutableSetOf<Pair<NonTerm, NonTerm>>()

    init {
        start()
    }

    override fun tryAdvancePointer(rule: Rule) {
        val currentSymbol = getCurrentSymbol(rule)
        // if at the end of production
        if (currentSymbol == null) {
            discardPointer(rule)
        } else {
            // if at nonterm
            if (currentSymbol is NonTerm) {
                data.add(Pair(rule.def, currentSymbol))
                // if nonterm is nullable
                if (nullableTable.isNullable(currentSymbol)) {
                    advancePointer(rule)
                } else { // if nonterm is not nullable
                    discardPointer(rule)
                }
            } else { // if at term
                discardPointer(rule)
            }
        }
    }
}