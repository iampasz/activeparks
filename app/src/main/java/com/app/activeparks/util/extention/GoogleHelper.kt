package com.app.activeparks.util.extention

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

/**
 * Created by O.Dziuba on 30.01.2024.
 */


val clientId = "97103190835-ls9vnubpv1oi4kn76odpuhn4bechpn71.apps.googleusercontent.com"

fun logOut(activity: Activity) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    mGoogleSignInClient.signOut()
}