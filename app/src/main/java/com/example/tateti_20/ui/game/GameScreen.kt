package com.example.tateti_20.ui.game

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.BlueLink
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2
import com.example.tateti_20.ui.theme.string_cancel
import com.example.tateti_20.ui.theme.string_congratulations
import com.example.tateti_20.ui.theme.string_copided_address
import com.example.tateti_20.ui.theme.string_dead_heat
import com.example.tateti_20.ui.theme.string_exit
import com.example.tateti_20.ui.theme.string_game_finished
import com.example.tateti_20.ui.theme.string_leave_game
import com.example.tateti_20.ui.theme.string_play_again
import com.example.tateti_20.ui.theme.string_player_left_game
import com.example.tateti_20.ui.theme.string_rival_turn
import com.example.tateti_20.ui.theme.string_very_close
import com.example.tateti_20.ui.theme.string_wait
import com.example.tateti_20.ui.theme.string_waiting
import com.example.tateti_20.ui.theme.string_waiting_player
import com.example.tateti_20.ui.theme.string_winner_game
import com.example.tateti_20.ui.theme.string_your_turn
import com.example.tateti_20.ui.theme.string_zeroVictories

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    hallId: String,
    userId: String,
    navigateToHome: () -> Unit
){

    var showDialogCloseGame by remember { mutableStateOf(false) }
    var closerGame by remember { mutableStateOf(false) }
    val uiState by gameViewModel.uiState.collectAsState()
    val game:GameModelUi? by gameViewModel.game.collectAsState()
    val winner:PlayerType by gameViewModel.winner.collectAsState()

    LaunchedEffect(true){ gameViewModel.initGame(userId, hallId) }

    when(uiState) {
        GameViewState.LOADING -> Loading()

        GameViewState.GAME -> {
            Board(game){ position ->
                gameViewModel.onClickItem(position)
            }
        }
        GameViewState.FINISH -> {
            FinishGame(game!!, winner,
                resetGame= { gameViewModel.resetGame() },
                closeGame = {
                    closerGame = true
                    showDialogCloseGame = true
                }
            )
        }
    }

    BackHandler {
        closerGame = true
        showDialogCloseGame = true
    }

    if (showDialogCloseGame){
        AlertCloseGame(
            onClickCancel = { showDialogCloseGame = false },
            onClickExit = {

                gameViewModel.closeGame(navigateToHome)
            }
        )
    }


    if (game?.isFinished == true && !closerGame){
        Dialog(onDismissRequest = {}){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp)
                    .border(2.dp, Orange1, RoundedCornerShape(24.dp))
            ){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = string_game_finished, fontSize = 24.sp, color = Orange1, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = string_player_left_game, fontSize = 16.sp, color = Orange2, fontWeight = FontWeight.Light)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    TextButton(modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 12.dp), onClick = { gameViewModel.closeGame(navigateToHome) }) {
                        Text(text = string_exit, color = Accent)
                    }
                }
            }
        }
    }
}

@Composable
fun AlertCloseGame(onClickCancel: () -> Unit, onClickExit: () -> Unit) {
    Dialog(onDismissRequest = { onClickCancel() }){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp)
                .border(2.dp, Orange1, RoundedCornerShape(24.dp))
        ){
            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = string_wait, fontSize = 24.sp, color = Orange1, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))

                Text(text = string_leave_game, fontSize = 16.sp, color = Orange2, fontWeight = FontWeight.Light)
                Spacer(modifier = Modifier.height(8.dp))

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)){
                    TextButton( onClick = { onClickCancel() }) {
                        Text(text = string_cancel, color = Accent)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(  onClick = { onClickExit() }) {
                        Text(text = string_exit, color = Accent)
                    }
                }
            }
        }
    }
}

