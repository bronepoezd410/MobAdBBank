package com.ahmedapps.bankningappui.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ahmedapps.bankningappui.data.rules.Validator
import com.ahmedapps.bankningappui.navigation.BroBankAppRouter
import com.ahmedapps.bankningappui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignupViewModel : ViewModel () {

    private val TAG = SignupViewModel::class.simpleName
    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    fun onEvent(event:SignupUIEvent){
        validateDataWithRules()
        when(event){
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }
            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }

            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()
            }

            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()
            }

            is SignupUIEvent.RegisterButtonClicked -> {
                signUp()
            }

            is SignupUIEvent.PrivacyPolicyCheckBoxClicked -> {
                registrationUIState.value = registrationUIState.value.copy(
                    privacyPolicyAccepted = event.status
                )
            }
            else -> {}
        }

    }

    private fun signUp() {
        Log.d(TAG, "Inside_printState")
        printState()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password

        )

    }

    private fun validateDataWithRules(){
        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )
        val lNameResult = Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )
        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )

        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )

        val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(
            statusValue = registrationUIState.value.privacyPolicyAccepted
        )

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "privacyPolicyResult = $privacyPolicyResult")


        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
            privacyPolicyError = privacyPolicyResult.status
        )

        allValidationsPassed.value = fNameResult.status && lNameResult.status && emailResult.status && passwordResult.status && privacyPolicyResult.status
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.value.toString())
    }

    private fun createUserInFirebase(email: String, password: String) {
        signUpInProgress.value = true

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                signUpInProgress.value = false

                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid

                    // Создаем документ для нового пользователя в коллекции "users"
                    userId?.let {
                        val userDocument = FirebaseFirestore.getInstance().collection("users").document(it)

                        val firstName = registrationUIState.value.firstName
                        val lastName = registrationUIState.value.lastName

                        // Добавляем информацию о пользователе в документ
                        val userData = hashMapOf(
                            "email" to email,
                            "first_name" to firstName,
                            "last_name" to lastName
                            // Другие поля, которые вы хотите сохранить, например, имя пользователя, возраст и т.д.
                        )

                        userDocument.set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Пользователь успешно добавлен в Firestore")

                                val walletData = hashMapOf(
                                    "amount" to "0"
                                    // Другие поля, которые вы хотите сохранить, например, историю транзакций и т.д.
                                )
                                // Создаем коллекцию "wallet" в документе пользователя и добавляем в нее информацию о кошельке
                                userDocument.collection("wallet").document("wallet_info").set(walletData)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Кошелек пользователя успешно создан")

                                        BroBankAppRouter.navigateTo(Screen.HomeScreen)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Ошибка при создании кошелька пользователя", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Ошибка при добавлении пользователя в Firestore", e)
                            }
                    }
                } else {
                    Log.w(TAG, "Ошибка при создании пользователя: ", task.exception)
                }
            }
            .addOnFailureListener { e ->
                signUpInProgress.value = false
                Log.d(TAG, "Inside_OnFailureListener")
                Log.d(TAG, " Exception = ${e.localizedMessage}")
            }
    }


}