package com.example.tateti_20.ui.annotator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.domain.GetAnnotatorGames
import com.example.tateti_20.ui.model.AnnotatorHeadModelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val TAG = "erich"
@HiltViewModel
class AnnotatorViewModel @Inject constructor(
    private val getGames: GetAnnotatorGames
): ViewModel(){

    private val _uiState = MutableStateFlow<AnnotatorsViewState>(AnnotatorsViewState.LOADING)
    val uiState: StateFlow<AnnotatorsViewState> = _uiState

    private val _annotatorGames = MutableStateFlow<List<AnnotatorHeadModelUi>>(mutableListOf())
    val annotatorGames:StateFlow<List<AnnotatorHeadModelUi>> = _annotatorGames

    fun getAnnotatorGames(userEmail: String) {
        Log.d(TAG, "init")
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    getGames(userEmail).collect{
                        Log.d(TAG, "init List<AnnotatorHeadModelUi>: $it")
                        _annotatorGames.value = it
                        _uiState.value = AnnotatorsViewState.ANNOTATORS
                    }
                } catch (e: Exception){
                    _uiState.value = AnnotatorsViewState.LOADING
                }

            }
        }
    }

}



sealed class AnnotatorsViewState {
    object LOADING : AnnotatorsViewState()
    object ANNOTATORS : AnnotatorsViewState()
}