@Composable
fun Loading() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp),
            color = Orange2,
            backgroundColor = Orange1,
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun Board(game: GameModelUi?, onClickItem: (Int) -> Unit) {

    if(game == null) return
    if(game.hallId == null) return

    val clipboard: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box (
        Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    clipboard.setText(AnnotatedString(game.hallId))
                    Toast.makeText(context, string_copided_address, Toast.LENGTH_SHORT).show()
                }
            ){
                Text(
                    text = game.hallName,
                    fontSize = 20.sp,
                    color = BlueLink,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = null,
                    tint = BlueLink
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Puntaje(game.player1)
                Spacer(modifier = Modifier.width(130.dp))
                Puntaje(game.player2)
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp))


            Row {
                GameItem(game.board[0]){ onClickItem(0) }
                GameItem(game.board[1]){ onClickItem(1) }
                GameItem(game.board[2]){ onClickItem(2) }
            }
            Row {
                GameItem(game.board[3]){ onClickItem(3) }
                GameItem(game.board[4]){ onClickItem(4) }
                GameItem(game.board[5]){ onClickItem(5) }
            }
            Row {
                GameItem(game.board[6]){ onClickItem(6) }
                GameItem(game.board[7]){ onClickItem(7) }
                GameItem(game.board[8]){ onClickItem(8) }
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp))


            val turno = if(game.isGameReady){
                if (game.player1?.resetGame == true && game.player2?.resetGame == false ){
                    "$string_waiting ${game.player2.userName}"
                } else if(game.player1?.resetGame == false && game.player2?.resetGame == true ){
                    "$string_waiting ${game.player1.userName}"
                } else if (game.isMyTurn) {
                    string_your_turn
                } else {
                    string_rival_turn
                }
            } else {
                string_waiting_player
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = turno, color = Orange1, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(12.dp))
                if(!game.isGameReady || game.player1?.resetGame == true || game.player2?.resetGame == true ){
                    CircularProgressIndicator(
                        color = Orange2,
                        backgroundColor = Orange1,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

}
@Composable
fun GameItem(playerType: PlayerType, onClickItem:()->Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(12.dp)
            .border(BorderStroke(2.dp, Accent))
            .clickable { onClickItem() }
        ,
        contentAlignment = Alignment.Center
    ){
        AnimatedContent(targetState = playerType.simbol, label = "") {
            Text(
                text = it,
                color = if (playerType == PlayerType.FirstPlayer) Orange1 else Orange2,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun FinishGame(game: GameModelUi, winner:PlayerType, resetGame:()-> Unit, closeGame:()->Unit) {
    val currentWinner = when (winner) {
        PlayerType.FirstPlayer -> game.player1?.userName
        PlayerType.SecondPlayer -> game.player2?.userName
        PlayerType.Empty -> string_dead_heat
    }
    val title = if(winner != PlayerType.Empty) string_congratulations else string_very_close

    Box (
        Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = title, fontSize = 34.sp, color = Orange1, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                if(winner != PlayerType.Empty){
                    Text(text = string_winner_game, fontSize = 24.sp, color = Orange1, fontWeight = FontWeight.Bold)
                }
                Text(text = currentWinner.orEmpty(), fontSize = 24.sp, color = Orange2, fontWeight = FontWeight.Bold)

            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp))

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 16.dp)
                .border(2.dp, Orange1, RoundedCornerShape(24.dp))) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Puntaje(game.player1)
                    Spacer(modifier = Modifier.width(130.dp))
                    Puntaje(game.player2)
                }
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp))

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Orange1, contentColor = Accent),
                onClick = { resetGame() }
            ) {
                Text(text = string_play_again)
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(24.dp))

            val waitingPlayer = if(game.player1?.resetGame == true || game.player2?.resetGame == true) {
                if (game.player1?.resetGame == true) "$string_waiting ${game.player2?.userName}" else "$string_waiting ${game.player1?.userName}"
            } else {
                ""
            }


            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = waitingPlayer, color = Orange1, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(12.dp))
                if(waitingPlayer.isNotEmpty()){
                    CircularProgressIndicator(
                        color = Orange2,
                        backgroundColor = Orange1,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Accent, contentColor = Orange1),
                onClick = { closeGame() }
            ) {
                Text(text = string_exit)
            }
        }

    }
}
@Composable
fun Puntaje(player: PlayerModelUi?) {
    val currentVictories = player?.victories?.toString() ?: string_zeroVictories
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        user?.let {
            Text(text = player?.userName.orEmpty(), fontSize = 20.sp, color = Orange1, fontWeight = FontWeight.Normal)
            Text(text = currentVictories, fontSize = 48.sp, color = Orange2, fontWeight = FontWeight.Bold)
//        }
    }
}