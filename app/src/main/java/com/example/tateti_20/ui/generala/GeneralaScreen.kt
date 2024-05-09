package com.example.tateti_20.ui.generala

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

const val TAG = "erich"

@Composable
fun GeneralaScreen(
    generalaViewModel: GeneralaViewModel = hiltViewModel(),
    userEmail: String,
    annotatorTime: String
) {
    Log.i(TAG, "GeneralaScreen con el usuario $userEmail")
}