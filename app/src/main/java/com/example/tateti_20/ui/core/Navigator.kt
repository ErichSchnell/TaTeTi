package com.example.tateti_20.ui.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tateti_20.ui.annotator.AnnotatorsScreen
import com.example.tateti_20.ui.core.Routes.*
import com.example.tateti_20.ui.game.GameScreen
import com.example.tateti_20.ui.generala.GeneralaScreen
import com.example.tateti_20.ui.generico.GenericoScreen
import com.example.tateti_20.ui.halls.HallsScreen
import com.example.tateti_20.ui.home.HomeScreen
import com.example.tateti_20.ui.truco.TrucoScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentWrapper(navigatonController: NavHostController) {


    NavHost(
        navController = navigatonController,
        startDestination = Home.route
    ){
        composable(Home.route){
            HomeScreen(
                navigateToMach = {hallId, userId ->
                    navigatonController.navigate(Game.createRoute(hallId, userId))
                },
                navigateToHalls = {userId ->
                    navigatonController.navigate(Halls.createRoute(userId))
                },
                navigateToAnnotator = {userEmail ->
                    navigatonController.navigate(Annotator.createRoute(userEmail))
                }
            )
        }
        composable(
            Game.route,
            arguments = listOf(
                navArgument("hallId") {type = NavType.StringType},
                navArgument("userId") {type = NavType.StringType},
            )
        ){
            GameScreen(
                hallId = it.arguments?.getString("hallId").orEmpty(),
                userId = it.arguments?.getString("userId").orEmpty(),
                navigateToHome = {
                    navigatonController.popBackStack(navigatonController.graph.startDestinationId, true)
                    navigatonController.navigate(Home.route)
                }
            )
        }
        composable(
            Halls.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) {
            HallsScreen(
                navigateToMach = {hallId, userId ->
                    navigatonController.navigate(Game.createRoute(hallId, userId))
                },
                userId = it.arguments?.getString("userId").orEmpty()
            )
        }
        composable(
            Annotator.route,
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType }
            )
        ) {
            AnnotatorsScreen(
                userEmail = it.arguments?.getString("userEmail").orEmpty(),
                navigateToGenerala = { userEmail, time -> navigatonController.navigate(Generala.createRoute(userEmail, time)) },
                navigateToGenerico = { userEmail, time -> navigatonController.navigate(Generico.createRoute(userEmail, time)) },
                navigateToTruco = { userEmail, time -> navigatonController.navigate(Truco.createRoute(userEmail, time)) }
            )
        }
        composable(
            Generala.route,
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) {
            GeneralaScreen(
                userEmail = it.arguments?.getString("userEmail").orEmpty(),
                annotatorTime = it.arguments?.getString("time").orEmpty()
            )
        }
        composable(
            Truco.route,
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) {
            TrucoScreen(
                userEmail = it.arguments?.getString("userEmail").orEmpty(),
                annotatorTime = it.arguments?.getString("time").orEmpty()
            )
        }
        composable(
            Generico.route,
            arguments = listOf(
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) {
            GenericoScreen(
                userEmail = it.arguments?.getString("userEmail").orEmpty(),
                annotatorTime = it.arguments?.getString("time").orEmpty()
            )
        }
    }
}

sealed class Routes(val route:String){
    object Home:Routes("home")
    object Game:Routes("game/{hallId}/{userId}"){
        fun createRoute(hallId: String, userId: String): String{
            return "game/${hallId}/${userId}"
        }
    }
    object Halls:Routes("halls/{userId}"){
        fun createRoute(userId: String): String{
            return "halls/${userId}"
        }
    }
    object Annotator:Routes("annotator/{userEmail}"){
        fun createRoute(userEmail: String): String{
            return "annotator/${userEmail}"
        }
    }
    object Generala:Routes("generala/{userEmail}/{time}"){
        fun createRoute(userEmail: String, time: String): String{
            return "generala/$userEmail/$time"
        }
    }
    object Truco:Routes("truco/{userEmail}/{time}"){
        fun createRoute(userEmail: String, time: String): String{
            return "truco/$userEmail/$time"
        }
    }
    object Generico:Routes("generico/{userEmail}/{time}"){
        fun createRoute(userEmail: String, time: String): String{
            return "generico/$userEmail/$time"
        }
    }

}