import algorithms.*
import parser.parseGrammar
import java.io.File

fun main() {
    val k = 3
    val text = File("src/main/resources/grammar.txt").readText()
    val grammar = parseGrammar(text).removeUselessSymbols()
    val nullable = NullableTable(grammar)

//    println("First")
//    for (nonTerm in grammar.allNonTerms) {
//        println("$nonTerm : ${first.firstOf(nonTerm).debugString()}")
//    }

    val leftRecursiveNonTerms = grammar.leftRecursiveNonTerms(nullable)

    if (leftRecursiveNonTerms.isNotEmpty()) {
        println("No!")
        println("Grammar contains left recursive non terminals: $leftRecursiveNonTerms")
        return
    }

    val first = grammar.firstTable(k, nullable)
    println("Calculated first")
    val sigma = SigmaTable(grammar, first, k)
    println("Calculated sigma")
    val rulesThatViolateLL = checkLL(grammar, sigma, first, k)

    if (rulesThatViolateLL != null) {
        println("No!")
        println("Here's a pair of problematic rules:\n${rulesThatViolateLL.first}\n${rulesThatViolateLL.second}")
    } else {
        println("Yes!")
    }
}