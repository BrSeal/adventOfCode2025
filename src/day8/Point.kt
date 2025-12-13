private var count = 1
class Point(coordinates: List<Long>){
    val x = coordinates[0]
    val y = coordinates[1]
    val z = coordinates[2]

    var path = mutableSetOf<Point>(this)

    val id = count++

    fun distanceTo(p:Point): Long{
        return (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y) + (z-p.z)*(z-p.z)
    }

    override fun toString(): String {
        return "P$id($x,$y,$z)"
    }
}