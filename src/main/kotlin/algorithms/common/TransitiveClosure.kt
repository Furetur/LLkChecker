package algorithms.common

import model.Relation

fun <T> Relation<T>.transitiveClosure(): Relation<T> {
    val data = data.toMutableSet()

    for (k in domain) {
        for (i in domain) {
            for (j in domain) {
                if (Pair(i, j) in data) continue
                if (Pair(i, k) in data && Pair(k, j) in data) data.add(Pair(i, j))
            }
        }
    }

    return Relation(domain, data)
}
