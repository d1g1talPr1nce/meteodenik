package cz.utb.meteodenik.data

import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Jediný zdroj pravdy o městech. Zatím jen v paměti, v lekci 11 sem přibude síť. */
object MestaRepozitar {

    private val _mesta = MutableStateFlow(UKAZKOVA_MESTA)
    val mesta: StateFlow<List<Mesto>> = _mesta.asStateFlow()

    fun pridej(nazev: String) {
        _mesta.update { seznam ->
            val noveId = (seznam.maxOfOrNull { it.id } ?: 0) + 1
            seznam + Mesto(noveId, nazev, null)
        }
    }

    fun smaz(id: Int) {
        _mesta.update { seznam -> seznam.filterNot { it.id == id } }
    }
}
