package com.example.tateti_20.ui.truco

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


const val TAG = "erich"
@Composable
fun TrucoScreen(
    trucoViewModel: TrucoViewModel = hiltViewModel()
){
    Log.i(TAG, "TrucoScreen: ")
}