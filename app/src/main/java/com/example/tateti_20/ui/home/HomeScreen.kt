package com.example.tateti_20.ui.home

import android.app.Activity
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToMach: (String, String) -> Unit,
    navigateToHalls: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val showModalDrawer = rememberDrawerState(initialValue = DrawerValue.Closed)

    val showToast by homeViewModel.showToast.collectAsState()
    val navigateToHall by homeViewModel.navigateToHall.collectAsState()

    val uiState by homeViewModel.uiState.collectAsState()
    val user by homeViewModel.user.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val context = LocalContext.current

    //PerfilImage
    val resultUri by homeViewModel.uriImage.collectAsState()
    var uri: Uri? by remember{ mutableStateOf(null) }

    /*val intenCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it && uri?.path?.isNotEmpty() == true){
            homeViewModel.uploadAndGetImage(uri!!){newUri -> homeViewModel.setUriImage(newUri)}
        }
    }*/

    val intenGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if(it?.path?.isNotEmpty() == true){
            homeViewModel.uploadAndGetImage(it){newUri -> homeViewModel.setUriImage(newUri)}
        }
    }

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

        when (uiState) {
            HomeViewState.LOADING -> {
                Loading(modifier = Modifier.weight(1f))
            }
            HomeViewState.LOGIN -> {
                LogoHead()
                Login(
                    modifier = Modifier.weight(1f),
                    loading = loading,
                    onClickLogin = { email, password ->
                        homeViewModel.login(email, password)
//                        coroutineScope.launch { showModalDrawer.close() }
                    },
                    onClickSingUp = { email, password, verifyPassword ->
                        homeViewModel.singUp(email, password, verifyPassword)
//                        coroutineScope.launch { showModalDrawer.close() }
                    },
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

            HomeViewState.HOME -> {
                ModalDrawer(
                    drawerState = showModalDrawer,
                    drawerBackgroundColor = Background,
                    drawerContent = {
                        ModalDrawerProfile(
                            user = user,
                            isLoading = loading,
                            uriImage = resultUri,
                            editUserName = {homeViewModel.editUserName(it)},
                            onClickLogout = {
                                coroutineScope.launch { showModalDrawer.close() }
                                homeViewModel.logout()
                            },
                            onClickIntentCameraLauncher = {

                            },
                            onClickIntentGalleryLauncher = {
                                intenGalleryLauncher.launch("image/*")
                            }
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Background), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LogoHead{
                            coroutineScope.launch {
                                showModalDrawer.open()
                            }
                        }
                        Home(
                            modifier = Modifier.weight(1f),
                            user = user,
                            resultUri = resultUri,
                            onClickHalls = { homeViewModel.viewHalls(navigateToHalls) },
                            onJoinGame = {   homeViewModel.joinGame(it, navigateToMach) },
                            onCreateGame = { hallName, password ->
                                homeViewModel.onCreateGame(hallName, password, navigateToMach)
                            },
                            onClickUserName = { coroutineScope.launch { showModalDrawer.open() } }
                        )
                    }

                }
                LoadingDate(loading)
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
fun ModalDrawerProfile(
    user: UserModelUi,
    isLoading: Boolean,
    uriImage: Uri?,
    editUserName: (String) -> Unit,
    onClickLogout: () -> Unit,
    onClickIntentCameraLauncher:()->Unit,
    onClickIntentGalleryLauncher:()->Unit,
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(Background)
    ) {
        ProfilePhoto(
            Modifier.align(Alignment.CenterHorizontally),
            isLoading,
            uriImage,
            onClickIntentCameraLauncher = onClickIntentCameraLauncher,
            onClickIntentGalleryLauncher = onClickIntentGalleryLauncher,
        )
        EditPerfil(user, editUserName = editUserName)
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))
        Logout(Modifier.align(Alignment.CenterHorizontally)) { onClickLogout() }
    }
}

@Composable
fun ProfilePhoto(
    modifier: Modifier,
    isLoading: Boolean,
    uriImage: Uri?,
    onClickIntentCameraLauncher:()->Unit,
    onClickIntentGalleryLauncher:()->Unit,

    ) {
    var showProfilePhoto by remember{ mutableStateOf(false) }
    var changeProfilePhoto by remember{ mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(200.dp)
            .clip(CircleShape)
            .border(2.dp, Orange1, CircleShape)
            .clickable { showProfilePhoto = true },
        contentAlignment = Alignment.Center
    ){
        if (isLoading) {
            Loading(modifier)
        } else {
            if (uriImage != null) {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    model = uriImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    imageVector = Icons.Default.AccountCircle, contentDescription = null
                )
            }
        }
    }

    if (showProfilePhoto){
        Dialog(onDismissRequest = { showProfilePhoto = false }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (uriImage != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(300.dp),
                        model = uriImage,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        modifier = Modifier.size(200.dp),
                        imageVector = Icons.Default.AccountCircle, contentDescription = null
                    )
                }
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            showProfilePhoto = false
                            onClickIntentGalleryLauncher()
                        },
                    painter = painterResource(id = R.drawable.ic_gallery), contentDescription = null,
                    colorFilter = ColorFilter.tint(Orange2)
                )
            }
        }
    }
}

