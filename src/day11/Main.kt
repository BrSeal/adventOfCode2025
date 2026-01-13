package day11

import java.io.File
import java.util.ArrayDeque

val YOU = "you"
val OUT = "out"
val SVR = "svr"
val DAC = "dac"
val FFT = "fft"

val input = File("src/day11/input").readLines()
    .associate {
        it.split(": ").let { strArr -> strArr[0] to strArr[1].split(" ").toMutableList() }
    }
    .toMutableMap()

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1(): Long {
    return countPaths(YOU, OUT)
}

fun part2(): Long {
    val srvToDac = countPaths(SVR, OUT)
    val dacToFft = countPaths(DAC, FFT)
    val fftToOut = countPaths(FFT, OUT)

    val srvToFft = countPaths(SVR, FFT)
    val fftToDac = countPaths(FFT, DAC)
    val dacToOut = countPaths(DAC, OUT)
    return srvToDac * dacToFft * fftToOut + srvToFft * fftToDac * dacToOut
}

fun countPaths(from: String, to:String): Long {
    val readableInput = input.filterReachable(from)
    val parentNodes = readableInput.filterReachable(from).invert()
    val pathCounts = mutableMapOf<String, Long>()
    pathCounts[from] = 1
    val visited = mutableSetOf<String>()

    val queue = ArrayDeque<String>()
    queue.add(from)
    while (queue.isNotEmpty()) {
        val node = queue.pop()

        if(node in visited){
            continue
        }

        val parents = parentNodes[node]?: listOf()
        if (parents.any { it !in visited }) {
            queue.add(node)
            continue
        }

        readableInput[node]?.forEach {
            pathCounts[it] = if(pathCounts[it] == null) pathCounts[node]!! else pathCounts[it]!! + pathCounts[node]!!
            queue.add(it)
        }

        visited.add(node)
    }
    return pathCounts[to]?:0
}

fun Map<String, List<String>>.filterReachable(from: String): Map<String, List<String>> {
    val reachableNodes = mutableSetOf<String>()

    val queue = ArrayDeque<String>()
    queue.add(from)

    while (queue.isNotEmpty()) {
        val node = queue.pop()
        if (reachableNodes.add(node)) {
            this[node]?.forEach { queue.add(it) }
        }
    }

    return filterKeys { it in reachableNodes }
}

fun Map<String, List<String>>.invert(): Map<String, List<String>> {
    val inverted = mutableMapOf<String, MutableList<String>>()
    forEach { (k, v) ->
        v.forEach {
            inverted[it] = inverted.getOrDefault(it, mutableListOf()).also { it.add(k) }
        }
    }
    return inverted
}

