package com.ahmedapps.bankningappui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmedapps.bankningappui.data.navigationdata.BottomNavigation
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

val items = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Rounded.Home,
        onClick = {
            BroBankAppRouter.navigateTo(Screen.HomeScreen)
        }
    ),

    BottomNavigation(
        title = "Wallet",
        icon = Icons.Rounded.Wallet,
        onClick = {
            BroBankAppRouter.navigateTo(Screen.WalletPage)
        }
    ),

    BottomNavigation(
        title = "Account",
        icon = Icons.Rounded.AccountCircle,
        onClick = {
            BroBankAppRouter.navigateTo(Screen.AccountPage)
        }
    ),

    BottomNavigation(
        title = "Log Out",
        icon = Icons.Rounded.Cancel,
        onClick = {
            Log.d(TAG, "Logout button clicked")
            logout()
        }
        )
    )

@Composable
fun BottomNavigationBar(selectedIndex: Int) {
    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    onClick = {
                        item.onClick()
                    },
                    selected = index == selectedIndex,
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}



fun logout() {
    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signOut()
    val authStateListener = AuthStateListener {
        if (it.currentUser == null) {
            Log.d(TAG, "Inside sign out success")
            BroBankAppRouter.navigateTo(Screen.LoginScreen)
        } else {
            Log.d(TAG, "Inside sign out is not complete")
        }
    }
    firebaseAuth.addAuthStateListener(authStateListener)
}




















