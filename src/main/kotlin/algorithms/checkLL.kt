package algorithms

import model.Grammar
import model.Rule


fun checkLL(grammar: Grammar, sigmaTable: SigmaTable, firstTable: FirstTable, k: Int): Pair<Rule, Rule>? {
    for (nonTerm in grammar.allNonTerms) {
        val curRules = grammar.rules[nonTerm] ?: emptySet()
        val allRulePairs = cartesianProduct(curRules, curRules)
        for ((rule1, rule2) in allRulePairs) {
            if (rule1 == rule2) continue
            for (lang in sigmaTable.sigma(nonTerm)) {
                val first1 = kProduct(firstTable.firstOf(rule1), lang, k)
                val first2 = kProduct(firstTable.firstOf(rule2), lang, k)
                if (first1.intersect(first2).isNotEmpty()) return Pair(rule1, rule2)
            }
        }
    }
    return null
}
