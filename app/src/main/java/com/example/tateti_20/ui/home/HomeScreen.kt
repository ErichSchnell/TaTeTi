package com.example.tateti_20.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.Accent
import com.example.tateti_20.ui.theme.Background
import com.example.tateti_20.ui.theme.HalfAccent
import com.example.tateti_20.ui.theme.Orange1
import com.example.tateti_20.ui.theme.Orange2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToMach: (String, String) -> Unit,
    navigateToHalls: (String) -> Unit
) {
    val showToast by homeViewModel.showToast.collectAsState()
    val navigateToHall by homeViewModel.navigateToHall.collectAsState()

    val uiState by homeViewModel.uiState.collectAsState()
    val user by homeViewModel.user.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val context = LocalContext.current

    val googleLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val taks =  GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = taks.getResult(ApiException::class.java)!!
                homeViewModel.loginWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Toast.makeText(context, "Ha ocurrido un error: ${e.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LogoHead()

        when (uiState) {
            HomeViewState.LOADING -> {
                Loading(modifier = Modifier.weight(1f))
            }
            HomeViewState.LOGIN -> {

                Login(
                    modifier = Modifier.weight(1f),
                    loading = loading,
                    onClickLogin = { email, password ->
                        homeViewModel.login(email, password)
                    },
                    onClickSingUp = { homeViewModel.getSingUp() },
                    onClickChangePassword = { email ->
                        homeViewModel.setChangePassword(email)
                                            },
                    onGoogleLoginSelected = {
                        homeViewModel.onGoogleLoginSelected{
                            googleLauncher.launch(it.signInIntent)
                        }
                    }
                )

            }
            HomeViewState.SINGUP -> {
                SingUp(modifier = Modifier.weight(1f), loading = loading) {nickname, email, password ->
                    homeViewModel.singUp(nickname, email, password)
                }
            }

            HomeViewState.HOME -> {
                Home(
                    modifier = Modifier.weight(1f),
                    user = user,
                    onClickHalls = { homeViewModel.viewHalls(navigateToHalls) },
                    onJoinGame = {   homeViewModel.joinGame(it, navigateToMach) },
                    onCreateGame = { hallName, password ->
                        homeViewModel.onCreateGame(hallName, password, navigateToMach)
                   },
                    onClickLogout = { homeViewModel.logout()}
                )
            }
        }
    }

    if(!showToast.isNullOrEmpty()){
        LaunchedEffect(showToast){
            Toast.makeText(context, showToast, Toast.LENGTH_SHORT).show()
            homeViewModel.clearToast()
        }
    }
    if (navigateToHall.state){
        homeViewModel.clearNavigateToHall()
        navigateToMach(navigateToHall.hallId,navigateToHall.userId)
    }
}

@Composable
fun LogoHead() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(4.dp, Orange1, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,

        ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "logo"
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )

    Text(text = "Firebase", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Orange1)

    Text(text = "Ta-Te-Ti", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Orange2)
}

