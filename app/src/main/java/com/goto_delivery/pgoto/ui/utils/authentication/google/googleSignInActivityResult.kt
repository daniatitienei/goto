package com.goto_delivery.pgoto.ui.utils.authentication.google

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

@Composable
fun googleSignInActivityResult(onContinue: (GoogleSignInAccount?) -> Unit) =
    rememberLauncherForActivityResult(contract = AuthResult()) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)

            onContinue(account)
        } catch (e: ApiException) {
            Log.d("login", e.localizedMessage!!)
        }
    }