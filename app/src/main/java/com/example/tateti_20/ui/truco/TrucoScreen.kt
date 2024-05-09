package com.example.tateti_20.ui.truco

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
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
                Cabecera(modifier = Modifier.height(100.dp).fillMaxWidth())

                Body(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
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
fun Cabecera(modifier: Modifier = Modifier) {
    Row (modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){

        Text(
            modifier = Modifier.weight(1f),
            text = "NOSOTROS",
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
            modifier = Modifier.weight(1f),
            text = "ELLOS",
            fontSize = 32.sp,
            color = Orange1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Body(
    modifier: Modifier = Modifier,
    increasePlayer1: () -> Unit,
    increasePlayer2: () -> Unit,
) {
    Row(modifier = modifier.background(Background), horizontalArrangement = Arrangement.Center){
        Column(modifier = Modifier
            .weight(1f)
            .background(Background)
            .clickable { increasePlayer1() },
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))

            Divider(
                color = Accent,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))


        }
        Divider(
                color = Accent,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
        )
        Column(modifier = Modifier
            .weight(1f)
            .background(Background)
            .clickable { increasePlayer2() },
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))

            Divider(
                color = Accent,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
            BoxPuntos(Modifier.size(100.dp))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun BoxPuntos(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
    ) {
        Image(modifier = Modifier.align(Alignment.TopCenter), painter = painterResource(id = R.drawable.fosforo_hor), contentDescription = "1")
        Image(modifier = Modifier
            .rotate(180f)
            .align(Alignment.CenterStart), painter = painterResource(id = R.drawable.fosforo_vert), contentDescription = "2")
        Image(modifier = Modifier.align(Alignment.CenterEnd), painter = painterResource(id = R.drawable.fosforo_vert), contentDescription = "3")
        Image(modifier = Modifier
            .rotate(180f)
            .align(Alignment.BottomCenter), painter = painterResource(id = R.drawable.fosforo_hor), contentDescription = "4")
        Image(modifier = Modifier.align(Alignment.Center), painter = painterResource(id = R.drawable.fosforo_diag), contentDescription = "5")
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
