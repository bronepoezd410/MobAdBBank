package com.ahmedapps.bankningappui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedapps.bankningappui.ui.theme.BlueEnd
import com.ahmedapps.bankningappui.ui.theme.BlueStart
import com.ahmedapps.bankningappui.ui.theme.GreenEnd
import com.ahmedapps.bankningappui.ui.theme.GreenStart
import com.ahmedapps.bankningappui.ui.theme.PurpleEnd
import com.ahmedapps.bankningappui.ui.theme.PurpleStart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


fun getGradient(
    startColor: Color,
    endColor: Color,
): Brush {
    return Brush.horizontalGradient(
        colors = listOf(startColor, endColor)
    )
}

@Preview
@Composable
fun CardsSection() {

    val db = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance().currentUser

    var creditCards by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            currentUser.uid.let { userId ->
                val userCreditCardsRef = db.collection("users").document(userId)
                    .collection("creditCards")
                userCreditCardsRef.addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore", "Error getting credit cards: ${error.message}")
                        return@addSnapshotListener
                    }

                    val cardsList = mutableListOf<Map<String, Any>>() // Изменили тип значения moneyAmount на Any

                    value?.documents?.forEach { document ->
                        val cardData = hashMapOf<String, Any>() // Изменили тип значения moneyAmount на Any
                        cardData["cardNumber"] = document["cardNumber"] as? String ?: ""
                        cardData["cardHolder"] = document["cardHolder"] as? String ?: ""
                        cardData["cardName"] = document["cardName"] as? String ?: ""
                        cardData["moneyAmount"] = (document["moneyAmount"] as? Number)?.toFloat() ?: 0.0f // Преобразовали в Float
                        cardData["cardType"] = document["cardType"] as? String ?: ""
                        cardsList.add(cardData)
                    }

                    creditCards = cardsList.map { cardData ->
                        cardData.mapValues { it.value.toString() }
                    }

                }
            }
        }
    }


    LazyRow {
        items(creditCards) { cardData ->
            CardItem(cardData)
        }
    }
}

@Composable
fun CardItem(
    cardData: Map<String, String>
) {
    var image = painterResource(id = R.drawable.ic_visa)
    var gradientStart = PurpleStart
    var gradientEnd = PurpleEnd
    if (cardData["cardType"] == "MASTER CARD") {
        image = painterResource(id = R.drawable.ic_mastercard)
        gradientStart = BlueStart
        gradientEnd = BlueEnd
    } else if (cardData["cardType"] == "VISA") {
        gradientStart = GreenStart
        gradientEnd = GreenEnd
    }

    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(getGradient(gradientStart, gradientEnd))
                .width(250.dp)
                .height(160.dp)
                .clickable {}
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = image,
                contentDescription = cardData["cardName"],
                modifier = Modifier.width(60.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = cardData["cardName"] ?: "",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$ ${"%.2f".format(cardData["moneyAmount"]?.toFloatOrNull() ?: 0.0f)}",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = cardData["cardNumber"] ?: "",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}