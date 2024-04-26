package com.example.tateti_20.domain

import javax.inject.Inject

class GetAnnotatorGames @Inject constructor(private val dataServerService: DataServerService){

    operator fun invoke(userEmail:String) = dataServerService.getAnnotatorGames(userEmail)
}