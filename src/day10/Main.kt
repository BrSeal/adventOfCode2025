package day10

import java.io.File
import java.util.ArrayDeque

val input = File("src/day10/input.tst").readLines()
    .map { it.split(" ") }
    .map { Machine.fromStringArray(it) }

fun main() {
    input.sumOf { calcButtonPresses(it) }
        .also { println("Part 1: $it") }
}

fun calcButtonPresses(m: Machine): Int {
    var minPresses = m.buttons.size

    val queue = ArrayDeque<Triple<Int, Int, List<Int>>>()
    queue.add(Triple(0, 0, m.buttons))

    while (queue.isNotEmpty()) {
        val iteration = queue.pop()
        val currentState = iteration.first
        val iterationNum = iteration.second
        val availableButtons = iteration.third

        if (availableButtons.isEmpty()) {
            continue
        }

        if (iterationNum >= minPresses) {
            continue
        }

        if (currentState == m.targetState) {
            minPresses = iterationNum
            continue
        }
        m.buttons.forEach { button ->
            val buttons = availableButtons.filter { it != button }
            queue.push(Triple(currentState xor button, iterationNum + 1, buttons))
        }
    }

    return minPresses
}