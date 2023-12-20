package com.example.tateti_20.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.data.network.AuthService
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
//    private val createNewUser: CreateNewUser,
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
//        getUser()

        val isUserLogged = authService.isUserLogged()
        _uiState.value = if (isUserLogged) {
            HomeViewState.HOME
        } else {
            HomeViewState.LOGIN
        }
    }

    private fun getUser() {
//        viewModelScope.launch {
//            val userId = async { getLocalUserId() }.await()
//
//            if (userId.isNotEmpty()) {
//                viewModelScope.launch {
//                    joinToUser(userId).take(1).collect {
//                        Log.w("printResume", "getUser it: $it")
//                        _user.value = it ?: UserModelUi()
//                    }
//                }
////                _uiState.value = HomeViewState.HOME
//            } else {
//                Log.w("printResume", "getUser userId: $userId")
////                _uiState.value = HomeViewState.LOGIN
//            }
//        }
    }

    fun onCreateGame(hallName: String, navigateToMach: (String, String) -> Unit) {
        Log.d("erich", "onCreateGame: entre")

        if (_user.value.hallId.isNullOrEmpty()) {
            val game = getNewGame(hallName = hallName)

//            val hallId = createNewGame(game.toModelData())
//            _user.value = _user.value.copy(hallId = hallId)

//            viewModelScope.launch {
//                updateUser(_user.value.toModelData())
//            }

        } else {
            val game = getNewGame(hallId = _user.value.hallId, hallName = hallName)
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
        Log.d("erich", "joinGame: entre")
//        navigateToMach(hallId, _user.value.userId)
    }


    fun viewHalls(navigateToHalls: (String) -> Unit) {
        Log.d("erich", "viewHalls: entre")
//        Log.d("erich", "viewHalls: ${_user.value.userId}")
//        navigateToHalls(_user.value.userId)
    }

    fun getSingUp() {
        _uiState.value = HomeViewState.SINGUP
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    authService.login(email, password)
                }
                if (result != null) _uiState.value = HomeViewState.HOME

            } catch (e: Exception) {
                Log.e("Erich", "${e.message}")
            }
            _loading.value = false
        }
//        val currenId = createNewUser(UserModelUi(nickname = nickname).toModelData())
//
//        if (currenId.isNotEmpty()){
//            viewModelScope.launch {
//                saveLocalUserId(currenId)
//                joinToUser(currenId).collect{
//                    _user.value = it ?: UserModelUi(currenId)
//                    Log.w("printResume", "onNickNameSelected: ${_user.value}")
//                }
//            }
//        }
//        _uiState.value = HomeViewState.HOME
    }

    fun singUp(email: String, password: String) {
        viewModelScope.launch() {
            _loading.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    authService.register(email, password)
                }

                if (result != null) _uiState.value = HomeViewState.HOME

            } catch (e: Exception) {
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

    fun onGoogleLoginSelected(googleLauncherLogin: (GoogleSignInClient) -> Unit) {
        val gsc = authService.getGoogleClient()
        googleLauncherLogin(gsc)
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO){
                authService.loginWithGoogle(idToken)
            }

            if (result != null){
                _uiState.value = HomeViewState.HOME
            }
            _loading.value = false
        }
    }

}

sealed class HomeViewState {
    object LOADING : HomeViewState()
    object LOGIN : HomeViewState()
    object HOME : HomeViewState()
    object SINGUP : HomeViewState()
}