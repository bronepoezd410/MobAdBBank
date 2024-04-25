package com.ahmedapps.bankningappui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen
import com.ahmedapps.bankningappui.navigation.SystemBackButtonHandler
import com.ahmedapps.bankningappui.screens.AccountPage
import com.ahmedapps.bankningappui.screens.LoginScreen
import com.ahmedapps.bankningappui.screens.SignUp
import com.ahmedapps.bankningappui.screens.TermsAndConditionsScreen
import com.ahmedapps.bankningappui.screens.WalletPage
import com.ahmedapps.bankningappui.ui.theme.BankningAppUITheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankningAppUITheme {
                checkForActiveSession()
                SetBarColor(color = MaterialTheme.colorScheme.background)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if(isUserLoggedIn.value == true){
                        BroBankAppRouter.navigateTo(Screen.HomeScreen)
                    }
                    Crossfade (targetState  = BroBankAppRouter.currentScreen,
                        label = ""
                    ) { currentState->
                        when(currentState.value){
                            
                            is Screen.SignUp-> {
                                SignUp()
                            }
                            is Screen.TermsAndConditionsScreen -> {
                                TermsAndConditionsScreen()
                            }
                            is Screen.LoginScreen -> {
                                LoginScreen()
                            }
                            is Screen.HomeScreen -> {
                                HomeScreen()
                            }
                            is Screen.AccountPage -> {
                                AccountPage()
                            }
                            is Screen.WalletPage -> {
                                WalletPage()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }

    }
    override fun onStart() {
        super.onStart()
        checkForActiveSession()
    }
    val isUserLoggedIn : MutableLiveData<Boolean> = MutableLiveData()

    fun checkForActiveSession(){
        if(FirebaseAuth.getInstance().currentUser != null){
            Log.d(ContentValues.TAG, "Valid Session")
            isUserLoggedIn.value = true
            Log.d(ContentValues.TAG, FirebaseAuth.getInstance().currentUser.toString())

        } else {
            Log.d(ContentValues.TAG, "User is not logged in")
            isUserLoggedIn.value = false

        }
    }


    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {


    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedIndex = 0)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

        WalletSection(walletAmount = null)
        CardsSection()
        Spacer(modifier = Modifier.height(16.dp))
        FinanceSection()

            val viewModel: CurrencyViewModel = viewModel()
            CurrenciesSection(viewModel = viewModel)
            SystemBackButtonHandler (){
                BroBankAppRouter.navigateTo(Screen.HomeScreen)
            }
        }


    }
}













