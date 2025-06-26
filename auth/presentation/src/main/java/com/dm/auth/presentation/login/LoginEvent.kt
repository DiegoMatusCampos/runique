package com.dm.auth.presentation.login

import com.dm.core.presentation.ui.util.UiText

sealed interface LoginEvent {
    data class Error(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
}