@Composable
fun Loading(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
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


@Composable
fun Home(
    modifier: Modifier,
    user: UserModelUi,
    onCreateGame: (String, String) -> Unit,
    onJoinGame: (String) -> Unit,
    onClickHalls: () -> Unit,
    onClickLogout: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.background(Background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var createGame by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Create Hall",
                    color = if (!createGame) Accent else HalfAccent
                )
                Switch(
                    checked = createGame,
                    onCheckedChange = { cambio -> createGame = cambio },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Orange2,
                        uncheckedThumbColor = Orange2
                    )
                )
                Text(
                    text = "Join Game",
                    color = if (createGame) Accent else HalfAccent
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(2.dp, Orange1, RoundedCornerShape(24.dp))
            ) {

                Column(
                    modifier = Modifier.background(Background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedContent(targetState = createGame, label = "") {
                        when (it) {
                            true -> JoinGame(
                                onJoinGame = onJoinGame, onClickHalls = onClickHalls
                            )

                            false -> CreateGame(onCreateGame)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
            Spacer(modifier = Modifier.height(38.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Victories(user)
                Spacer(modifier = Modifier.width(130.dp))
                Defeats(user)
            }
        }
        Row(
            Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd)
                .clickable { onClickLogout() }, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                tint = Accent,
                contentDescription = "ic_logout"
            )
            Text(text = "Logout", color = Accent, fontSize = 12.sp)
        }
    }
}



@Composable
fun Victories(user: UserModelUi) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Victories", fontSize = 20.sp, color = Orange1, fontWeight = FontWeight.Normal)
        Text(
            text = user.victories.toString(),
            fontSize = 48.sp,
            color = Orange2,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun Defeats(user: UserModelUi) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Defeats", fontSize = 20.sp, color = Orange1, fontWeight = FontWeight.Normal)
        Text(
            text = user.defeats.toString(),
            fontSize = 48.sp,
            color = Orange2,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CreateGame(onCreateGame: (String, String) -> Unit) {
    var nameNewHalls by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column() {
            OutlinedTextField(
                label = { Text(text = "Hall Name", color = Orange2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = nameNewHalls,
                onValueChange = { nameNewHalls = it },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    val icon = if (isPublic) R.drawable.ic_lock_open else R.drawable.ic_lock
                    IconButton(onClick = { isPublic = !isPublic }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = Orange2
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange2
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(targetState = !isPublic, label = "") {
                if(it){
                    OutlinedTextField(
                        label = { Text(text = "Password", color = Orange2) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 4.dp, bottom = 8.dp),
                        value = password,
                        onValueChange = { password = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (isPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_show_password
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = null,
                                    tint = Orange2
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = Orange1,
                            textColor = Accent,
                            focusedBorderColor = Orange1,
                            unfocusedBorderColor = Orange2
                        )
                    )
                }
            }
        }
        


        Button(
            modifier = Modifier.padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Orange1,
                contentColor = Accent
            ),

            enabled = (nameNewHalls.isNotEmpty() && (isPublic || password.length >= 4)),
            onClick = { onCreateGame(nameNewHalls, password) }
        ) {
            Text(text = "Create")
        }


    }
}


@Composable
fun JoinGame(onJoinGame: (String) -> Unit, onClickHalls: () -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            label = { Text(text = "Hall ID", color = Orange2) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = text,
            onValueChange = { text = it },
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
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.padding(end = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),
                onClick = { onJoinGame(text) },
                enabled = text.isNotEmpty()
            ) {
                Text(text = "Join")
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),
                onClick = { onClickHalls() }
            ) {
                Text(text = "Halls")
            }
        }

    }
}




/*
* ------------------ Authetication -----------------
* */
@Composable
fun Login(modifier: Modifier, loading: Boolean,
          onClickLogin: (String, String) -> Unit,
          onClickSingUp: () -> Unit,
          onClickChangePassword: (String) -> Unit,
          onGoogleLoginSelected: () -> Unit
) {
    var email by remember { mutableStateOf("schnellerich@hotmail.com") }
    var password by remember { mutableStateOf("1234566") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Background)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                label = { Text(text = "email", color = Orange2) },
                value = email,
                onValueChange = { email = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                label = { Text(text = "password", color = Orange2) },
                value = password,
                onValueChange = { password = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (isPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_show_password
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
//                            tint = Orange1
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)) {
                Text(
                    text = "Forgot Password?",
                    color = Accent,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { onClickChangePassword(email) }
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Create Account",
                    color = Accent,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { onClickSingUp() }
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),
                onClick = { onClickLogin(email, password) },
                enabled = (email.isNotEmpty() && password.isNotEmpty())
            ) {
                Text(text = "Login")
            }
            Card(
                Modifier
                    .clickable { onGoogleLoginSelected() }
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp), elevation = 12.dp){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(Orange1),
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(modifier = Modifier
                        .size(26.dp)
                        .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_google),
                        tint = Color.Black,
                        contentDescription = "ic_google")
                    Text(text = "Login with Google", color = Accent, fontSize = 14.sp, fontWeight = FontWeight.Light)
                }
            }

        }
        if(loading){
            CircularProgressIndicator(
                modifier = Modifier.size(38.dp),
                color = Orange2,
                backgroundColor = Orange1,
                strokeWidth = 2.dp
            )
        }

    }
}
@Composable
fun SingUp(modifier: Modifier, loading: Boolean, onClickSingUp: (String, String, String) -> Unit) {
    var nickname by remember { mutableStateOf("erich") }
    var email by remember { mutableStateOf("schnellerich@hotmail.com") }
    var password by remember { mutableStateOf("1234566") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Background)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                label = { Text(text = "nickname", color = Orange2) },
                value = nickname,
                onValueChange = { nickname = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                label = { Text(text = "email", color = Orange2) },
                value = email,
                onValueChange = { email = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                label = { Text(text = "password", color = Orange2) },
                value = password,
                onValueChange = { password = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Orange1,
                    textColor = Accent,
                    focusedBorderColor = Orange1,
                    unfocusedBorderColor = Orange1
                ),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (isPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_show_password
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
//                            tint = Orange1
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Orange1,
                    contentColor = Accent
                ),
                onClick = { onClickSingUp(nickname, email, password) },
                enabled = (email.isNotEmpty() && password.isNotEmpty())
            ) {
                Text(text = "SingUp")
            }
        }
        if(loading){
            CircularProgressIndicator(
                modifier = Modifier.size(38.dp),
                color = Orange2,
                backgroundColor = Orange1,
                strokeWidth = 2.dp
            )
        }
    }
}
/*
* ---------------------------------------------------
* */


