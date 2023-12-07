package com.app.activeparks.util

import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

/**
 * Created by O.Dziuba on 07.12.2023.
 */
open class EasyFacebookCallback : FacebookCallback<LoginResult> {
    override fun onSuccess(loginResult: LoginResult) { }

    override fun onCancel() { }

    override fun onError(error: FacebookException) {  }
}