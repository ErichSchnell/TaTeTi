package com.example.tateti_20.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.data.network.AuthService
import com.example.tateti_20.data.network.FirebaseStorageService
import com.example.tateti_20.domain.CreateNewGame
import com.example.tateti_20.domain.CreateNewUser
import com.example.tateti_20.domain.GetLocalProfilePhoto
import com.example.tateti_20.domain.GetLocalProfilePhotoState
import com.example.tateti_20.domain.GetLocalUserId
import com.example.tateti_20.domain.GetUser
import com.example.tateti_20.domain.SaveLocalProfilePhoto
import com.example.tateti_20.domain.SaveLocalUserId
import com.example.tateti_20.domain.SetProfilePhotoState
import com.example.tateti_20.domain.UpdateUser
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.string_email_incorrect
import com.example.tateti_20.ui.theme.string_email_pass_incorrect
import com.example.tateti_20.ui.theme.string_email_sent
import com.example.tateti_20.ui.theme.string_email_used
import com.example.tateti_20.ui.theme.string_exception_no_registrada
import com.example.tateti_20.ui.theme.string_insert_email
import com.example.tateti_20.ui.theme.string_log
import com.example.tateti_20.ui.theme.string_pass_same
import com.example.tateti_20.ui.theme.string_pass_short
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


private const val TAG = string_log

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveLocalUserId: SaveLocalUserId,
    private val getLocalUserId: GetLocalUserId,
    private val saveLocalProfilePhoto: SaveLocalProfilePhoto,
    private val getLocalProfilePhoto: GetLocalProfilePhoto,
    private val setProfilePhoto: SetProfilePhotoState,
    private val getLocalProfilePhotoState: GetLocalProfilePhotoState,
//
    private val createNewGame: CreateNewGame,
//    private val updateGame: UpdateGame,
//
    private val createNewUser: CreateNewUser,
    private val getUser: GetUser,
    private val setUser: UpdateUser,
