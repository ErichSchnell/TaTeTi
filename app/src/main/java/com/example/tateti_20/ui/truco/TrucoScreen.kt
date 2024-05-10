package com.example.tateti_20.ui.truco

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.TrucoModelUI
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2

private const val TAG = "erich"
@Composable
fun TrucoScreen(
    trucoViewModel: TrucoViewModel = hiltViewModel(),
    userEmail: String,
    annotatorTime: String
){
    LaunchedEffect(true){
        trucoViewModel.initAnnotator(userEmail, annotatorTime)
    }

    val uiState by trucoViewModel.uiState.collectAsState()
    val game by trucoViewModel.game.collectAsState()

    when(uiState){
        TrucoState.LOADING -> Loading(Modifier.fillMaxSize())
        TrucoState.READY -> {

            Column(modifier = Modifier
                .fillMaxSize()
                .background(Background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Cabecera(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    player1Name = game.player1.playerName,
                    player2Name = game.player2.playerName,
                    onClickPlayer1 = { trucoViewModel.changeNamePlayer1("josue1") },
                    onClickPlayer2 = { trucoViewModel.changeNamePlayer2("josue2") }
                )

                Body(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    game = game,
                    increasePlayer1 = { trucoViewModel.increasePlayer1() },
                    increasePlayer2 = { trucoViewModel.increasePlayer2() },
                )

                RestarPuntos(
                    modifier = Modifier.height(100.dp),
                    decreasePlayer1 = { trucoViewModel.decreasePlayer1() },
                    decreasePlayer2 = { trucoViewModel.decreasePlayer2() },
                )
            }

        }
        TrucoState.SETTING -> {}
    }



}


@Composable
fun Loading(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp),
            color = Orange2,
            backgroundColor = Orange1,
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun Cabecera(
    modifier: Modifier = Modifier,
    player1Name: String = "Nosotros",
    player2Name: String = "Ellos",
    onClickPlayer1:() -> Unit,
    onClickPlayer2:() -> Unit,
) {
    Row (modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){

        Text(
            modifier = Modifier
                .weight(1f)
                .clickable { onClickPlayer1() },
            text = player1Name,
            fontSize = 32.sp,
            color = Orange1,
            textAlign = TextAlign.Center
        )
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Settings,
            tint = Accent,
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .clickable { onClickPlayer2() },
            text = player2Name,
            fontSize = 32.sp,
            color = Orange1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    game: TrucoModelUI,
    increasePlayer1: () -> Unit,
    increasePlayer2: () -> Unit,
) {

    Row(modifier = modifier.background(Background), horizontalArrangement = Arrangement.Center){

        PointsPlayer(
            modifier = Modifier
                .weight(1f)
                .background(Background)
                .clickable { increasePlayer1() },
            playerPoint = game.player1.playerPoint
        )
        Divider(color = Accent, modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp))
        PointsPlayer(
            modifier = Modifier
                .weight(1f)
                .background(Background)
                .clickable { increasePlayer2() },
            playerPoint = game.player2.playerPoint
        )
    }
}

@Composable
fun PointsPlayer(modifier: Modifier = Modifier, playerPoint: Int){

    var pointCount = playerPoint

    Log.i(TAG, "PointsPlayer pointCount: $pointCount")

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))

        for (i in 1 .. 6){

            BoxPuntos(
                Modifier.size(100.dp),
                points = pointCount
            )
            Spacer(modifier = Modifier.weight(1f))

            if (i == 3){
                Divider(
                    color = Accent,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            if (pointCount >= 5) pointCount -= 5 else pointCount = 0
        }


    }
}
@Composable
fun BoxPuntos(modifier: Modifier = Modifier, points: Int){
    val limitAux = if (points > 5) 5 else points
    Box(
        modifier = modifier
    ) {

        val alignmentList = listOf(
            Alignment.CenterStart,
            Alignment.TopCenter,
            Alignment.CenterEnd,
            Alignment.BottomCenter,
            Alignment.Center
        )
        val drawableListList = listOf(
            R.drawable.fosforo_vert,
            R.drawable.fosforo_hor,
            R.drawable.fosforo_vert,
            R.drawable.fosforo_hor,
            R.drawable.fosforo_diag
        )

        for (i in 0 until limitAux){
            Image(modifier = Modifier.align(alignmentList[i]), painter = painterResource(id = drawableListList[i]), contentDescription = "")
        }

    }
}

@Composable
fun RestarPuntos(
    modifier: Modifier = Modifier,
    decreasePlayer1:() -> Unit,
    decreasePlayer2:() -> Unit
    ) {
    Row (modifier = modifier.border(border = BorderStroke(1.dp, Accent))){
        Box (modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clickable { decreasePlayer1() },
            contentAlignment = Alignment.Center){
            Text(text = "-", color = Background)
        }
        Divider(
            color = Accent,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Box (modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .clickable { decreasePlayer2() },
            contentAlignment = Alignment.Center){
            Text(text = "-", color = Background)
        }
    }
}
