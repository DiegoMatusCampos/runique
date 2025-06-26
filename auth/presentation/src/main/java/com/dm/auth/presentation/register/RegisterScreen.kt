package com.dm.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dm.auth.presentation.R
import com.dm.auth.domain.UserDataValidator
import com.dm.core.presentation.designsystem.CheckIcon
import com.dm.core.presentation.designsystem.CrossIcon
import com.dm.core.presentation.designsystem.EmailIcon
import com.dm.core.presentation.designsystem.Poppins
import com.dm.core.presentation.designsystem.RuniqueDarkRed
import com.dm.core.presentation.designsystem.RuniqueGray
import com.dm.core.presentation.designsystem.RuniqueGreen
import com.dm.core.presentation.designsystem.RuniqueTheme
import com.dm.core.presentation.designsystem.components.GradientBackground
import com.dm.core.presentation.designsystem.components.RuniqueActionButton
import com.dm.core.presentation.designsystem.components.RuniquePasswordTextField
import com.dm.core.presentation.designsystem.components.RuniqueTextField
import com.dm.core.presentation.ui.util.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable

fun RegisterScreenRot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel  = koinViewModel()

) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(viewModel.events) {event ->
        when(event){
            is RegisterEvent.Error -> {
                keyboardController?.hide()

                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()

                Toast.makeText(
                    context,
                    R.string.registration_successful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulRegistration()
            }
        }

    }
    RegisterScreen(

        state = viewModel.state,

        onAction = {action ->

            when(action){
                RegisterAction.OnLoginClick -> onSignInClick()
                else -> Unit
            }
            viewModel.onAction(action)

        }

    )

}

@Composable

private fun RegisterScreen(

    state: RegisterState,

    onAction: (RegisterAction) -> Unit

) {

    GradientBackground{
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = RuniqueGray
                    )
                ){
                    append(stringResource(R.string.already_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation = stringResource(R.string.login)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ) {
                        append(stringResource(R.string.login))
                    }
                }
            }

            ClickableText(
                text = annotatedString,
                onClick = {offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        onAction(RegisterAction.OnLoginClick)
                    }
                }
            )

            Spacer(Modifier.height(48.dp))

            RuniqueTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if(state.isEmailValid) CheckIcon else null,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                additionalInfo = stringResource(R.string.must_be_a_valid_email)
            )
            Spacer(Modifier.height(16.dp))
            RuniquePasswordTextField(
                state = state.password,
                isPasswordVisable = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            PasswordRequirement(
                text = stringResource(R.string.at_least_x_characters, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasMinLength,
            )
            Spacer(Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.at_least_one_number),
                isValid = state.passwordValidationState.hasNumber,
            )
            Spacer(Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.contains_lowercase_character),
                isValid = state.passwordValidationState.hasLowerCaseCharacter,
            )
            Spacer(Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(R.string.contains_uppercase_character),
                isValid = state.passwordValidationState.hasUpperCaseCharacter,
            )
            Spacer(Modifier.height(32.dp))
            RuniqueActionButton(
                text = stringResource(R.string.register),
                isLoading = state.isRegistering,
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Composable
fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = if(isValid) CheckIcon else CrossIcon,
            contentDescription = null,
            tint = if(isValid) RuniqueGreen else RuniqueDarkRed
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp

        )
    }
}

@Preview

@Composable

private fun RegisterScreenPreview() {

    RuniqueTheme{

       RegisterScreen(

           state = RegisterState(
           ),

           onAction = {}

       )

   }

}