package com.example.tateti_20.ui.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tateti_20.ui.ExampleScreen
import com.example.tateti_20.ui.core.Routes.*
import com.example.tateti_20.ui.game.GameScreen
import com.example.tateti_20.ui.halls.HallsScreen
import com.example.tateti_20.ui.home.HomeScreen

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

}