package com.example.tateti_20.ui.generico

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


const val TAG = "erich"
@Composable
fun GenericoScreen(
    genericoViewModel: GenericoViewModel = hiltViewModel(),
    userEmail: String,
    annotatorTime: String
) {
    Log.i(TAG, "GenericoScreen con el usuario $userEmail")
}