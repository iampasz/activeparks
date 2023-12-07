package com.app.activeparks.data.repository.sesion.model

/**
 * Created by O.Dziuba on 28.11.2023.
 */
data class GsonSession(
    val accessToken: String,
    val refreshToken: String

) : ISession {
    override fun accessToken() = accessToken

    override fun refreshToken() = refreshToken

}
