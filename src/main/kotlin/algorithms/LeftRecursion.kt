package algorithms

import model.Grammar
import model.NonTerm

fun Grammar.leftRecursiveNonTerms(nullableTable: NullableTable = NullableTable(this)): Set<NonTerm> {
    val potentiallyStartsWith = potentiallyStartsWith(this, nullableTable)
    return potentiallyStartsWith.domain.filter { potentiallyStartsWith.satisfies(it, it) }.toSet()
}
