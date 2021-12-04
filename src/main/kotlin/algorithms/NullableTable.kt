package algorithms

import algorithms.common.PointerBasedAlgorithm
import model.Grammar
import model.NonTerm
import model.Rule
import model.Term

class NullableTable(grammar: Grammar) : PointerBasedAlgorithm(grammar) {
    private val nullable = mutableMapOf<NonTerm, Boolean>()

    init {
        start()
    }

    val nullableNonTerms: Map<NonTerm, Boolean>
        get() = nullable

    fun isNullable(nonTerm: NonTerm): Boolean = nullable[nonTerm] ?: error("Nonterm $nonTerm is not in the table")

    override fun tryAdvancePointer(rule: Rule) {
        val def = rule.def
        val currentSymbol = getCurrentSymbol(rule)
        // if at the end of production
        if (currentSymbol == null) {
            nullable[def] = true
            discardPointer(rule)

        } else {
            // if at terminal
            if (currentSymbol is Term) {
                nullable[def] = false
                discardPointer(rule)
            } else { // if at nonterm
                // if nonterm is nullable
                if (nullable[currentSymbol] == true) {
                    advancePointer(rule)
                } else if (nullable[currentSymbol] == false) { // if nonterm is not nullable
                    nullable[def] = false
                    discardPointer(rule)
                }
                // if it is unknown whether nonterm is nullable or not, we just wait
            }
        }
    }
}