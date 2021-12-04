package algorithms

import model.Lang
import model.Term
import model.TermString

fun kProduct(lang1: Lang, lang2: Lang, k: Int): Lang {
    require(k >= 1)
    val result = mutableSetOf<TermString>()
    for (s1 in lang1) {
        for (s2 in lang2) {
            result.add((s1 + s2).take(k))
        }
    }
    return result
}

fun kProduct(term: Term, lang: Lang, k: Int): Lang = kProduct(setOf(listOf(term)), lang, k)
