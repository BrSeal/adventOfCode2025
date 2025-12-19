package day10

class Machine(
    val targetState: Int,
    val buttons:List<Button>,
    val targetJoltage: IntArray
) {
    companion object { const val ON = '#'
       fun fromStringArray(arr:List<String>): Machine{
           val targetState = arr[0].replace("[]\\[]".toRegex(), "")
               .map { it == ON }.toBooleanArray().joinToString("") { if(it) "1" else "0" }.toInt(2)
           val buttons = arr.slice(1..<arr.lastIndex)
               .map {
                   val button = IntArray(arr[0].length - 2)
                   it.replace("[()]".toRegex(), "").split(",")
                       .map { it.toInt() }
                       .forEach { button[it] = 1 }

                   Button(button.joinToString("").toInt(2), button)

               }
           val targetJoltage = arr.last().replace("[{}]".toRegex(), "").split(",")
               .map{ it.toInt()}.toIntArray()

           return Machine(targetState, buttons, targetJoltage)
       }
    }

    class Button(val intVal: Int, val array: IntArray)
}