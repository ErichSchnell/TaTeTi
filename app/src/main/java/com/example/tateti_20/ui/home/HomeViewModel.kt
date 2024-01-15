package com.example.tateti_20.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.data.network.AuthService
import com.example.tateti_20.domain.CreateNewGame
import com.example.tateti_20.domain.CreateNewUser
import com.example.tateti_20.domain.GetLocalUserId
import com.example.tateti_20.domain.GetUser
import com.example.tateti_20.domain.SaveLocalUserId
import com.example.tateti_20.domain.UpdateUser
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveLocalUserId: SaveLocalUserId,
    private val getLocalUserId: GetLocalUserId,
//
    private val createNewGame: CreateNewGame,
//    private val updateGame: UpdateGame,
//
    private val createNewUser: CreateNewUser,
    private val getUser: GetUser,
    private val setUser: UpdateUser,
//    private val joinToUser: JoinToUser,

    private val authService: AuthService
) : ViewModel() {

    val TAG = "erich"

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

/*
        *-------------- CONTROL DE LOGIN / RECUPERAR USER ----------------
        */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (authService.isUserLogged()) {
                val userId = async{ getLocalUserId() }.await()
                Log.i(TAG, "userId: $userId")

                if (userId.isNotEmpty()){
                    Log.i(TAG, "userId.isNotEmpty(): $userId")
                    loadUser(userId)
                } else {
                    Log.i(TAG, "userId.isEmpty(): $userId")
                    logout()
                }
            } else {
                Log.i(TAG, "userId.NoLogin()")
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
    fun onCreateGame(hallName: String, password: String, navigateToMach: (String, String) -> Unit) {
        val newGame = getNewGame(hallName = hallName, password = password)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val hallId = async {createNewGame(newGame.toModelData())}.await()

                if(hallId.isNotEmpty()){
                    updateUser(_user.value.copy(lastHall = hallId))
                    Log.i("erich", "Sala creada: $hallId \n $newGame")
                }
            } catch (e:Exception){
                Log.i("erich", "exploto todo: ${e.message}")
            }
        }
    }
    private fun getNewGame(hallId: String? = null, hallName: String, password: String) = GameModelUi(
        hallId = hallId,
        hallName = hallName,

        board = List(9) { PlayerType.Empty },

        player1 = _user.value.toPlayer(PlayerType.FirstPlayer),
        player2 = null,
        playerTurn = _user.value.toPlayer(PlayerType.FirstPlayer),

        isPublic = password.isEmpty(),
        password = password,
    )
    fun joinGame(hallId: String, navigateToMach: (String, String) -> Unit) {
         navigateToMach(hallId, _user.value.userId)
    }
/*
    *--------------------------------
    */

    fun viewHalls(navigateToHalls: (String) -> Unit) {
        navigateToHalls(_user.value.userId)
    }








/*
*----------- AUTETICATION -------------
*/

    /*
    *-------Email-------
    */
    fun singUp(email: String, password: String, verifyPassword: String) {
        if (password == verifyPassword){
            viewModelScope.launch(Dispatchers.IO){
                _loading.value = true
                try {
                    val result = authService.register(email, password)

                    result?.let {
                        createUser(result.uid,result.email.orEmpty(),"random${(0..100000).random()}")
                        saveLocalUserId(it.uid)
                    }

                } catch (e: Exception) {
                    Log.e("Erich", "${e.message}")
                    when(e.message){
                        "The email address is badly formatted." -> {
                            _showToast.value = "Email incorrect"
                        }
                        "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> {
                            _showToast.value = "Email or Password incorrect"
                        }
                        "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                            _showToast.value = "Password should be at least 6 characters"
                        }
                        "The email address is already in use by another account." -> {
                            _showToast.value = "Email is already in use"
                        }
                        else -> {
                        }
                    }

                }
                _loading.value = false
            }
        } else {
            _showToast.value = "passwords aren't same"
        }

    }
    fun login(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val result =  authService.login(email, password)
                result?.let {
                    loadUser(it.uid)
                    saveLocalUserId(it.uid)
                }

            } catch (e: Exception) {
                Log.e("Erich", "${e.message}")
                when(e.message){
                    "The email address is badly formatted." -> {
                        _showToast.value = "Email incorrect"
                    }
                    "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> {
                        _showToast.value = "Email or Password incorrect"
                    }
                    else -> {

                    }
                }

            }
            _loading.value = false
        }
    }
    fun setChangePassword(email: String) {
        if (email.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                _loading.value = true
                try {
                    if (authService.changePassword(email)){
                        _showToast.value = "Email Sent"
                    }

                } catch (e: Exception) {
                    Log.e("Erich", "${e.message}")
                    _showToast.value = "Email Incorrect"
                }
                _loading.value = false
            }
        } else {
            _showToast.value = "Insert Email"
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
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val result = async{ authService.loginWithGoogle(idToken) }.await()
                result?.let {
                    loadUser(it.uid){
                        createUser(it.uid,it.email.orEmpty(),it.displayName.orEmpty())
                    }
                    saveLocalUserId(it.uid)
                }

            }catch (e:Exception){
                Log.e("Erich", "${e.message}")
            }
            _loading.value = false
        }
    }
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.logout()
            saveLocalUserId("")
        }

        _uiState.value = HomeViewState.LOGIN
    }

    /*
    *--------- USER  ---------
    */
    private fun loadUser(serverUserId: String, createUser:() -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val userAux = async{ getUser(serverUserId) }.await()

                if (userAux.userEmail.isNotEmpty()){
                    _user.value = userAux
                    _uiState.value = HomeViewState.HOME
                } else {
                    createUser()
                }

            } catch(e:Exception){
                logout()
                Log.e("Erich", "${e.message}")
            }
        }
    }
    private fun createUser(userId: String, userEmail: String, userName: String){
        val newUser = UserModelUi(userId = userId, userEmail = userEmail, userName = userName)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { createNewUser(newUser) }.await()
                if(result){
                    _user.value = newUser
                    _uiState.value = HomeViewState.HOME
                }
            } catch(e:Exception){
                Log.e("Erich", "${e.message}")
            }

        }
    }
    private fun updateUser(user:UserModelUi){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { setUser(user) }.await()

                if(result){
                    Log.e("Erich", "$result")
                    _user.value = user
                    _navigateToHall.value = NavigateToHall(true, user.lastHall, user.userId)
                }
            } catch(e:Exception){
                Log.e("Erich", "${e.message}")
            }
        }
    }


    fun clearToast() {
        _showToast.value = ""
    }

    fun clearNavigateToHall() {
        _navigateToHall.value = _navigateToHall.value.copy(false)
    }

    fun editUserName(newUserName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = async { setUser(_user.value.copy(userName = newUserName)) }.await()

                if(result){
                    Log.e("Erich", "$result")
                    _user.value = user.value.copy(userName = newUserName)
//                    _navigateToHall.value = NavigateToHall(true, user.lastHall, user.userId)
                }
            } catch(e:Exception){
                Log.e("Erich", "${e.message}")
            }
        }
    }

    /*
        *---------------------------------------
        */
}

sealed class HomeViewState {
    object LOADING : HomeViewState()
    object LOGIN : HomeViewState()
    object HOME : HomeViewState()
//    object SINGUP : HomeViewState()
}

data class NavigateToHall(
    val state:Boolean = false,
    val hallId:String = "",
    val userId:String = ""
)