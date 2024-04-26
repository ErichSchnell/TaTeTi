package com.example.tateti_20.ui.model

import com.example.tateti_20.ui.annotator.AnnotatorsViewState

data class AnnotatorCardModelUi(
    val text:String = "",
    val typeAnnotator: AnnotatorsViewState = AnnotatorsViewState.LOADING
)