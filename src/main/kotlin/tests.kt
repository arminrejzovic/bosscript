import java.util.StringJoiner

fun main() {
    val source = """
        za svako (var x od 0 do 10 korak 2){
           ispis(x)
        }
        
        objekat o = {
            ime: "mirel",
            prezime: "bajric",
            godiste: 2003,
            prosjek: 3.67,
            bogatstvo: 1_000_000,
            student: tacno,
            otac: {
                ime: "Miralem",
                prezime: "Bajric"
            }
        }
        
        ispis(o.ime)
    """.trimIndent()

    val short = "\"hello odjebi\" odjebi do 10"

    val tokenRegex = hashMapOf(
        "-?(0|[1-9](_?[0-9])*)(\\.[0-9](_?[0-9])*)?([eE][-+]?[0-9]+)?".toRegex() to "NUMBER",
        "\"([^\"\\\\]|\\\\.)*\"".toRegex() to "STRING",
        "\bfunkcija\b".toRegex() to "FUNCTION_KEYWORD",
        "za".toRegex() to "ZA_KEYWORD",
        "svako".toRegex() to "SVAKO_KEYWORD",
        "var".toRegex() to "VAR_KEYWORD",
        "konst".toRegex() to "KONST_KEYWORD",
        "od".toRegex() to "OD_KEYWORD",
        "do".toRegex() to "DO_KEYWORD",
        "korak".toRegex() to "KORAK_KEYWORD",
        "objekat".toRegex() to "OBJEKAT_KEYWORD",
    )

    var line = 0
    val tokens = arrayListOf<String>()

    source.lines().forEach {
        var cursor = 0
        tokenRegex.forEach { entry ->
            val matchResult = entry.key.find(it.substring(cursor))
            if(matchResult != null){
                tokens.add("${entry.value} -> ${matchResult.value} [length: ${matchResult.value.length}], position: $cursor")
                cursor += matchResult.value.length
            }
        }

        line++
    }
    tokens.forEach {
        println(it)
    }

    val regex = "svako".toRegex()
    val str = "zasvako x"
    println(regex.find(str)?.value)
}