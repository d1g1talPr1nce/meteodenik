
package cz.utb.meteodenik.cv04.prehled


data class Mesto(
    val nazev: String,
    val teplota: Double
)


fun Mesto.popis(): String {
    return "$nazev: $teplota C"
}


sealed interface StavPrehledu {
    data object Nacita : StavPrehledu
    data object Chyba : StavPrehledu
    data object Prazdno : StavPrehledu
    data object Hotovo : StavPrehledu
}


class Prehled(
    val mesta: List<Mesto>,
    var stav: StavPrehledu = StavPrehledu.Hotovo
) {

    
    fun textStavu(): String = when (stav) {
        StavPrehledu.Nacita -> "Načítám…"
        StavPrehledu.Chyba -> "Chyba"
        StavPrehledu.Prazdno -> "Nic tu není"
        StavPrehledu.Hotovo -> "Měst: ${mesta.size}"
    }

    
    fun nejteplejsi(): Mesto? {
        return mesta.maxByOrNull { it.teplota }
    }

    
    fun vypis() {
        mesta.forEach { mesto ->
            println(mesto.popis())
        }

        println(textStavu())

        nejteplejsi()?.let { mesto ->
            println(
                "Nejtepleji: ${mesto.nazev} (${mesto.teplota} C)"
            )
        }
    }
}


fun main() {

    val mesta = listOf(
        Mesto("Zlín", 21.4),
        Mesto("Brno", 23.0),
        Mesto("Otrokovice", 20.9)
    )

    val prehled = Prehled(mesta)

    prehled.vypis()
}
