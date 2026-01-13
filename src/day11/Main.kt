package day11

import java.io.File
import java.util.ArrayDeque

val START = "you"
val FINISH = "out"

val input = File("src/day11/input").readLines()
    .associate {
        it.split(": ").let { strArr -> strArr[0] to strArr[1].split(" ").toMutableList() }
    }
    .filterReachable(START)
    .toMutableMap()

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1(): Int {
    return countPaths()
}

fun part2(): Long {
    return 0
}

fun countPaths(): Int {
    val parentNodes = input.invert()
    val pathCounts = mutableMapOf<String, Int>()
    pathCounts[START] = 1
    val visited = mutableSetOf<String>()

    val queue = ArrayDeque<String>()
    queue.add(START)
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

        input[node]?.forEach {
            pathCounts[it] = if(pathCounts[it] == null) pathCounts[node]!! else pathCounts[it]!! + pathCounts[node]!!
            queue.add(it)
        }

        visited.add(node)
    }
    return pathCounts[FINISH]!!
}

fun Map<String, List<String>>.filterReachable(from: String): Map<String, List<String>> {
    val reachableNodes = mutableSetOf<String>()

    val queue = ArrayDeque<String>()
    queue.add(START)

    while (queue.isNotEmpty()) {
        val node = queue.pop()
        if (reachableNodes.add(node)) {
            get(node)?.forEach { queue.add(it) }
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

