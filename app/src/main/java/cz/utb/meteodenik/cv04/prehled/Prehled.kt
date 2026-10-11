
package cz.utb.meteodenik.cv04.prehled

// 1. Datová třída
data class Mesto(
    val nazev: String,
    val teplota: Double
)

// 2. Extension funkce
fun Mesto.popis(): String {
    return "$nazev: $teplota C"
}

// 3. Sealed interface pro stav
sealed interface StavPrehledu {
    data object Nacita : StavPrehledu
    data object Chyba : StavPrehledu
    data object Prazdno : StavPrehledu
    data object Hotovo : StavPrehledu
}

// 4. Třída Prehled
class Prehled(
    val mesta: List<Mesto>,
    var stav: StavPrehledu = StavPrehledu.Hotovo
) {

    // Výpis aktuálního stavu
    fun textStavu(): String = when (stav) {
        StavPrehledu.Nacita -> "Načítám…"
        StavPrehledu.Chyba -> "Chyba"
        StavPrehledu.Prazdno -> "Nic tu není"
        StavPrehledu.Hotovo -> "Měst: ${mesta.size}"
    }

    // Nalezení města s nejvyšší teplotou
    fun nejteplejsi(): Mesto? {
        return mesta.maxByOrNull { it.teplota }
    }

    // Výpis všech měst
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

// 5. Hlavní funkce
fun main() {

    val mesta = listOf(
        Mesto("Zlín", 21.4),
        Mesto("Brno", 23.0),
        Mesto("Otrokovice", 20.9)
    )

    val prehled = Prehled(mesta)

    prehled.vypis()
}
