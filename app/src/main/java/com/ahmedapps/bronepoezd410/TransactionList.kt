package com.ahmedapps.bankningappui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

@Composable
fun TransactionList() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: ""
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }

    LaunchedEffect(userId) {
        val db = Firebase.firestore
        val userTransactionsRef = db.collection("users").document(userId).collection("transactions")
        userTransactionsRef.addSnapshotListener { snapshot, _ ->
            snapshot?.let { querySnapshot ->
                val loadedTransactions = mutableListOf<Transaction>()
                for (doc in querySnapshot.documents) {
                    doc.toObject(Transaction::class.java)?.let { transaction ->
                        loadedTransactions.add(transaction)
                    }
                }
                transactions = loadedTransactions
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()) // Добавляем прокрутку
    ) {
        Text(
            text = "Транзакции текущего пользователя:",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (transactions.isNotEmpty()) {
            transactions.forEach { transaction ->
                TransactionCard(transaction = transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(text = "Нет доступных транзакций")
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        elevation = CardDefaults.cardElevation(
//            defaultElevation = 10.dp
        ),    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val sign = if (transaction.type == "INCOME") "+" else "-"
            Text(text = "${transaction.timestamp}")
            Text(text = "Operation: $sign${transaction.amount}")
            Text(text = "Card Number: ${transaction.cardNumber}")
//            Text(text = "Тип: ${transaction.type}")
//            Text(text = "Тип: $sign")
        }
    }
}

data class Transaction(
    val amount: Int = 0,
    val cardNumber: String = "",
    val timestamp: Date? = null,
    val type: String = ""
)