//    private val joinToUser: JoinToUser,

    private val authService: AuthService,
    private val storageService: FirebaseStorageService
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.LOADING)
    val uiState: StateFlow<HomeViewState> = _uiState

    private val _showToast = MutableStateFlow<String?>(null)
    val showToast: StateFlow<String?> = _showToast

    private val _navigateToHall = MutableStateFlow(NavigateToHall())
    val navigateToHall: StateFlow<NavigateToHall> = _navigateToHall

    private val _user = MutableStateFlow(UserModelUi())
    val user: StateFlow<UserModelUi> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _uriImage = MutableStateFlow<Uri?>(null)
    val uriImage: StateFlow<Uri?> = _uriImage


    /*
            *-------------- CONTROL DE LOGIN / RECUPERAR USER ----------------
            */
    init {
        viewModelScope.launch {
            if (authService.isUserLogged()) {
                try {

                    val userId = withContext(Dispatchers.IO) { async { getLocalUserId() }.await() }
                    Log.d(TAG, "userId: $userId")

                    if (userId.isNotEmpty()){
                        loadUser(userId){logout()}

                        _uiState.value = HomeViewState.HOME
                    } else logout()



                } catch (e:Exception){
                    Log.e(TAG, "getLocalUserId(): ${e.message}")
                }

            } else {
                _uiState.value = HomeViewState.LOGIN
            }
        }
    }
    /*
            *--------------------------------
            */


    /*
        *-------------- GAME ----------------
        */
    fun onCreateGame(hallName: String, password: String) {
        val newGame = getNewGame(hallName = hallName, password = password)

        viewModelScope.launch {
            _loading.value = true
            try {
                val hallId = withContext(Dispatchers.IO) {
                    async { createNewGame(newGame.toModelData()) }.await()
                }

                if (hallId.isNotEmpty()) {
                    updateUser(_user.value.copy(lastHall = hallId))
                    Log.i(TAG, "Sala creada: $hallId \n $newGame")
                }
            } catch (e: Exception) {
                Log.i(TAG, "exploto todo: ${e.message}")
            }
            _loading.value = false
        }
    }

    private fun getNewGame(hallId: String? = null, hallName: String, password: String) =
        GameModelUi(
            hallId = hallId,
            hallName = hallName,

            board = List(9) { PlayerType.Empty },

            player1 = _user.value.toPlayer(PlayerType.FirstPlayer),
            player2 = null,
            playerTurn = _user.value.toPlayer(PlayerType.FirstPlayer),

            isPublic = password.isEmpty(),
            password = password,
            isFinished = false,
            isVisible = true,
            winner = 0
        )

    fun joinGame(hallId: String, navigateToMach: (String, String) -> Unit) {
        navigateToMach(hallId, _user.value.userId)
    }
    /*
        *--------------------------------
        */

    fun viewHalls(navigateToHalls: (String) -> Unit) {
        _loading.value = true
        navigateToHalls(_user.value.userId)
        _loading.value = false
    }


    /*
    *----------- AUTETICATION -------------
    */

    /*
    *-------Email-------
    */
    fun singUp(email: String, password: String, verifyPassword: String) {
        if (password == verifyPassword) {
            viewModelScope.launch {
                _loading.value = true
                try {
                    val fireUser = withContext(Dispatchers.IO) { async { authService.register(email, password) }.await() }
                    printFireUser(fireUser)

                    if (fireUser?.email?.isNotEmpty() == true){
                        createUser(
                            userId = fireUser.uid,
                            userEmail = fireUser.email.orEmpty(),
                            userName= "random${(0..100000).random()}",
                            photoUrl = fireUser.photoUrl
                        )
                        saveLocalUserId(fireUser.uid)
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "authService.register(email, password): ${e.message}")
                    Log.e(TAG, "authService.register(email, password): ${e.cause}")

                    when (e.message) {
                        "The email address is badly formatted." -> {
                            _showToast.value = string_email_incorrect
                        }
                        "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> {
                            _showToast.value = string_email_pass_incorrect
                        }
                        "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                            _showToast.value = string_pass_short
                        }
                        "The email address is already in use by another account." -> {
                            _showToast.value = string_email_used
                        }
                        else -> {
                            _showToast.value = string_exception_no_registrada
                        }
                    }

                }
                _loading.value = false
            }
        } else {
            _showToast.value = string_pass_same
        }

    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val fireUser = withContext(Dispatchers.IO) { async { authService.login(email, password) }.await() }
                printFireUser(fireUser)

                if (fireUser?.email?.isNotEmpty() == true){
                    loadUser(fireUser.uid){logout()}

                    saveLocalUserId(fireUser.uid)
                    _uiState.value = HomeViewState.HOME
                }

            } catch (e: Exception) {
                Log.e(TAG, "authService.login(email, password): ${e.message}")
                when (e.message) {
                    "The email address is badly formatted." -> {
                        _showToast.value = string_email_incorrect
                    }
                    "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> {
                        _showToast.value = string_email_pass_incorrect
                    }
                    else -> {
                        _showToast.value = string_exception_no_registrada
                    }
                }

            }
            _loading.value = false
        }
    }

    fun setChangePassword(email: String) {
        if (email.isNotEmpty()) {
            viewModelScope.launch {
                _loading.value = true
                try {
                    val changePass = withContext(Dispatchers.IO) { async { authService.changePassword(email) }.await() }
                    Log.e(TAG, "changePass: $changePass")

                    if (changePass) { _showToast.value = string_email_sent }

                } catch (e: Exception) {
                    Log.e(TAG, "${e.message}")
                    _showToast.value = string_email_incorrect
                }
                _loading.value = false
            }
        } else {
            _showToast.value = string_insert_email
        }
    }

    /*
    *------- Google ---------
    */
    fun onGoogleLoginSelected(googleLauncherLogin: (GoogleSignInClient) -> Unit) {
        val gsc = authService.getGoogleClient()
        googleLauncherLogin(gsc)
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    async { authService.loginWithGoogle(idToken) }.await()
                }
                Log.i(TAG, "loginWithGoogle result: ${result}")
                result?.let {
                    loadUser(it.uid) {
                        createUser(it.uid, it.email.orEmpty(), it.displayName.orEmpty(), it.photoUrl)
                    }
                    _uiState.value = HomeViewState.HOME
                    saveLocalUserId(it.uid)
                }

            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
            _loading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                authService.logout()
                saveLocalUserId("")
                saveLocalProfilePhoto(Uri.parse(""))
                setProfilePhoto(false)
            }
        }
        _uiState.value = HomeViewState.LOGIN
    }

    /*
    *--------- USER  ---------
    */
    private fun loadUser(serverUserId: String, createUser: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val userAux = withContext(Dispatchers.IO) {
                    getUser(serverUserId)
                }
                printFireUser(userAux)

                if (userAux.userEmail.isNotEmpty()) {
                    _user.value = userAux
                    if (_user.value.profilePhoto) loadProfilePhoto()
                } else {
                    createUser()
                }

            } catch (e: Exception) {
                Log.e(TAG, "getUser(serverUserId): ${e.message}")
                logout()
            }
        }
    }

    private fun createUser(userId: String, userEmail: String, userName: String, photoUrl: Uri?) {
        val newUser = UserModelUi(userId = userId, userEmail = userEmail, userName = userName)
        printFireUser(newUser)

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { createNewUser(newUser) }

                if (result) {
                    _user.value = newUser
                    if (photoUrl != null){
                        withContext(Dispatchers.IO) {
                            async { storageService.uploadImage(user = newUser,uri = photoUrl)}.await()
                        }
                    }
                    _uiState.value = HomeViewState.HOME
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }

        }
    }

    private fun updateUser(user: UserModelUi) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    async { setUser(user) }.await()
                }

                if (result) {
                    _user.value = user
                    _navigateToHall.value = NavigateToHall(true, user.lastHall, user.userId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }
    }


    fun clearToast() {
        _showToast.value = ""
    }

    fun clearNavigateToHall() {
        _navigateToHall.value = _navigateToHall.value.copy(state = false)
    }

    fun editUserName(newUserName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    async { setUser(_user.value.copy(userName = newUserName)) }.await()
                }

                if (result) {
                    _user.value = user.value.copy(userName = newUserName)
//                    _navigateToHall.value = NavigateToHall(true, user.lastHall, user.userId)
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
            _loading.value = false
        }
    }

    /*
        *---------------------------------------
        */


    /*
    * ----------------- STORAGE-------------------
    * */
    private fun loadProfilePhoto() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val localProfilePhotoState = withContext(Dispatchers.IO) { async { getLocalProfilePhotoState() }.await() }
                    Log.d(TAG, "localProfilePhoto: $localProfilePhotoState")

                if (localProfilePhotoState){
                    val localProfilePhoto = withContext(Dispatchers.IO) { async { getLocalProfilePhoto() }.await() }
                    Log.d(TAG, "localProfilePhoto: $localProfilePhoto")

                    if (localProfilePhoto.toString() != "") {
                        Log.e(TAG, "localProfilePhoto: $localProfilePhoto")
                        _uriImage.value = localProfilePhoto
                    }
                } else {
                    _uriImage.value = withContext(Dispatchers.IO) { async { storageService.getProfilePhoto(_user.value.userEmail)}.await() }
                }

            } catch (e: Exception) {
                Log.e(TAG, "getLocalProfilePhoto(): ${e.message}")
            }
            _loading.value = false
        }
    }

    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _user.value = _user.value.copy(profilePhoto = true)
                withContext(Dispatchers.IO){ async { storageService.uploadImage(_user.value, uri) }.await()}
                withContext(Dispatchers.IO){ async { setUser(_user.value) }.await()}
                setProfilePhoto(true)
                saveLocalProfilePhoto(uri)
                _uriImage.value = uri
            } catch (e: Exception) {
                Log.i(TAG, e.message.orEmpty())
            }
            _loading.value = false
        }
    }

    fun openAnnotator(navigateToAnnotator: (String) -> Unit) {

        navigateToAnnotator(_user.value.userEmail)
    }

    /*
        *---------------------------------------
        */

}



