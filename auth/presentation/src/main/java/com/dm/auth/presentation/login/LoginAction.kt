package com.dm.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnLoginCLick: LoginAction
    data object OnRegisterClick: LoginAction
}