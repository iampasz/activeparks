package com.app.activeparks.data.repository.sesion.model

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface ISession {
    fun accessToken(): String
    fun refreshToken(): String
}