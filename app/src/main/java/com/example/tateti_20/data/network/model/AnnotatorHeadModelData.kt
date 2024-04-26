package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.annotator.AnnotatorsViewState
import com.example.tateti_20.ui.model.AnnotatorHeadModelUi

data class AnnotatorHeadModelData (
    val name:String? = null,
    val annotatorType: AnnotatorsViewState? = null,
    val reference:String? = null,
) {
    fun toModelUi() = AnnotatorHeadModelUi(
        name = name.orEmpty(),
        annotatorType = annotatorType ?: AnnotatorsViewState.ANNOTATORS,
        reference = reference.orEmpty()
    )
}