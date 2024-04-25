package com.example.tateti_20.ui.annotator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AnnotatorViewModel @Inject constructor(

): ViewModel(){

    private val _uiState = MutableStateFlow<SelectAnnotatorViewState>(SelectAnnotatorViewState.VACIO)
    val uiState: StateFlow<SelectAnnotatorViewState> = _uiState

    fun setUiState(typeAnnotator: SelectAnnotatorViewState){
        _uiState.value = typeAnnotator
    }
}



sealed class SelectAnnotatorViewState {
    object VACIO : SelectAnnotatorViewState()
    object GENERICO : SelectAnnotatorViewState()
    object TRUCO : SelectAnnotatorViewState()
    object GENERALA : SelectAnnotatorViewState()
}