import algorithms.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import parser.parseGrammar
import java.io.File

class LLCommand : CliktCommand(help = "This command checks whether grammar is LL(k) or not") {
    val k by argument("k").int().restrictTo(min = 1)
    val grammarFile by argument("gram").file(mustExist = true, mustBeReadable = true, canBeDir = false)
    val verbose by option("-v", "--verbose").flag(default = false)

    override fun run() {
        val grammar = parseGrammar(grammarFile.readText()).removeUselessSymbols()
        if (verbose) {
            println("After removing useless symbols")
            println(grammar)
        }
        val nullable = NullableTable(grammar)
        if (verbose) {
            println("Nullable non terminals: ${nullable.nullableNonTerms}")
        }
        val leftRecursiveNonTerms = grammar.leftRecursiveNonTerms(nullable)
        if (leftRecursiveNonTerms.isNotEmpty()) {
            println("No!")
            println("Grammar contains left recursive non terminals: $leftRecursiveNonTerms")
            return
        }
        val first = grammar.firstTable(k, nullable)
        if (verbose) {
            println("FIRST_$k:")
            for (nonTerm in grammar.allNonTerms) {
                println("$nonTerm : ${first.firstOf(nonTerm).debugString()}")
            }
        }
        val sigma = SigmaTable(grammar, first, k)
        if (verbose) {
            println("SIGMA:")
            for (nonTerm in grammar.allNonTerms) {
                println("SIGMA($nonTerm) = ${sigma.sigma(nonTerm).map { it.debugString() }}")
            }
        }
        val error = checkLL(grammar, sigma, first, k)
        if (error != null) {
            println("No!")
            println("Here's a pair of problematic rules:")
            println(error.problemRule1)
            println("\t${error.termStrings1.debugString()}")
            println(error.problemRule2)
            println("\t${error.termStrings2.debugString()}")
            println("Because the intersection is not empty: ${error.termStrings1.intersect(error.termStrings2).debugString()}")
            println("The analyser cannot decide which production to use")
        } else {
            println("Yes!")
        }
    }
}

fun main(args: Array<String>) {
    LLCommand().main(args)
}