package com.app.activeparks.data.model.registration

/**
 * Created by O.Dziuba on 06.12.2023.
 */
data class ForgotPasswordRequest(
    var phoneLogin: String = "",
    var typeId: Int = 1
)
