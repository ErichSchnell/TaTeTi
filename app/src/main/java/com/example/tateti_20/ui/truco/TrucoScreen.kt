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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.TrucoModelUI
import com.example.tateti_20.ui.model.TypePlayer
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.HalfAccent
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

    var showSetting by remember{ mutableStateOf(false) }
    var showChangeNamePlayer1 by remember{ mutableStateOf(false) }
    var showChangeNamePlayer2 by remember{ mutableStateOf(false) }

    when(uiState){
        TrucoState.LOADING -> Loading(Modifier.fillMaxSize())
        TrucoState.READY -> {
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

                TrucoAnnotator(
                    game = game,

                    onClickSetting = { showSetting = true },

                    onChangeNamePlayer1 = { showChangeNamePlayer1 = true },
                    increasePlayer1 = { trucoViewModel.increasePlayer1() },
                    decreasePlayer1 = { trucoViewModel.decreasePlayer1() },

                    onChangeNamePlayer2 = { showChangeNamePlayer2 = true },
                    increasePlayer2 = { trucoViewModel.increasePlayer2() },
                    decreasePlayer2 = { trucoViewModel.decreasePlayer2() },
                )

                if(showSetting){
                    DialogSetting(
                        pointCurrent = game.pointLimit,
                        onDismissRequest = { showSetting = false },
                        onClickResetAnnotator = {
                            trucoViewModel.setAnnotator(it)
                            showSetting = false
                        }
                    )
                }
                if (showChangeNamePlayer1){
                    DialogChangeName(
                        onDismissRequest = { showChangeNamePlayer1 = false },
                        onChangeName = {
                            trucoViewModel.changeNamePlayer1(it)
                            showChangeNamePlayer1 = false
                        }
                    )
                }
                if (showChangeNamePlayer2){
                    DialogChangeName(
                        onDismissRequest = { showChangeNamePlayer2 = false },
                        onChangeName = {
                            trucoViewModel.changeNamePlayer2(it)
                            showChangeNamePlayer2 = false
                        }
                    )
                }
                if (game.winner != TypePlayer.VACIO) {
                    DialogFinishGame(
                        winner = if (game.winner == TypePlayer.PLAYER1) game.player1.playerName else game.player2.playerName,
                        onClickCancel = { trucoViewModel.clearWinner() },
                        onClickResetAnnotator = {
                            trucoViewModel.setAnnotator(game.pointLimit)
                        }
                    )
                }
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
fun TrucoAnnotator(
    game: TrucoModelUI,
    onClickSetting: () -> Unit,
    onChangeNamePlayer1: () -> Unit,
    onChangeNamePlayer2: () -> Unit,
    increasePlayer1: () -> Unit,
    decreasePlayer1:() -> Unit,
    increasePlayer2: () -> Unit,
    decreasePlayer2:() -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background), horizontalAlignment = Alignment.CenterHorizontally) {
        Cabecera(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(),
            player1Name = game.player1.playerName,
            player2Name = game.player2.playerName,
            onClickSetting = { onClickSetting() },
            onClickPlayer1 = { onChangeNamePlayer1() },
            onClickPlayer2 = { onChangeNamePlayer2() }
        )

        Body(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            game = game,
            increasePlayer1 = { increasePlayer1() },
            increasePlayer2 = { increasePlayer2() },
        )

        RestarPuntos(
            modifier = Modifier.height(100.dp),
            decreasePlayer1 = { decreasePlayer1() },
            decreasePlayer2 = { decreasePlayer2() },
        )
    }
}
@Composable
fun Cabecera(
    modifier: Modifier = Modifier,
    player1Name: String = "Nosotros",
    player2Name: String = "Ellos",
    onClickSetting: () -> Unit,
    onClickPlayer1: () -> Unit,
    onClickPlayer2: () -> Unit,
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
            modifier = Modifier
                .size(36.dp)
                .clickable { onClickSetting() },
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

@Composable
fun DialogSetting(pointCurrent:Int ,onDismissRequest:() -> Unit, onClickResetAnnotator:(Int) -> Unit){
    val pointList = listOf(12, 15, 24, 30)
    var selectedPoint by remember { mutableStateOf(pointCurrent) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).background(HalfAccent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "POINTS")

            pointList.forEach { points ->
                Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedPoint == points,
                        onClick = { selectedPoint = points }
                    )
                    Text(text = "$points")
                }
            }
            Button(
                modifier = Modifier.padding(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),onClick = { onClickResetAnnotator(selectedPoint) }) {
                Text(text = "RESET")
            }
        }
    }
}


@Composable
fun DialogChangeName(onDismissRequest:() -> Unit, onChangeName:(String) -> Unit){
    var name by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                label = { Text(text = "New Name", color = Orange2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = name,
                onValueChange = { name = it },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange2
                )
            )
            Button(
                modifier = Modifier.padding(end = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),
                onClick = {
                    onChangeName(name)
                },
                enabled = name.isNotEmpty() && name.length < 12
            ) {
                Text(text = "Update Name")
            }
        }
    }
}




@Composable
fun DialogFinishGame(winner:String, onClickCancel:() -> Unit, onClickResetAnnotator:() -> Unit){
    var name by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {}) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "WINNER TEAM $winner")
            Row (modifier = Modifier.padding(6.dp)) {
                Button(
                    modifier = Modifier.padding(end = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Orange1,
                        contentColor = Accent
                    ),onClick = {onClickCancel()}) {
                    Text(text = "Cancel")
                }
                Button(
                    modifier = Modifier.padding(end = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Orange1,
                        contentColor = Accent
                    ),onClick = {onClickResetAnnotator()}) {
                    Text(text = "Reset")
                }
            }
        }
    }
}


//@Composable
//fun DialogSetting(onDismissRequest:()->Unit, onClickResetAnnotator:(Boolean) -> Unit){
//
//    var playerName by remember{ mutableStateOf("") }
//
//
//}
