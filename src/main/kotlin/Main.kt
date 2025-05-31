import kotlin.random.Random

fun main() {
    val runnerAdvancementCalculator = DefaultRunnerAdvancementCalculator()
    val eventGenerator = EventGenerator(RandomWrapper(Random.Default), runnerAdvancementCalculator)
    val observer = GameSimulatorPrinter()
    val gameSimulator = GameSimulator(eventGenerator, observer)
    gameSimulator.simulateGame()
}