@Composable
fun EditPerfil(user: UserModelUi, editUserName:(String) -> Unit) {
    var editName by remember { mutableStateOf(false) }
    var currentUserName by remember { mutableStateOf(user.userName) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(end = 8.dp),
            imageVector = Icons.Default.AccountCircle, contentDescription = null
        )
        Text(text = user.userName)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable { editName = !editName },
            imageVector = Icons.Filled.Edit, contentDescription = null
        )
    }
    if (editName){
        Dialog(
            onDismissRequest = { editName = false },
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    label = { Text(text = "New Username", color = Orange2) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = currentUserName,
                    onValueChange = { currentUserName = it },
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
                        editUserName(currentUserName)
                        editName = false
                    },
                    enabled = currentUserName != user.userName
                ) {
                    Text(text = "Update Username")
                }
            }
        }
    }
}
@Composable
fun Perfil(user: UserModelUi, resultUri: Uri?, onClickUserName: () -> Unit) {
    var editName by remember { mutableStateOf(false) }
    var currentUserName by remember { mutableStateOf(user.userName) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClickUserName() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        if (resultUri != null) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                model = resultUri,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        } else {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                imageVector = Icons.Default.AccountCircle, contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = user.userName, color = Accent, fontSize = 14.sp)
    }
}
@Composable
fun Logout(modifier: Modifier, onClickLogout: () -> Unit) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
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

@Composable
fun LogoHead(onClickLogo:() -> Unit = {}) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(4.dp, Orange1, RoundedCornerShape(24.dp))
            .clickable { onClickLogo() },
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
    resultUri: Uri?,
    onCreateGame: (String, String) -> Unit,
    onClickHalls: () -> Unit,
    onClickUserName: () -> Unit,
    onJoinGame: (String) -> Unit
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


            Perfil(user = user, resultUri, onClickUserName = onClickUserName)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(28.dp))
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

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(28.dp))
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
            Spacer(modifier = Modifier.height(18.dp))


            
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
          onClickSingUp: (String, String, String) -> Unit,
          onClickChangePassword: (String) -> Unit,
          onGoogleLoginSelected: () -> Unit
) {
    var createUser by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("schnellerich@hotmail.com") }
    var password by remember { mutableStateOf("111111") }
    var verifyPassword by remember { mutableStateOf("111111") }
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
            AnimatedContent(targetState = createUser, label = "") {
                if(it){
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        label = { Text(text = "confirm password", color = Orange2) },
                        value = verifyPassword,
                        onValueChange = { verifyPassword = it },
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
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = "Create Account",
                    color = Accent,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { createUser = !createUser }
                )
                Checkbox(checked = createUser, onCheckedChange = { createUser = it })
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Forgot Password?",
                    color = Accent,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { onClickChangePassword(email) }
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
                onClick = {
                    if (createUser) onClickSingUp(email, password, verifyPassword)
                    else onClickLogin(email, password)
                },
                enabled = (email.isNotEmpty() && password.isNotEmpty() && (verifyPassword.isNotEmpty() || !createUser))
            ) {
                if (createUser) Text(text = "Create User")
                else Text(text = "Login")
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
        LoadingDate(loading)

    }
}

@Composable
fun LoadingDate(loading:Boolean = false) {
    if(loading){
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp),
            color = Orange2,
            backgroundColor = Orange1,
            strokeWidth = 2.dp
        )
    }
}
/*
* ---------------------------------------------------
* */


/*
*
* */

private fun generateFile(): File {
    val name = "PhotoCompose_" + SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
    return File.createTempFile(name,".jpg")
}
/*
*
* */

