package com.example.tateti_20.ui.game

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.game.GameViewModel
import com.example.tateti_20.ui.game.GameViewState
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.BlueLink
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    hallId: String,
    userId: String
){

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
                closeGame = { gameViewModel.closeGame() }
            )
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
                    Toast.makeText(context, "Copiado!", Toast.LENGTH_SHORT).show()
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
                    contentDescription = "ic_copy",
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
                if (game.isMyTurn) {
                    "You Turn"
                } else {
                    "Rival Turn"
                }
            } else {
                "Waiting Player"
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = turno, color = Orange1, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(12.dp))
                if(!game.isGameReady){
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
        PlayerType.Empty -> "EMPATE"
    }
    val title = if(winner != PlayerType.Empty) "¡FELICITACIONES!" else "¡MUY CERCA!"

    Box (
        Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = title, fontSize = 44.sp, color = Orange1, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                if(winner != PlayerType.Empty){
                    Text(text = "WINNER: ", fontSize = 24.sp, color = Orange1, fontWeight = FontWeight.Bold)
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
                Text(text = "Play Again")
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(24.dp))

            val waitingPlayer = if(game.player1?.resetGame == true || game.player2?.resetGame == true) {
                if (game.player1?.resetGame == true) "Waiting ${game.player2?.userName}" else "Waiting ${game.player1?.userName}"
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
                Text(text = "Exit")
            }
        }

    }
}
@Composable
fun Puntaje(player: PlayerModelUi?) {
    val currentVictories = player?.victories?.toString() ?: "0"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        user?.let {
            Text(text = player?.userName.orEmpty(), fontSize = 20.sp, color = Orange1, fontWeight = FontWeight.Normal)
            Text(text = currentVictories, fontSize = 48.sp, color = Orange2, fontWeight = FontWeight.Bold)
//        }
    }
}