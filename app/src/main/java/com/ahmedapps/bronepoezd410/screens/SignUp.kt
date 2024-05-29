package com.ahmedapps.bankningappui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.ahmedapps.bankningappui.components.CheckBoxComponent
import com.ahmedapps.bankningappui.components.HeadingTextComponent
import com.ahmedapps.bankningappui.components.MyTextFieldComponent
import com.ahmedapps.bankningappui.components.NormalTextComponent
import com.ahmedapps.bankningappui.components.PasswordTextFieldComponent
import com.ahmedapps.bankningappui.components.ButtonComponent
import com.ahmedapps.bankningappui.components.ClickableLoginTextComponent
import com.ahmedapps.bankningappui.components.DividerTextComponent
import com.ahmedapps.bankningappui.data.SignupViewModel
import com.ahmedapps.bankningappui.data.SignupUIEvent
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen

@Composable
fun SignUp (signupViewModel: SignupViewModel = viewModel())
{
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
    Surface (color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
        ) {
        Column (modifier = Modifier.fillMaxSize()) {
        NormalTextComponent(value = stringResource(id = R.string.hello))
        HeadingTextComponent(value = stringResource(id = R.string.create_account))
        Spacer(modifier = Modifier.height(20.dp))


            MyTextFieldComponent(labelValue = stringResource(id = R.string.first_name),
                leadingIcon = Icons.Default.AccountCircle,
                onTextSelected = {
                    signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                },
                errorStatus = signupViewModel.registrationUIState.value.firstNameError
            )

            MyTextFieldComponent(
            labelValue = stringResource(id = R.string.last_name),
                leadingIcon = Icons.Default.AccountCircle,
                onTextSelected = {
                    signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                },
                errorStatus = signupViewModel.registrationUIState.value.lastNameError
            )
            MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                leadingIcon = Icons.Default.Email,
                onTextSelected = {
                    signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                },
                errorStatus = signupViewModel.registrationUIState.value.emailError
            )
            PasswordTextFieldComponent(labelValue = stringResource(id = R.string.password),
                leadingIcon = Icons.Default.Lock,
                onTextSelected = {
                    signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                },
                errorStatus = signupViewModel.registrationUIState.value.passwordError
            )
            CheckBoxComponent(value = stringResource(id = R.string.terms_and_conditions), onTextSelected = {
                BroBankAppRouter.navigateTo(Screen.TermsAndConditionsScreen)
            },
                onCheckedChange = {
                    signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it))
                })

            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(value = stringResource(id = R.string.register), onButtonClicked = {
                signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
            },
                isEnabled = signupViewModel.allValidationsPassed.value
            )

            DividerTextComponent()

            ClickableLoginTextComponent(
                tryingToLogin = true,
                onTextSelected = {
                BroBankAppRouter.navigateTo(Screen.LoginScreen)
            })
        }
    }

        if(signupViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }
}


@Preview
@Composable
fun DefaultPreviewOfSignUpScreen(){
    SignUp()
}