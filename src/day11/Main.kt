package day11

import java.io.File

val START = "you"
val FINISH = "out"

val input = File("src/day11/input.tst").readLines()
    .associate { it.split(": ").let {strArr -> strArr[0] to strArr[1].split(" ").toMutableList() } }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1(): Long {
    return 0
}

fun part2(): Long{
    return 0
}