fun printFireUser(fireUser: FirebaseUser?) {
    Log.d(TAG, "------------------- FireUser -------------------")
    Log.d(TAG, "fireUser.uid: ${fireUser?.uid}")
    Log.d(TAG, "fireUser.email: ${fireUser?.email}")
    Log.d(TAG, "fireUser.isAnonymous: ${fireUser?.isAnonymous}")
    Log.d(TAG, "fireUser.metadata: ${fireUser?.metadata}")
    Log.d(TAG, "fireUser.multiFactor: ${fireUser?.multiFactor}")
    Log.d(TAG, "fireUser.providerData: ${fireUser?.providerData}")
    Log.d(TAG, "fireUser.tenantId: ${fireUser?.tenantId}")
    Log.d(TAG, "fireUser.photoUrl: ${fireUser?.photoUrl}")
    Log.d(TAG, "fireUser.phoneNumber: ${fireUser?.phoneNumber}")
    Log.d(TAG, "--------------------------------------")
}
fun printFireUser(User: UserModelUi?) {
    Log.d(TAG, "------------------- User -------------------")
    Log.d(TAG, "User.userId: ${User?.userId}")
    Log.d(TAG, "User.userEmail: ${User?.userEmail}")
    Log.d(TAG, "User.userName: ${User?.userName}")
    Log.d(TAG, "User.defeats: ${User?.defeats}")
    Log.d(TAG, "User.victories: ${User?.victories}")
    Log.d(TAG, "User.lastHall: ${User?.lastHall}")
    Log.d(TAG, "User.profilePhoto: ${User?.profilePhoto}")
    Log.d(TAG, "--------------------------------------")
}

sealed class HomeViewState {
    object LOADING : HomeViewState()
    object LOGIN : HomeViewState()
    object HOME : HomeViewState()
//    object SINGUP : HomeViewState()
}

data class NavigateToHall(
    val state: Boolean = false,
    val hallId: String = "",
    val userId: String = ""
)