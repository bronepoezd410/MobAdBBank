package com.ahmedapps.bankningappui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.ahmedapps.bankningappui.R
import com.ahmedapps.bankningappui.components.HeadingTextComponent
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen
import java.lang.reflect.Modifier
import com.ahmedapps.bankningappui.navigation.SystemBackButtonHandler



@Composable
fun TermsAndConditionsScreen() {
    Surface(color = Color.White,
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)) {

        HeadingTextComponent(value = stringResource(id = R.string.terms_and_conditions_header))
        }
    SystemBackButtonHandler (){
        BroBankAppRouter.navigateTo(Screen.SignUp)
    }
    }

@Preview
@Composable
fun TermsAndConditionsScreenPreview (){
    TermsAndConditionsScreen()
}