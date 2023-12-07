package com.app.activeparks.ui.registration.model

/**
 * Created by O.Dziuba on 23.11.2023.
 */
data class StateForgotPassword(
    var isEmail: Boolean = true,
    var isSms: Boolean = false,
    var isPassword: Boolean = false,
    var isComplete: Boolean = false
)
