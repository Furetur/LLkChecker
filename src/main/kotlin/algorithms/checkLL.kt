package algorithms

import model.Grammar
import model.Lang
import model.Rule


fun checkLL(grammar: Grammar, sigmaTable: SigmaTable, firstTable: FirstTable, k: Int): LLCheckError? {
    for (nonTerm in grammar.allNonTerms) {
        val curRules = grammar.rules[nonTerm] ?: emptySet()
        val allRulePairs = cartesianProduct(curRules, curRules)
        for ((rule1, rule2) in allRulePairs) {
            if (rule1 == rule2) continue
            for (lang in sigmaTable.sigma(nonTerm)) {
                val first1 = kProduct(firstTable.firstOf(rule1), lang, k)
                val first2 = kProduct(firstTable.firstOf(rule2), lang, k)
                if (first1.intersect(first2).isNotEmpty()) {
                    return LLCheckError(
                        problemRule1 = rule1,
                        problemRule2 = rule2,
                        termStrings1 = first1,
                        termStrings2 = first2
                    )
                }
            }
        }
    }
    return null
}

data class LLCheckError(val problemRule1: Rule, val termStrings1: Lang, val problemRule2: Rule, val termStrings2: Lang)