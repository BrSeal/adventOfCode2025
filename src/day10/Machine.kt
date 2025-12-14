package day10

class Machine(
    val targetState: Int,
    val buttons:List<Int>,
    val targetJoltage: IntArray
) {
    companion object { const val ON = '#'
       fun fromStringArray(arr:List<String>): Machine{
           val targetState = arr[0].replace("[]\\[]".toRegex(), "")
               .map { it == ON }.toBooleanArray().joinToString("") { if(it) "1" else "0" }.toInt(2)
           val buttons = arr.slice(1..<arr.lastIndex)
               .map {
                   val button = BooleanArray(arr[0].length - 2)
                   it.replace("[()]".toRegex(), "").split(",")
                       .map { it.toInt() }
                       .forEach { button[it] = true }
                   button.joinToString("") { if(it) "1" else "0" }.toInt(2)
               }
           val targetJoltage = arr.last().replace("[{}]".toRegex(), "").split(",")
               .map{ it.toInt()}.toIntArray()

           return Machine(targetState, buttons, targetJoltage)
       }


    }
}