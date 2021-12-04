import algorithms.NullableTable
import algorithms.leftRecursiveNonTerms
import parser.parseGrammar
import java.io.File

fun main() {
    val text = File("src/main/resources/grammar.txt").readText()
    val grammar = parseGrammar(text)
    println(grammar)
    println("Without useless symbols")
    println(grammar.removeUselessSymbols())
    println("Nullable: ${NullableTable(grammar).nullableNonTerms}")
    println("Left recursive nonterms: ${grammar.leftRecursiveNonTerms()}")
}