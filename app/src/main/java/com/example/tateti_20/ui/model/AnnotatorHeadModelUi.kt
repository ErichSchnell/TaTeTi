package com.example.tateti_20.ui.model

import com.example.tateti_20.data.network.model.AnnotatorHeadModelData
import com.example.tateti_20.ui.annotator.AnnotatorsViewState

data class AnnotatorHeadModelUi (
    val name:String = "",
    val annotatorType:AnnotatorsViewState = AnnotatorsViewState.LOADING,
    val reference:String = "",
) {
    fun toModelData() = AnnotatorHeadModelData(
        name = name,
        annotatorType = annotatorType,
        reference = reference
    )
}