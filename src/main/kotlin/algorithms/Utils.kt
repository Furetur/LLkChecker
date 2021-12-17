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

fun <A, B> cartesianProduct(xs: Iterable<A>, ys: Iterable<B>): Set<Pair<A, B>> {
    val result = mutableSetOf<Pair<A, B>>()
    for (x in xs) {
        for (y in ys) {
            result.add(Pair(x, y))
        }
    }
    return result
}

fun <T> List<T>.findAll(value: T): List<Int> {
    val result = mutableListOf<Int>()
    for ((index, el) in withIndex()) {
        if (el == value) {
            result.add(index)
        }
    }
    return result
}

fun TermString.debugString() = "<${joinToString(",")}>"
fun Lang.debugString() = this.map { it.debugString() }.toString()
