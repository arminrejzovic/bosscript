import java.io.File

fun main() {
    val coords = File("C:\\Users\\armin\\IdeaProjects\\bosscript\\src\\test\\kotlin\\Koordinate.txt").readLines()
    var dest = File("C:\\Users\\armin\\IdeaProjects\\bosscript\\src\\test\\kotlin\\output.txt")

    var id = 0
    coords.forEach {
        val data = it.split(", ")
        dest.appendText("cities.add(new City($id, \"${data[0]}\", ${data[1]}, ${data[2]}));\n")
        id++
    }
}