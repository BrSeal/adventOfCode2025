import java.io.File

val input = File("src/day8/input").readLines()
    .map { it.split(",").map(String::toLong) }
    .map { Point(it) }
val wires = if(input.size == 20) 10 else 1000

val distances = run {
    val map = mutableMapOf<Long, Pair<Point, Point>>()

    for (i in input.indices) {
        for (j in i + 1..input.lastIndex) {
            map[input[i].distanceTo(input[j])] = input[i] to input[j]
        }
    }
    map.entries.sortedBy { it.key }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

fun part1(): Int{
    var i = 0
    while (i < wires){
        connect(distances[i].value.first, distances[i].value.second)
        i++
    }
    return input.map { point -> point.path.size }.toSet().sorted()
        .takeLast(3)
        .reduce { a,b -> a*b }
}

fun part2(): Long {
    var i = 0
    while (input.first().path.size != input.size) {
        connect(distances[i].value.first, distances[i].value.second)
        i++
    }
    return distances[i - 1]
        .let { (_, edge) -> edge.first.x * edge.second.x }
}

fun connect(p1: Point, p2: Point) {
    if (p1.path !== p2.path) {
        p1.path.addAll(p2.path)
        p1.path.forEach { p -> p.path = p1.path }
    }
}


