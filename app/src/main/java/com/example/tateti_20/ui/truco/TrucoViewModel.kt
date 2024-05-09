package com.example.tateti_20.ui.truco

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "erich"
@HiltViewModel
class TrucoViewModel @Inject constructor(

): ViewModel() {

    init {
        Log.i(TAG, "viewModel: init")
    }





}