package cz.utb.meteodenik.model

data class Mesto(
    val id: Int,
    val nazev: String,
    val teplota: Double?
)

val UKAZKOVA_MESTA = listOf(
    Mesto(1, "Zlín", 21.4),
    Mesto(2, "Brno", 23.1),
    Mesto(3, "Ostrava", 19.8)
)