package model

data class Relation<T>(val domain: Set<T>, val data: Set<Pair<T, T>>) {
    fun satisfies(a: T, b: T) = Pair(a, b) in data
}
