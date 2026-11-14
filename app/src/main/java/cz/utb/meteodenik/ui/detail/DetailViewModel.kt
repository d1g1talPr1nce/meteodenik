package cz.utb.meteodenik.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.utb.meteodenik.data.MestaRepozitar
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.navigace.Detail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // Argument čteme z SavedStateHandle, ne z UI: přežije otočení i smrt procesu.
    private val mestoId: Int = savedStateHandle.toRoute<Detail>().mestoId

    /** null = město s tímto id neexistuje (např. bylo smazáno). */
    val mesto: StateFlow<Mesto?> = MestaRepozitar.mesta
        .map { seznam -> seznam.firstOrNull { it.id == mestoId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MestaRepozitar.mesta.value.firstOrNull { it.id == mestoId }
        )
}