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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Title()
        ListHalls(listHalls, onClickHall)
    }
}

@Composable
fun Title() {
    Text(
        modifier = Modifier.padding(24.dp),
        text = "SELECT YOUR FAVORITE HALL",
        fontSize = 28.sp,
        color = Orange1,
        fontWeight = FontWeight.Bold
    )
    Divider(Modifier.fillMaxWidth(), color = Orange2)
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
        Text(text = "there isn't hall available.")
    }
}

//@Preview
@Composable
fun ItemHall(hall: GameModelUi?, onClickHall: (String) -> Unit) {

    if (hall == null) return
    if (hall.hallId == null) return

    Card(
        modifier = Modifier
            .clickable { onClickHall(hall.hallId) }
            .height(100.dp)
            .padding(16.dp)
            .border(2.dp, Orange1, RoundedCornerShape(24.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Background)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.weight(0.25f))
                NameHall(hall.hallName)

                Spacer(modifier = Modifier.weight(1f))
                if (hall.isPublic) Icon(painter = painterResource(id = R.drawable.ic_lock_open), tint = Accent, contentDescription = "")
                else Icon(painter = painterResource(id = R.drawable.ic_lock), tint = Orange2, contentDescription = "")
                Spacer(modifier = Modifier.weight(0.25f))
            }

            Divider(Modifier.fillMaxWidth(), color = Orange1)

            hall.player1?.userName?.let { name -> NameJugador(name) }
        }
    }
}

@Composable
fun NameHall(name: String) {
    Text(
        modifier = Modifier.padding(top = 2.dp),
        text = name,
        fontSize = 24.sp,
        color = Accent,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun NameJugador(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "•",
            fontSize = 34.sp,
            color = Accent,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = name,
            fontSize = 20.sp,
            color = Orange2,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}