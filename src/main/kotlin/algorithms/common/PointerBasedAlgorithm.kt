package algorithms.common

import model.Grammar
import model.Rule
import model.Symbol

abstract class PointerBasedAlgorithm(grammar: Grammar) {
    private val rules = grammar.allRules
    private val pointers = grammar.allRules.associateWith { 0 }.toMutableMap()
    private var didUpdateHappen = true

    protected fun start() {
        while (didUpdateHappen) {
            didUpdateHappen = false
            for (rule in rules) {
                if (rule in pointers) tryAdvancePointer(rule)
            }
        }
    }

    protected abstract fun tryAdvancePointer(rule: Rule)

    protected fun getPointer(rule: Rule): Int? = pointers[rule]

    protected fun getCurrentSymbol(rule: Rule): Symbol? = pointers[rule]?.let { rule.value.getOrNull(it) }

    protected fun advancePointer(rule: Rule) {
        val currentValue = getPointer(rule) ?: return
        pointers[rule] = currentValue + 1
        didUpdateHappen = true
    }

    protected fun discardPointer(rule: Rule) {
        pointers.remove(rule)
        didUpdateHappen = true
    }
}
