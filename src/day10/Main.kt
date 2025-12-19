package day10

import java.io.File
import java.util.ArrayDeque

val input = File("src/day10/input.tst").readLines()
    .map { it.split(" ") }
    .map { Machine.fromStringArray(it) }

fun main() {
    var start = System.currentTimeMillis()
//    input.sumOf { calcButtonPresses(it) }
//        .also { println("Part 1: $it (took ${System.currentTimeMillis() - start} ms)") }

    start = System.currentTimeMillis()
    input.sumOf { calcJoltagePresses(it) }
        .also { println("Part 2: $it (took ${System.currentTimeMillis() - start} ms)") }
}

fun calcButtonPresses(m: Machine): Int {
    var minPresses = m.buttons.size

    val queue = ArrayDeque<Triple<Int, Int, List<Machine.Button>>>()
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
            queue.push(Triple(currentState xor button.intVal, iterationNum + 1, buttons))
        }
    }

    return minPresses
}

fun calcJoltagePresses(m: Machine): Int {
    val matrix = m.buttons.map {it.array}.toTypedArray().rotate()
        .mapIndexed {button, it -> IntArray(it.size + 1){i -> if(i != it.size) it[i] else m.targetJoltage[button]} }
        .toTypedArray()
    return solveSystem(matrix).sum()
}

fun solveSystem(matrix: Array<IntArray>): IntArray {
    val result = IntArray(matrix[0].size)
    val base = matrix.copyOf()

    toStepMatrix(matrix)

    return result
}

fun toStepMatrix(matrix:  Array<IntArray>) {
    for(i in 0..<matrix.lastIndex){
        for(j in i..<matrix[i].lastIndex){
            if(matrix.makeStep(i,j)){
                break
            }
        }
    }
}

fun Array<IntArray>.rotate(): Array<IntArray> {
    val result = Array(this[0].size) { IntArray(this.size) }
    for (row in this.indices) {
        for (col in this[row].indices) {
            result[col][row] = this[row][col]
        }
    }
    return result
}

fun Array<IntArray>.makeStep(startRow: Int, startCol: Int): Boolean{
    // делаем так, чтобы первая строка была ненулевая
    if(this[startRow][startCol] == 0){
        for(i in startRow + 1 ..this.lastIndex){
            if(this[i][startCol] != 0){
                swap(startRow, i)
                break
            }
        }
    }

    // если ненулевой строки нет, то ничего не делаем
    if(this[startRow][startCol] == 0){
        return false
    }

    // зануляем все следующие коэффициенты
    for (row in startRow+1..lastIndex){
        if(this[row][startCol] != 0) {
            val base = this[startRow].map { it * this[row][startCol] }
            for (col in startCol..this[row].lastIndex) {
                this[row][col] = this[row][col] * this[startRow][startCol] - base[col]
            }
        }
    }
    return true
}

fun <T> Array<T>.swap(i:Int, j: Int){
    this[i] = this[j].also { this[j] = this[i] }
}
