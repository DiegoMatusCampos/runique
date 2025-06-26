package com.dm.auth.presentation.login

import androidx.lifecycle.ViewModel
import com.dm.auth.domain.AuthRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.dm.auth.domain.UserDataValidator
import com.dm.auth.presentation.R
import com.dm.core.domain.util.DataError
import com.dm.core.domain.util.Result
import com.dm.core.presentation.ui.util.UiText
import com.dm.core.presentation.ui.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel() {


    var state by mutableStateOf(LoginState())
        private set


    private val channelEvents = Channel<LoginEvent>()
    val events = channelEvents.receiveAsFlow()


    init {
        combine(
            snapshotFlow { state.email.text },
            snapshotFlow { state.password.text }
        ) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isEmailValid(email.toString().trim()) &&
                        password.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginCLick -> login()
            LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(
                isLoggingIn = true
            )
            val result = repository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(
                isLoggingIn = false
            )
            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        channelEvents.send(

                            LoginEvent.Error(UiText.StringResource(R.string.error_email_password_incorrect))
                        )
                    } else {
                        channelEvents.send(

                            LoginEvent.Error(result.error.asUiText())
                        )
                    }
                }

                is Result.Success -> {
                    channelEvents.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}