package com.dm.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dm.auth.presentation.register.RegisterAction
import com.dm.core.presentation.designsystem.EmailIcon
import com.dm.core.presentation.designsystem.Poppins
import com.dm.core.presentation.designsystem.RuniqueGray
import com.dm.core.presentation.designsystem.RuniqueTheme
import com.dm.core.presentation.designsystem.components.GradientBackground
import com.dm.core.presentation.designsystem.components.RuniqueActionButton
import com.dm.core.presentation.designsystem.components.RuniquePasswordTextField
import com.dm.core.presentation.designsystem.components.RuniqueTextField
import com.dm.core.presentation.ui.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable

fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()

) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserveAsEvents(viewModel.events) {event ->
        when(event){
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }
            LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(context, R.string.login_success, Toast.LENGTH_LONG).show()
                onLoginSuccess()
            }
        }

    }
    LoginScreenScreen(

        state = viewModel.state,

        onAction = {action ->
            when(action){
                LoginAction.OnRegisterClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)

        }

    )

}

@Composable

private fun LoginScreenScreen(

    state: LoginState,

    onAction: (LoginAction) -> Unit

) {

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(vertical = 32.dp)
                .padding(top = 16.dp)
        ) {

            Text(
                text = stringResource(R.string.hi_there),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.runique_welcome),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(48.dp))
            RuniqueTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = null,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
            )
            Spacer(Modifier.height(16.dp))
            RuniquePasswordTextField(
                state = state.password,
                isPasswordVisable = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))
            RuniqueActionButton(
                text = stringResource(R.string.login),
                isLoading = state.isLoggingIn,
                onClick = {
                    onAction(LoginAction.OnLoginCLick)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canLogin && !state.isLoggingIn
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ){
                    append(stringResource(R.string.dont_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "clickable_text",
                        annotation = stringResource(R.string.sign_up)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        )
                    ) {
                        append(stringResource(R.string.sign_up))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){

                ClickableText(
                    text = annotatedString,
                    onClick = {offset ->
                        annotatedString.getStringAnnotations(
                            tag = "clickable_text",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            onAction(LoginAction.OnRegisterClick)
                        }
                    }
                )
            }


        }
    }

}

@Preview

@Composable

private fun LoginScreenRotScreenPreview() {

    RuniqueTheme {

        LoginScreenScreen(

            state = LoginState(),

            onAction = {}

        )

    }

}