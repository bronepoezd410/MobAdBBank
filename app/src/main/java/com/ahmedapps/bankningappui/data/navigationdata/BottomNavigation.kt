package com.ahmedapps.bankningappui.data.navigationdata

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigation(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)