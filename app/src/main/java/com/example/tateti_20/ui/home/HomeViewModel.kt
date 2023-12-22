package com.example.tateti_20.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.data.network.AuthService
import com.example.tateti_20.domain.CreateNewGame
import com.example.tateti_20.domain.CreateNewUser
import com.example.tateti_20.domain.GetUser
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
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


@HiltViewModel
class HomeViewModel @Inject constructor(
//    private val saveLocalUserId: SaveLocalUserId,
//    private val getLocalUserId: GetLocalUserId,
//
//    private val createNewGame: CreateNewGame,
//    private val updateGame: UpdateGame,
//
    private val createNewUser: CreateNewUser,
    private val getUser: GetUser,
//    private val joinToUser: JoinToUser,
//    private val updateUser: UpdateUser,

    private val authService: AuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.LOADING)
    val uiState: StateFlow<HomeViewState> = _uiState

    private val _user = MutableStateFlow(UserModelUi())
    val user: StateFlow<UserModelUi> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        val isUserLogged = authService.isUserLogged()
        _uiState.value = if (isUserLogged) {

            HomeViewState.HOME
        } else {
            HomeViewState.LOGIN
        }
    }

    /*
    *----------- GAME -------------
    */
    fun onCreateGame(hallName: String, navigateToMach: (String, String) -> Unit) {
        if (_user.value.lastHall.isNullOrEmpty()) {
            val game = getNewGame(hallName = hallName)

//            val hallId = createNewGame(game.toModelData())
//            _user.value = _user.value.copy(hallId = hallId)

//            viewModelScope.launch {
//                updateUser(_user.value.toModelData())
//            }

        } else {
            val game = getNewGame(hallId = _user.value.lastHall, hallName = hallName)
//            viewModelScope.launch {
//                updateGame(game.toModelData())
//            }
        }
//        navigateToMach(_user.value.hallId, _user.value.userId)
    }
    private fun getNewGame(hallId: String? = null, hallName: String) = GameModelUi(
        hallId = hallId,
        hallName = hallName,
        available = true,
        board = List(9) { PlayerType.Empty },
        player2 = null,
        player1 = _user.value.toPlayer(PlayerType.FirstPlayer),
        playerTurn = _user.value.toPlayer(PlayerType.FirstPlayer)
    )
    fun joinGame(hallId: String, navigateToMach: (String, String) -> Unit) {
//        navigateToMach(hallId, _user.value.userId)
    }


    fun viewHalls(navigateToHalls: (String) -> Unit) {
//        navigateToHalls(_user.value.userId)
    }

/*
*----------- AUTETICATION -------------
*/
    /*
    *-------Email-------
    */
    fun getSingUp() {
        _uiState.value = HomeViewState.SINGUP
    }
    fun singUp(nickname: String,email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO){
            _loading.value = true
            try {
                val result = authService.register(email, password)

                result?.let { createUser(result.uid,result.email.orEmpty(),nickname) }

            } catch (e: Exception) {
                Log.e("Erich", "${e.message}")
            }
            _loading.value = false
        }
    }
    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val result =  authService.login(email, password)
                result?.let { loadUser(it) }

            } catch (e: Exception) {
                Log.e("Erich", "${e.message}")
            }
            _loading.value = false
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
                result?.let { loadUser(it) }

            }catch (e:Exception){
                Log.e("Erich", "${e.message}")
            }
            _loading.value = false
        }
    }
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.logout()
        }
        _uiState.value = HomeViewState.LOGIN
    }


    /*
    *--------- USER  ---------
    */
    private fun loadUser(firebaseUser: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val userAux = async{ getUser(firebaseUser.uid) }.await()

                if (userAux.userEmail.isEmpty()){
                    createUser(firebaseUser.uid, firebaseUser.email.orEmpty(), firebaseUser.displayName.orEmpty())
                } else {
                    _user.value = userAux
                    _uiState.value = HomeViewState.HOME
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

}

sealed class HomeViewState {
    object LOADING : HomeViewState()
    object LOGIN : HomeViewState()
    object HOME : HomeViewState()
    object SINGUP : HomeViewState()
}