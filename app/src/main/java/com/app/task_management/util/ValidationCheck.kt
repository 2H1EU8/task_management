package com.app.task_management.util

import android.util.Patterns

fun emailValidation(email: String): RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")

    val matcher = Patterns.EMAIL_ADDRESS.matcher(email)
    if (!matcher.matches())
        return RegisterValidation.Failed("Wrong email format")


    return RegisterValidation.Success
}

fun passValidation(pass: String, rePass: String): RegisterValidation {
    if(pass.isEmpty() || rePass.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")

    if (pass.length <6)
        return RegisterValidation.Failed("Password is too short")

    return RegisterValidation.Success
}