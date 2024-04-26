package com.example.tateti_20.ui.annotator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.tateti_20.ui.model.AnnotatorCardModelUi
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Orange1


/*
* homeViewModel: HomeViewModel = hiltViewModel(), navigateToMach: (String, String) -> Unit, navigateToHalls: (String) -> Unit, navigateToAnnotator: () -> Unit
* */
@Composable
fun AnnotatorScreen(
    annotatorViewModel: AnnotatorViewModel = hiltViewModel(),
    userEmail: String
){
    var showDialog by remember{mutableStateOf(false)}
    val uiState by annotatorViewModel.uiState.collectAsState()


    LaunchedEffect(true){
        annotatorViewModel.getAnnotatorGames(userEmail)
    }

    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        when(uiState){
            AnnotatorsViewState.LOADING -> Loading(Modifier.align(Alignment.Center))
            AnnotatorsViewState.ANNOTATORS -> {

            }
        }

        StartAnnotator(Modifier.align(Alignment.BottomEnd)){showDialog = true}

        if (showDialog){
            Dialog(onDismissRequest = { showDialog = false }) {
                Column {
                    Annotator(annotatorCardModelUi =  AnnotatorCardModelUi("Generala"))
                    Annotator(annotatorCardModelUi =  AnnotatorCardModelUi("Generico"))
                    Annotator(annotatorCardModelUi =  AnnotatorCardModelUi("truco"))
                }
            }
        }
    }










}
@Composable
fun Loading(modifier: Modifier = Modifier) {
//    Box(modifier = modifier, contentAlignment = Alignment.Center){
        CircularProgressIndicator(modifier = modifier)
//    }
}

@Composable
fun StartAnnotator(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var showDialog by remember{mutableStateOf(false)}
    FloatingActionButton(modifier = modifier, onClick = { onClick() }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }

}


/*
when(uiState){
        AnnotatorsViewState.GENERALA -> Text(text = "estoy en Generala")
        AnnotatorsViewState.GENERICO -> Text(text = "estoy en Generico")
        AnnotatorsViewState.TRUCO -> Text(text = "estoy en Truco")
        AnnotatorsViewState.VACIO -> {

            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
                contentAlignment = Alignment.Center
            ){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    content = {
                        items(listAnnotator) { annotator ->
                            Annotator(modifier = Modifier.height(100.dp), annotatorCardModelUi = annotator){
                                annotatorViewModel.setUiState(annotator.typeAnnotator)
                            }
                        }
                    },
                    contentPadding = PaddingValues(16.dp)
                )
            }

        }
    }

    if (uiState != AnnotatorsViewState.VACIO){
        BackHandler {
            annotatorViewModel.setUiState(AnnotatorsViewState.VACIO)
        }
    }
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Annotator(
    modifier: Modifier = Modifier,
    annotatorCardModelUi: AnnotatorCardModelUi = AnnotatorCardModelUi("empty"),
    onClick:()-> Unit = {}
){
    Card (
        modifier = modifier.padding(16.dp),
        backgroundColor = Orange1,
        contentColor = Accent,
        elevation = 12.dp,
        onClick = {onClick()}
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = annotatorCardModelUi.text)
        }
    }
}