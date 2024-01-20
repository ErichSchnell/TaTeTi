package com.example.tateti_20.ui.halls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2

@Composable
fun HallsScreen(
    hallsViewModel: HallsViewModel = hiltViewModel(),
    navigateToMach: (String, String) -> Unit,
    userId: String
) {
    val uiState by hallsViewModel.uiState.collectAsState()
    val listHalls by hallsViewModel.halls.collectAsState()


    when (uiState) {
        HallsViewState.LOADING -> Loading()
        HallsViewState.HALLS -> {
            Halls(listHalls) {
                hallsViewModel.joinGame(it, userId, navigateToMach)
            }
        }
    }
}

//@Preview
@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp),
            color = Orange2,
            backgroundColor = Orange1,
            strokeWidth = 2.dp
        )
    }
}

//@Preview
@Composable
fun Halls(listHalls: List<GameModelUi?>?, onClickHall: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Title()
        ListHalls(listHalls, onClickHall)
    }
}

@Composable
fun ListHalls(listHalls: List<GameModelUi?>?, onClickHall: (String) -> Unit) {
    if (listHalls?.isNotEmpty() == true) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(listHalls) { hall ->
                    ItemHall(hall, onClickHall = onClickHall)
                }
            },
            contentPadding = PaddingValues(16.dp)
        )

    } else {
        Column (Modifier.fillMaxSize()){
            Spacer(modifier = Modifier.weight(1f))
            Row (
                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "there isn't hall available.",
                    fontSize = 18.sp,
                    color = Orange2
                )
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp).padding(start = 8.dp),
                    color = Orange2,
                    backgroundColor = Orange1,
                    strokeWidth = 2.dp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

//@Preview
@Composable
fun ItemHall(hall: GameModelUi?, onClickHall: (String) -> Unit) {

    if (hall == null) return
    if (hall.hallId == null) return

    var joinGameF by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable { joinGameF = true }
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
            .border(2.dp, Orange1, RoundedCornerShape(12.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Background)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = hall.hallName.uppercase(),
                    fontSize = 24.sp,
                    color = Orange2,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))

                if (hall.isPublic) Icon(modifier = Modifier, painter = painterResource(id = R.drawable.ic_lock_open), tint = Orange2, contentDescription = "")
                else Icon(painter = painterResource(id = R.drawable.ic_lock), tint = Orange2, contentDescription = "")
                Spacer(modifier = Modifier.width(20.dp))
            }

            Divider(Modifier.fillMaxWidth(), color = Orange1)

            hall.player1?.userName?.let { name -> NameJugador(name) }
        }
    }
    if (joinGameF){
        InsertPassword(hall, onClickHall = onClickHall){
            joinGameF = it
        }
    }
}
@Composable
fun InsertPassword(hall: GameModelUi, onClickHall: (String) -> Unit, showDialog: (Boolean) -> Unit) {
    var pass by remember { mutableStateOf("") }
    var joinToGameState by remember { mutableStateOf(true) }

    if (hall.isPublic){
        joinToGameState = false
        onClickHall(hall.hallId.orEmpty())
    } else {
        Dialog(onDismissRequest = { joinToGameState = false }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    label = { Text(text = "Password", color = Orange2) },
                    modifier = Modifier,
                    value = pass,
                    onValueChange = { pass = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Orange1,
                        textColor = Accent,
                        focusedBorderColor = Orange1,
                        unfocusedBorderColor = Orange2
                    )
                )
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Orange1,
                        contentColor = Accent
                    ),
                    enabled = (hall.password == pass),
                    onClick = {
                        joinToGameState = false
                        onClickHall(hall.hallId.orEmpty())
                    }
                ) {
                    Text(text = "Join To Game")
                }
            }

        }
    }
    showDialog(joinToGameState)
}

@Composable
fun NameJugador(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = name,
            fontSize = 20.sp,
            color = Accent,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}