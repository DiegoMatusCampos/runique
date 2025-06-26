package com.dm.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dm.auth.domain.AuthRepository
import com.dm.auth.domain.UserDataValidator
import com.dm.auth.presentation.R
import com.dm.core.domain.util.DataError
import com.dm.core.domain.util.Result
import com.dm.core.presentation.ui.util.UiText
import com.dm.core.presentation.ui.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val repository: AuthRepository
): ViewModel() {



    var state by mutableStateOf(RegisterState())
        private set

    private val eventsChannel = Channel<RegisterEvent>()
    val events = eventsChannel.receiveAsFlow()

    init {

        snapshotFlow { state.email.text }
            .onEach { email ->
                val isEmailValid = userDataValidator.isEmailValid(email.toString())
                state = state.copy(
                    isEmailValid = isEmailValid,
                    canRegister = isEmailValid && state.passwordValidationState.isPasswordValid && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

        snapshotFlow { state.password.text }
            .onEach { password ->

                val passwordValidationState = userDataValidator.validatePassword(password.toString())
                state = state.copy(
                    passwordValidationState = passwordValidationState,
                    canRegister = state.isEmailValid && passwordValidationState.isPasswordValid && !state.isRegistering
                )
            }
            .launchIn(viewModelScope)

    }

    fun onAction(action: RegisterAction){
        when(action){
            RegisterAction.OnRegisterClick -> {
                register()
            }
            RegisterAction.OnTogglePasswordVisibilityClick ->{
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
            else -> Unit
        }
    }

    private fun register(){

        viewModelScope.launch {

            state = state.copy(
                isRegistering = true
            )
            val result = repository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(
                isRegistering = false
            )

            when(result){
                is Result.Error -> {
                    if(result.error == DataError.Network.CONFLICT){
                        eventsChannel.send(RegisterEvent.Error(
                            UiText.StringResource(R.string.error_email_exists)
                        ))
                    }else {

                        eventsChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success -> {
                    eventsChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }
}