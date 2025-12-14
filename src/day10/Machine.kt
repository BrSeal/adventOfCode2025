package day10

class Machine(
    val targetState: BooleanArray,
    val buttons:List<BooleanArray>,
    val joltage: List<Int>,
    val currentState: BooleanArray = BooleanArray(targetState.size),
) {
    companion object {
        const val ON = '#'
       fun fromStringArray(arr:List<String>): Machine{
           val targetState = arr[0].replace("[]\\[]".toRegex(), "")
               .map { it == ON }.toBooleanArray()
           val buttons = arr.slice(1..<arr.lastIndex)
               .map {
                   val button = BooleanArray(targetState.size)
                   it.replace("[()]".toRegex(), "").split(",")
                       .map { it.toInt() }
                       .forEach { button[it] = true }
                   button
               }
           val joltage = arr.last().replace("[{}]".toRegex(), "").split(",")
               .map{ it.toInt()}

           return Machine(targetState, buttons, joltage)
       }


    }

    fun push(i: Int){
        val button = buttons[i]
        for(i in button.indices){
            if(button[i]){
                currentState[i] = !currentState[i]
            }
        }
    }

    override fun toString(): String {
        return getStateStr()
    }

    fun getStateStr(): String{
        return currentState.joinToString("", "[", "]") { if(it) "#" else "." }
    }
}