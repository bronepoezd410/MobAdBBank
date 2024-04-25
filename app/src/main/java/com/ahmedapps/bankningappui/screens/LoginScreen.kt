package com.ahmedapps.bankningappui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedapps.bankningappui.R
import com.ahmedapps.bankningappui.components.ButtonComponent
import com.ahmedapps.bankningappui.components.ClickableLoginTextComponent
import com.ahmedapps.bankningappui.components.DividerTextComponent
import com.ahmedapps.bankningappui.components.HeadingTextComponent
import com.ahmedapps.bankningappui.components.MyTextFieldComponent
import com.ahmedapps.bankningappui.components.NormalTextComponent
import com.ahmedapps.bankningappui.components.PasswordTextFieldComponent
import com.ahmedapps.bankningappui.components.UnderLineTextComponent
import com.ahmedapps.bankningappui.data.LoginUIEvent
import com.ahmedapps.bankningappui.data.LoginViewModel
import com.ahmedapps.bankningappui.data.SignupUIEvent
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen
import com.ahmedapps.bankningappui.navigation.SystemBackButtonHandler
import com.ahmedapps.bankningappui.data.SignupViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
        ) {
        Surface(color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                NormalTextComponent(value = stringResource(id = R.string.hello))
                HeadingTextComponent(value = stringResource(id = R.string.welcome_back))

                Spacer(modifier = Modifier.height(20.dp))


                MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                    leadingIcon = Icons.Default.Email,
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError

                )

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    leadingIcon = Icons.Default.Lock,
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError
                )

                Spacer(modifier = Modifier.height(20.dp))
                UnderLineTextComponent(value = stringResource(id = R.string.forgot_password))

                Spacer(modifier = Modifier.height(40.dp))
                ButtonComponent(
                    value = stringResource(id = R.string.login), onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)

                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()

                ClickableLoginTextComponent(
                    tryingToLogin = false,
                    onTextSelected = {
                        BroBankAppRouter.navigateTo(Screen.SignUp)
                    })

            }

        }

        if(loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }
        SystemBackButtonHandler {
        BroBankAppRouter.navigateTo(Screen.SignUp)
    }
}

@Preview
@Composable
fun LoginScreenPreview(){

    LoginScreen()
    
}