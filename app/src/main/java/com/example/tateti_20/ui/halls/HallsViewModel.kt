package com.example.tateti_20.ui.halls

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.tateti_20.domain.GetHalls
import com.example.tateti_20.ui.model.GameModelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HallsViewModel @Inject constructor(
//    private val getHalls: GetHalls
): ViewModel() {
    private val _uiState = MutableStateFlow<HallsViewState>(HallsViewState.LOADING)
    val uiState: StateFlow<HallsViewState> = _uiState

    private val _halls = MutableStateFlow<List<GameModelUi?>?>(mutableListOf())
    val halls:StateFlow<List<GameModelUi?>?> = _halls

    init {
                Log.d("erich", "init")
        viewModelScope.launch {
//            getHalls().collect{
//                Log.d("erich", "recibiendo la data")
//                if(it != null){
//                    _halls.value = it.halls
//                    _uiState.value = HallsViewState.HALLS
//                }
//            }
        }
    }
    fun joinGame(hallId: String, userId: String, navigateToMach: (String, String) -> Unit) {
        navigateToMach(hallId,userId)
    }
}


sealed class HallsViewState(){
    object LOADING:HallsViewState()
    object HALLS:HallsViewState()
}