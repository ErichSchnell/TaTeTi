package com.example.tateti_20.ui.annotator

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.Orange1
import java.time.LocalTime
import java.time.format.DateTimeFormatter


private val TAG = "erich"
/*
* homeViewModel: HomeViewModel = hiltViewModel(), navigateToMach: (String, String) -> Unit, navigateToHalls: (String) -> Unit, navigateToAnnotator: () -> Unit
* */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnotatorsScreen(
    annotatorViewModel: AnnotatorViewModel = hiltViewModel(),
    navigateToTruco: (String, String) -> Unit,
    navigateToGenerico: (String, String) -> Unit,
    navigateToGenerala: (String, String) -> Unit,
    userEmail: String
){
    var showDialog by remember{mutableStateOf(false)}
    val uiState by annotatorViewModel.uiState.collectAsState()


    LaunchedEffect(true){
        annotatorViewModel.getAnnotatorGames(userEmail)
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){
        when(uiState){
            AnnotatorsViewState.LOADING -> Loading(Modifier.align(Alignment.Center))
            AnnotatorsViewState.ANNOTATORS -> {

            }
        }

        StartAnnotator(Modifier.align(Alignment.BottomEnd)){showDialog = true}

        if (showDialog) {
            DialogSelectAnnotator(
                onDismissRequest = { showDialog = false },
                onClickAnnotatorGenerico = {time ->  annotatorViewModel.navigateTo(userEmail, time, navigateToGenerico)},
                onClickAnnotatorTruco = {time ->  annotatorViewModel.navigateTo(userEmail, time, navigateToTruco) },
                onClickAnnotatorGenerala = {time ->  annotatorViewModel.navigateTo(userEmail, time, navigateToGenerala) }
            )
        }
    }
}


@Composable
fun Loading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun StartAnnotator(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(modifier = modifier.padding(24.dp), onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogSelectAnnotator(
    onDismissRequest: () -> Unit,
    onClickAnnotatorGenerico: (time:String) -> Unit,
    onClickAnnotatorTruco: (time:String) -> Unit,
    onClickAnnotatorGenerala: (time:String) -> Unit
){
    val currentTime = LocalTime.now()
    val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))


    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Annotator(text = "GENERICO")  {onClickAnnotatorGenerico(formattedTime)}
            Annotator(text = "TRUCO") {onClickAnnotatorTruco(formattedTime)}
            Annotator(text = "GENERALA") {onClickAnnotatorGenerala(formattedTime)}
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Annotator(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick:()-> Unit = {}
){
    Card (
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .clickable { onClick() },
        backgroundColor = Orange1,
        contentColor = Accent,
        elevation = 12.dp,
        onClick = {onClick()}
    ) {
        Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = text)
        }
    }
}