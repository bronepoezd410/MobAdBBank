package com.ahmedapps.bankningappui.data.rules

object Validator {

    fun validateFirstName(fName:String) :ValidationResult{
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length>=4)
        )
    }

    fun validateLastName(lName:String):ValidationResult{
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length>=4)
        )
    }

    fun validateEmail(email:String):ValidationResult{
        return ValidationResult(
            (!email.isNullOrEmpty() && email.length>=4)
        )
    }


    fun validatePassword(password:String):ValidationResult{
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length>=8)
        )
    }

    fun validatePrivacyPolicyAcceptance(statusValue:Boolean):ValidationResult{
        return ValidationResult(
            statusValue
        )
    }

}

data class ValidationResult(
    val status :Boolean = false

)