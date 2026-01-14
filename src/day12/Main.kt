package day12

import java.io.File

val input = File("src/day12/input").readLines()

val SHAPES = input.let {
    val shapes = mutableListOf<Array<IntArray>>()
    for (i in input.indices step 5) {
        if(input[i].contains('x')) break

        val shape = arrayOf(
            input[i + 1].map { if (it == '#') 1 else 0 }.toIntArray(),
            input[i + 2].map { if (it == '#') 1 else 0 }.toIntArray(),
            input[i + 3].map { if (it == '#') 1 else 0 }.toIntArray()
        )

        shapes.add(shape)
    }
    shapes
}

val REGIONS = input.let {
    val regions = mutableListOf<Region>()

    for (str in input){
        if(!str.contains('x')){
            continue
        }

        val dimensions = str.split(": ")[0].split("x").map {it.toInt()}
        val boxes = str.split(": ")[1].trim().split(" ").map {it.toInt()}.toIntArray()
        regions.add(Region(dimensions[0], dimensions[1], boxes))
    }
    regions
}

fun main() {
    println("SHAPES: " + SHAPES.size)
    println("REGIONS: " + REGIONS.size)
}

class Region(
    val width: Int,
    val height: Int,
    val boxCounts: IntArray
)