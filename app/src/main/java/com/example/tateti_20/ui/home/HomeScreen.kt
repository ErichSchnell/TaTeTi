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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.tateti_20.R
import com.example.tateti_20.ui.core.ShimmerHome
import com.example.tateti_20.ui.core.ShimmerModalDrawer


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel(), navigateToMach: (String, String) -> Unit, navigateToHalls: (String) -> Unit) {


    val coroutineScope = rememberCoroutineScope()
    val showModalDrawer = rememberDrawerState(initialValue = DrawerValue.Closed)

    val showToast by homeViewModel.showToast.collectAsState()
    val navigateToHall by homeViewModel.navigateToHall.collectAsState()

    val uiState by homeViewModel.uiState.collectAsState()
    val user by homeViewModel.user.collectAsState()
    val loading by homeViewModel.loading.collectAsState()
    val context = LocalContext.current

    //ProfileImage
    val resultUri by homeViewModel.uriImage.collectAsState()
//    var uri: Uri? by remember{ mutableStateOf(null) }

    /*val intenCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it && uri?.path?.isNotEmpty() == true){
            homeViewModel.uploadAndGetImage(uri!!){newUri -> homeViewModel.setUriImage(newUri)}
        }
    }*/

    val intenGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if(it?.path?.isNotEmpty() == true){
            homeViewModel.uploadProfileImage(it)
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
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            HomeViewState.LOADING -> { Loading(modifier = Modifier
                .weight(1f)
                .background(Background)) }
            HomeViewState.LOGIN -> {
                LogoHead()

                Login(
                    isLoading = loading,
                    modifier = Modifier.weight(1f),
                    onClickLogin = { email, password -> homeViewModel.login(email, password)},
                    onClickSingUp = { email, password, verifyPassword -> homeViewModel.singUp(email, password, verifyPassword) },
                    onClickChangePassword = { email -> homeViewModel.setChangePassword(email) },
                    onGoogleLoginSelected = { homeViewModel.onGoogleLoginSelected{
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
                            isLoading = loading,
                            user = user,
                            resultUri = resultUri,
                            onClickHalls = { homeViewModel.viewHalls(navigateToHalls) },
                            onJoinGame = {   homeViewModel.joinGame(it, navigateToMach) },
                            onCreateGame = { hallName, password ->
                                homeViewModel.onCreateGame(hallName, password)
                            },
                            onClickUserName = { coroutineScope.launch { showModalDrawer.open() } }
                        )
                    }

                }
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

//    LoadingDate(Modifier.size(38.dp), loading)
}

@Composable
fun ModalDrawerProfile(
    user: UserModelUi,
    isLoading: Boolean,
    uriImage: Uri?,
    editUserName: (String) -> Unit,
    onClickLogout: () -> Unit,
    onClickIntentGalleryLauncher:()->Unit,
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)
        .background(Background)
    ) {
        ShimmerModalDrawer(
            isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            ModalProfilePhoto(
                Modifier.align(Alignment.CenterHorizontally),
                uriImage,
                onClickIntentGalleryLauncher = onClickIntentGalleryLauncher,
            )
            Spacer(modifier = Modifier.height(12.dp))

            EditProfile(user, editUserName = editUserName)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))

            Logout(Modifier.align(Alignment.CenterHorizontally)) { onClickLogout() }
        }
    }
}

@Composable
fun ModalProfilePhoto(
    modifier: Modifier,
    uriImage: Uri?,
    onClickIntentGalleryLauncher:()->Unit,
) {
    var showProfilePhoto by remember{ mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(200.dp)
            .clip(CircleShape)
            .border(2.dp, Orange1, CircleShape)
            .clickable { showProfilePhoto = true },
        contentAlignment = Alignment.Center
    ){
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
fun EditProfile(user: UserModelUi, editUserName:(String) -> Unit) {
    var editName by remember { mutableStateOf(false) }
    var currentUserName by remember { mutableStateOf(user.userName) }
    Row (modifier = Modifier
        .fillMaxWidth()
        .clickable { editName = !editName }, horizontalArrangement = Arrangement.Center){
        Text(text = user.userName)
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
fun ProfileData(user: UserModelUi, resultUri: Uri?, onClickUserName: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .padding(8.dp)
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
        Text(text = user.userName, color = Accent, fontSize = 18.sp, fontWeight = FontWeight.Normal)
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

    Spacer(modifier = Modifier.height(36.dp))

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

//    Text(text = "Firebase", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Orange1)

    Text(text = "Ta-Te-Ti", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Orange2)
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
fun Home(
    modifier: Modifier,
    isLoading: Boolean,
    user: UserModelUi,
    resultUri: Uri?,
    onCreateGame: (String, String) -> Unit,
    onClickHalls: () -> Unit,
    onClickUserName: () -> Unit,
    onJoinGame: (String) -> Unit
) {
    Column(modifier = modifier.background(Background), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        var createGame by remember { mutableStateOf(false) }

        ShimmerHome(isLoading = isLoading) {
            Divider(modifier = Modifier.fillMaxWidth())
            ProfileData(user = user, resultUri, onClickUserName = onClickUserName)
            Divider(modifier = Modifier.fillMaxWidth())
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
        Column {
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
                        onValueChange = {pass -> password = pass },
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


/*private fun generateFile(): File {
    val name = "PhotoCompose_" + SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
    return File.createTempFile(name,".jpg")
}*/
/*
*
* */

