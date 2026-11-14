package cz.utb.meteodenik.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import cz.utb.meteodenik.data.MestaRepozitar
import kotlinx.coroutines.launch

/** Všechno, co je na obrazovce seznamu vidět. Neměnné – změna = nová instance přes copy(). */
data class MestaUiState(
    val mesta: List<Mesto> = emptyList(),
    val jeDuplikat: Boolean = false
)

class MestaViewModel : ViewModel() {

    private val _stav = MutableStateFlow(MestaUiState(mesta = MestaRepozitar.mesta.value))
    val stav: StateFlow<MestaUiState> = _stav.asStateFlow()

    init {
        // Kdykoli se seznam v repozitáři změní, promítneme ho do stavu obrazovky.
        viewModelScope.launch {
            MestaRepozitar.mesta.collect { mesta ->
                _stav.update { it.copy(mesta = mesta) }
            }
        }
    }

    /** Vrátí true, pokud se město přidalo – UI pak vyprázdní pole. */
    fun pridej(nazev: String): Boolean {
        val cistyNazev = nazev.trim()
        if (cistyNazev.isEmpty()) return false

        val uzExistuje = _stav.value.mesta.any { it.nazev.trim().equals(cistyNazev, ignoreCase = true) }
        if (uzExistuje) {
            _stav.update { it.copy(jeDuplikat = true) }
            return false
        }

        MestaRepozitar.pridej(cistyNazev)
        _stav.update { it.copy(jeDuplikat = false) }
        return true
    }

    fun smaz(id: Int) = MestaRepozitar.smaz(id)

    fun zrusChybu() {
        _stav.update { s -> if (s.jeDuplikat) s.copy(jeDuplikat = false) else s }
    }

    override fun onCleared() {
        Log.d("Stav", "onCleared")
        super.onCleared()
    }
}