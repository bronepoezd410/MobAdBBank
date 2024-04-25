package com.ahmedapps.bankningappui.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ahmedapps.bankningappui.BottomNavigationBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun WalletPage() {
    var cardNumber by remember { mutableStateOf(TextFieldValue()) }
    var cardName by remember { mutableStateOf(TextFieldValue()) }
    var moneyAmount by remember { mutableStateOf(TextFieldValue()) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val cardHolderName ="$firstName $lastName" // Используем исходное значение имени держателя карты
    var selectedCardType by remember { mutableStateOf("VISA") } // Значение по умолчанию

    var documentReference: DocumentReference? = null // Ссылка на документ


    val db = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance().currentUser

    currentUser?.let { user ->
        val userId = user.uid
        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val userData = document.data
                if (userData != null) {
                    firstName = userData["first_name"] as? String ?: ""
                    lastName = userData["last_name"] as? String ?: ""
                }
            }
        }.addOnFailureListener { e ->
            // Обработка ошибки при получении данных из Firestore
            Log.e("Firestore", "Error getting user data: $e")
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1)
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedCardType == "VISA",
                    onClick = { selectedCardType = "VISA" },
                    enabled = true
                )
                Text("VISA", modifier = Modifier.padding(start = 8.dp))

                RadioButton(
                    selected = selectedCardType == "MASTER CARD",
                    onClick = { selectedCardType = "MASTER CARD" },
                    enabled = true
                )
                Text("MASTER CARD", modifier = Modifier.padding(start = 8.dp))
            }
            TextField(
                value = "$firstName $lastName",
                onValueChange = { },
                label = { Text("Card Holder") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Card Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = moneyAmount,
                onValueChange = { moneyAmount = it },
                label = { Text("Money Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val cardData = hashMapOf(
                        "cardNumber" to cardNumber.text,
                        "cardHolder" to cardHolderName,
                        "cardName" to cardName.text,
                        "moneyAmount" to moneyAmount.text,
                        "cardType" to selectedCardType,
                    )

                    currentUser?.let { user ->
                        db.collection("users").document(user.uid).collection("creditCards")
                            .add(cardData)
                            .addOnSuccessListener {
                                // Card added successfully
                                cardNumber = TextFieldValue()
//                            cardHolder = TextFieldValue()
                                cardName = TextFieldValue()
                                moneyAmount = TextFieldValue()
                                selectedCardType = "VISA"
                            }
                            .addOnFailureListener { e ->
                                // Error adding card
                                // Handle error appropriately
                            }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Credit Card")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display user's credit cards
            currentUser?.let { DisplayUserCreditCards(it) }
        }
    }
}

@Composable
fun DisplayUserCreditCards(currentUser: FirebaseUser) {
    val db = Firebase.firestore
    var isLoading by remember { mutableStateOf(true) } // Состояние загрузки данных
    var creditCards by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    LaunchedEffect(Unit) {
        currentUser.uid.let { userId ->
            val userCreditCardsRef = db.collection("users").document(userId)
                .collection("creditCards")

            userCreditCardsRef.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore", "Error getting credit cards: ${error.message}")
                    return@addSnapshotListener
                }

                val cardsList = mutableListOf<Map<String, String>>()
                value?.documents?.forEach { document ->
                    val cardData = hashMapOf<String, String>()
                    cardData["cardNumber"] = document["cardNumber"] as? String ?: ""
                    cardData["cardHolder"] = document["cardHolder"] as? String ?: ""
                    cardData["cardName"] = document["cardName"] as? String ?: ""
                    cardData["moneyAmount"] = document["moneyAmount"] as? String ?: ""
                    cardData["cardType"] = document["cardType"] as? String ?: ""
                    cardsList.add(cardData)
                }

                creditCards = cardsList // Обновление состояния с данными кредитных карт
                isLoading = false // Установка состояния загрузки в false после завершения загрузки
            }
        }
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(creditCards) { cardData ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween // Расположение элементов между собой
                ) {
                    Text("Card Type: ${cardData["cardType"]}")
                    Text("Card Name: ${cardData["cardName"]}")
                    Text("Money Amount: ${cardData["moneyAmount"]}")
                    Text("Card Number: ${cardData["cardNumber"]}")
                    Text("Card Holder: ${cardData["cardHolder"]}")
                    Button(
                        onClick = {
                            currentUser.let { user ->
                                db.collection("users").document(user.uid).collection("creditCards")
                                    .whereEqualTo("cardNumber", cardData["cardNumber"])
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        for (document in querySnapshot.documents) {
                                            document.reference.delete()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle error appropriately
                                    }
                            }
                        },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text("Delete")
                    }
                }

            }
        }
        item {
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween // Расположение элементов между собой
                ) {
                    Text("")
                    Text("")
                    Text("")
                    Text("")
                    Text("")
                }
            }
        }
    }
}
