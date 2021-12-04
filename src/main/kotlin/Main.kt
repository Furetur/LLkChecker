import algorithms.NullableTable
import algorithms.firstTable
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

    println("k = 1")
    val firstTable1 = grammar.firstTable(1)
    for (nonTerm in grammar.allNonTerms) {
        println("$nonTerm : ${firstTable1.firstOf(nonTerm)}")
    }
    println("k = 2")
    val firstTable = grammar.firstTable(3)
    for (nonTerm in grammar.allNonTerms) {
        println("$nonTerm : ${firstTable.firstOf(nonTerm)}")
    }
}