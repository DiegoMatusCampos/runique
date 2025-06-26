package com.dm.runique

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dm.auth.presentation.intro.IntroScreenRoot
import com.dm.auth.presentation.login.LoginScreenRoot
import com.dm.auth.presentation.register.RegisterScreenRot
import kotlinx.serialization.Serializable

@Serializable
data object Auth

@Serializable
data object Intro

@Serializable
data object Register

@Serializable
data object Login

@Serializable
data object Run

@Serializable
data object RunOverview


@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) Run else Auth
    ) {
        authGraph(navController)
        runGraph(navController)
    }

}

private fun NavGraphBuilder.authGraph(navController: NavHostController){
    navigation<Auth>(
        startDestination = Intro,
    ) {
        composable<Intro> {
            IntroScreenRoot(
                onSignUpClick = {
                    navController.navigate(Register)
                },
                onSignInClick = {
                    navController.navigate(Login)
                }
            )
        }
        composable<Register>{
            RegisterScreenRot(
                onSignInClick = {
                    navController.navigate(Login){
                        popUpTo(
                            Register
                        ) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(Login)
                }
            )
        }

        composable<Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(Run){
                        popUpTo(Auth) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Register){
                        popUpTo(
                            Login
                        ) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },

            )
        }
    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController){
    navigation<Run>(startDestination = RunOverview) {
        composable<RunOverview> {
            Text(
                text = "RUN"
            )
        }
    }
}