package cz.utb.meteodenik.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Všechno, co je na obrazovce seznamu vidět. Neměnné – změna = nová instance přes copy(). */
data class MestaUiState(
    val mesta: List<Mesto> = emptyList(),
    val jeDuplikat: Boolean = false
)

class MestaViewModel : ViewModel() {

    // Soukromý: zapisovat smí jen ViewModel.
    private val _stav = MutableStateFlow(MestaUiState(mesta = UKAZKOVA_MESTA))

    // Veřejný: UI smí jen číst.
    val stav: StateFlow<MestaUiState> = _stav.asStateFlow()

    /** Vrátí true, pokud se město přidalo – UI pak vyprázdní pole. */
    fun pridej(nazev: String): Boolean {
        val cistyNazev = nazev.trim()
        if (cistyNazev.isEmpty()) return false

        var pridano = false
        _stav.update { s ->
            val uzExistuje = s.mesta.any { it.nazev.trim().equals(cistyNazev, ignoreCase = true) }
            if (uzExistuje) {
                pridano = false
                s.copy(jeDuplikat = true)
            } else {
                pridano = true
                val noveId = (s.mesta.maxOfOrNull { it.id } ?: 0) + 1
                s.copy(mesta = s.mesta + Mesto(noveId, cistyNazev, null), jeDuplikat = false)
            }
        }
        return pridano
    }

    fun smaz(id: Int) {
        _stav.update { s -> s.copy(mesta = s.mesta.filterNot { it.id == id }) }
    }

    fun zrusChybu() {
        _stav.update { s -> if (s.jeDuplikat) s.copy(jeDuplikat = false) else s }
    }

    override fun onCleared() {
        Log.d("Stav", "onCleared")
        super.onCleared()
    }
}