package com.example.tateti_20.ui.annotator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.ui.model.AnnotatorCardModelUi
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.Orange1


/*
* homeViewModel: HomeViewModel = hiltViewModel(), navigateToMach: (String, String) -> Unit, navigateToHalls: (String) -> Unit, navigateToAnnotator: () -> Unit
* */
@Composable
fun AnnotatorScreen(
    annotatorViewModel: AnnotatorViewModel = hiltViewModel(),
    userId: String
){

    val uiState by annotatorViewModel.uiState.collectAsState()

    val listAnnotator = listOf(
        AnnotatorCardModelUi("Generico", SelectAnnotatorViewState.GENERICO),
        AnnotatorCardModelUi("Truco" , SelectAnnotatorViewState.TRUCO),
        AnnotatorCardModelUi("Generala" , SelectAnnotatorViewState.GENERALA),
    )

    when(uiState){
        SelectAnnotatorViewState.GENERALA -> Text(text = "estoy en Generala")
        SelectAnnotatorViewState.GENERICO -> Text(text = "estoy en Generico")
        SelectAnnotatorViewState.TRUCO -> Text(text = "estoy en Truco")
        SelectAnnotatorViewState.VACIO -> {

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

    if (uiState != SelectAnnotatorViewState.VACIO){
        BackHandler {
            annotatorViewModel.setUiState(SelectAnnotatorViewState.VACIO)
        }
    }

}

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