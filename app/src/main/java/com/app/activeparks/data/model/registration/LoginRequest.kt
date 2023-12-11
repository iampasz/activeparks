package com.app.activeparks.data.model.registration

/**
 * Created by O.Dziuba on 06.12.2023.
 */
data class LoginRequest(
    var phoneLogin: String = "",
    var password: String = "",
    var typeId: Int = 1
)
