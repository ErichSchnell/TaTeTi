package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.ui.model.GameModelUi
//import com.example.tateti_20.ui.model.HallsModelUi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHalls @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke() = dataServerService.getHalls()

}