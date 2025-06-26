package com.dm.auth.presentation.register

import com.dm.core.presentation.ui.util.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}