package day9

import java.io.File
import java.util.ArrayDeque
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val COL = 1
const val ROW = 0

const val EMPTY = 0L
const val RED = 1L
const val GREEN = 2L
const val UNKNOWN = 3L

val input = File("src/day9/input").readLines()
    .map { it.split(",").let { intArrayOf(it[COL].toInt(), it[ROW].toInt()) } }
val colsNormalizedIndex = input.asSequence().map { it[COL] }.toSet().sorted().withIndex().associate { it.value to it.index * 2 }
val rowsNormalizedIndex = input.asSequence().map { it[ROW] }.toSet().sorted().withIndex().associate { it.value to it.index * 2 }

val colWeights = getNormalized(COL)
val rowWeights = getNormalized(ROW)

val grid = Array(rowWeights.size) { LongArray(colWeights.size) { UNKNOWN } }
    .also { grid ->
        input.forEach {
            val c = colsNormalizedIndex[it[COL]]!!
            val r = rowsNormalizedIndex[it[ROW]]!!
            grid[r][c] = RED
        }
    } // put red lights on grid
    .also { grid ->
        val fillArray = { array: LongArray ->
            val first = array.indexOfFirst { it == RED }
            val last = array.indexOfLast { it == RED }
            if (first != -1) {
                for (i in first + 1..<last) array[i] = GREEN
            }
        }

        for (row in grid) {
            fillArray(row)
        }

        val rotated = run {
            val result = Array(grid[0].size) { LongArray(grid.size) }
            for (row in grid.indices) {
                for (col in grid[row].indices) {
                    result[col][row] = grid[row][col]
                }
            }
            result
        } // rotated by 90 deg copy of grid to simplify vertical connections

        for (row in rotated) {
            fillArray(row)
        }

        for (row in rotated.indices) {
            for (col in rotated[row].indices) {
                grid[col][row] = rotated[row][col]
            }
        }
    } // connect all red lights
    .also { grid ->
        val findEmptyFields = { startRow: Int, startCol: Int ->
            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.add(startRow to startCol)

            while (queue.isNotEmpty()) {
                val data = queue.pop()
                val r = data.first
                val c = data.second
                if (r !in grid.indices || c !in grid[r].indices) {
                    continue
                }
                val point = grid[r][c]
                if (point == RED || point == GREEN || point == EMPTY) {
                    continue
                }
                grid[r][c] = EMPTY

                queue.add(r - 1 to c)
                queue.add(r + 1 to c)
                queue.add(r to c - 1)
                queue.add(r to c + 1)
            }
        } // recursion through queue to avoid SO

        findEmptyFields(0, 0)
        findEmptyFields(0, grid[0].lastIndex)
        findEmptyFields(grid.lastIndex, 0)
        findEmptyFields(grid.lastIndex, grid[0].lastIndex)
    } // find and mark all empty spaces
    .also { grid ->
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                val up = if (row > 0) grid[row - 1][col] else 0
                val left = if (col > 0) grid[row][col - 1] else 0
                val diagLeft = if (row > 0 && col > 0) grid[row - 1][col - 1] else 0

                val cellWeight = if (grid[row][col] == EMPTY) 0 else colWeights[col] * rowWeights[row]
                grid[row][col] = up + left - diagLeft + cellWeight
            }
        }
    } // calc prefix sum on grid

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1(): Long {
    var max = 0L
    for(i in input.indices) {
        for (j in i + 1..input.lastIndex) {
            val area = getArea(input[i], input[j])
            if (area > max){
                max = area
            }
        }
    }

    return max
}

fun part2(): Long{
    var max = 0L
    for (i in input.indices) {
        for (j in i + 1..input.lastIndex) {
            val p1 = input[i]
            val p2 = input[j]

            val area = getArea(p1, p2)
            val paintedArea = getPaintedArea(p1, p2)
            if (area == paintedArea && area > max) {
                max = area
            }
        }
    }

    return max
}

fun getArea(p1: IntArray, p2: IntArray): Long {
    val x = abs(p1[ROW] - p2[ROW]).toLong() + 1
    val y = abs(p1[COL] - p2[COL]).toLong() + 1

    return x * y
}

fun getPaintedArea(p1: IntArray, p2: IntArray): Long {
    val r1 = rowsNormalizedIndex[min(p1[ROW], p2[ROW])]!!
    val c1 = colsNormalizedIndex[min(p1[COL], p2[COL])]!!
    val r2 = rowsNormalizedIndex[max(p1[ROW], p2[ROW])]!!
    val c2 = colsNormalizedIndex[max(p1[COL], p2[COL])]!!

    val upper = if (r1 != 0) grid[r1 - 1][c2] else 0
    val left = if (c1 != 0) grid[r2][c1 - 1] else 0
    val diag = if (r1 != 0 && c1 != 0) grid[r1 - 1][c1 - 1] else 0

    return grid[r2][c2] - upper - left + diag
}

fun getNormalized(index: Int): IntArray {
    val values = input.map { it[index] }.toSet().sorted()
    val arr = IntArray(values.size * 2 - 1) { 1 }

    for (i in 1..<arr.lastIndex step 2) {
        val firstI = i / 2
        val secondI = (i + 1) / 2
        arr[i] = values[secondI] - values[firstI] - 1
    }
    return arr
}