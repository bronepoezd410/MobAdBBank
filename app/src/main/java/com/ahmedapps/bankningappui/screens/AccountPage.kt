package com.ahmedapps.bankningappui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmedapps.bankningappui.BottomNavigationBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AccountPage() {
    val firstNameState = remember { mutableStateOf("") }
    val lastNameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }

    val isEditing = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(userId)

            userDocument.get()
                .addOnSuccessListener { document ->
                    val firstName = document.getString("first_name") ?: ""
                    val lastName = document.getString("last_name") ?: ""
                    val email = document.getString("email") ?: ""

                    firstNameState.value = firstName
                    lastNameState.value = lastName
                    emailState.value = email
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Ошибка при получении информации о пользователе", e)
                }
        }
    }

// Обработчик для кнопки "Сохранить"
    val onSaveClicked = {
        // Обновляем информацию о пользователе в базе данных Firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userDocument = FirebaseFirestore.getInstance().collection("users").document(userId)

            val updatedUserData: MutableMap<String, Any> = hashMapOf(
                "first_name" to firstNameState.value,
                "last_name" to lastNameState.value,
                "email" to emailState.value
            )

            userDocument.update(updatedUserData)
                .addOnSuccessListener {

                    Log.d(TAG, "Информация о пользователе успешно обновлена")

                    isEditing.value = false
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Ошибка при обновлении информации о пользователе", e)
                }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedIndex = 2)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp), // добавляем padding снизу
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            EditableTextField(
                label = "First Name",
                text = firstNameState.value,
                readOnly = !isEditing.value,
                onTextChanged = { if (isEditing.value) firstNameState.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            EditableTextField(
                label = "Last Name",
                text = lastNameState.value,
                readOnly = !isEditing.value,
                onTextChanged = { if (isEditing.value) lastNameState.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            EditableTextField(
                label = "Email",
                text = emailState.value,
                readOnly = !isEditing.value,
                onTextChanged = { if (isEditing.value) emailState.value = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (!isEditing.value) {
                Button(
                    onClick = { isEditing.value = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Edit")
                }
            } else {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { isEditing.value = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onSaveClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@Composable
fun EditableTextField(
    label: String,
    text: String,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            if (!readOnly) {
                onTextChanged(it)
            }
        },
        label = { Text(label) },
        readOnly = readOnly,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun AccountPagePreview(){
    AccountPage()
}