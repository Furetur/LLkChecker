import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    antlr
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.github.furetur"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.9.3")
    implementation("com.github.ajalt.clikt:clikt:3.2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    dependsOn("generateGrammarSource")
    kotlinOptions.jvmTarget = "1.8"
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}
