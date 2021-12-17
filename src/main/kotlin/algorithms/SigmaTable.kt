package algorithms

import model.Grammar
import model.Lang
import model.NonTerm

private typealias SigmaInput = Pair<NonTerm, NonTerm>

class SigmaTable(private val grammar: Grammar, private val firstTable: FirstTable, private val k: Int) {
    private var sigmas = mutableMapOf<SigmaInput, Set<Lang>>()
    private val allSigmaInputs = cartesianProduct(grammar.allNonTerms, grammar.allNonTerms)
    private val notSaturatedSigmaInputs = allSigmaInputs.toMutableSet()

    init {
        start()
    }

    fun xsigma(a: NonTerm, b: NonTerm): Set<Lang> = sigmas[Pair(a, b)] ?: error("Sigma'($a, $b) is not calculated")

    fun sigma(nonTerm: NonTerm): Set<Lang> = xsigma(grammar.main, nonTerm)

    private fun start() {
        baseCase()
        var i = 0
        while (notSaturatedSigmaInputs.isNotEmpty()) {
//            println("Sigmas i = $i. Not saturated: ${notSaturatedSigmaInputs.size}")
//            println(sigmas.mapValues { it.value.map { it.debugString() } })
            val nextSigmas = sigmas.mapValues { it.value.toSet() }.toMutableMap()
            for ((a, b) in notSaturatedSigmaInputs.toSet()) {
                val nextSigmasAB = calcNewSigmas(a, b)
                if (nextSigmasAB == nextSigmas[Pair(a, b)]) {
                    notSaturatedSigmaInputs.remove(Pair(a, b))
                }
                nextSigmas[Pair(a, b)] = nextSigmasAB
            }
            sigmas = nextSigmas
            i += 1
        }
    }

    private fun calcNewSigmas(a: NonTerm, b: NonTerm): Set<Lang> {
        val newSigmas = sigmas[Pair(a, b)]!!.toMutableSet()
        for (rule in grammar.rules[a] ?: emptySet()) {
            for ((xIndex, x) in rule.value.withIndex()) {
                if (x !is NonTerm) continue
                val leftLangs = sigmas[Pair(x, b)]!!
                val rightFirst = firstTable.firstOf(rule.value.subList(xIndex + 1, rule.value.size))
                for (leftLang in leftLangs) {
                    newSigmas.add(kProduct(leftLang, rightFirst, k))
                }
            }
        }
        return newSigmas
    }

    // i = 0
    private fun baseCase() {
        for ((a, b) in allSigmaInputs) {
            val thisSigma = mutableSetOf<Lang>()
            for (rule in grammar.rules[a] ?: emptySet()) {
                val allRightContexts = rule.value.findAll(b).map { i -> rule.value.subList(i + 1, rule.value.size) }
                for (rightContext in allRightContexts) {
                    thisSigma.add(firstTable.firstOf(rightContext))
                }
            }
            sigmas[Pair(a, b)] = thisSigma
        }
    }
}
