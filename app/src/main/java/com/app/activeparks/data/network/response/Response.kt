package com.app.activeparks.data.network.response

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

fun <T> Response<T>.parseErrorBody(): ErrorBody {
    return try {
        Gson().fromJson(errorBody()?.charStream(), ErrorBody::class.java)
    } catch (e: Exception) {
        ErrorBody(e.message)
    }
}

inline fun <reified T> String.fromJson(): T = Gson().fromJson(this, object : TypeToken<T>() {}.type)