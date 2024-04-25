package com.example.tateti_20.ui.model

import com.example.tateti_20.ui.annotator.SelectAnnotatorViewState

data class AnnotatorCardModelUi(
    val text:String = "",
    val typeAnnotator: SelectAnnotatorViewState = SelectAnnotatorViewState.VACIO
)