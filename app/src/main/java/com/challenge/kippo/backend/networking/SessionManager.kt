package com.challenge.kippo.backend.networking

import android.content.Context
import android.content.SharedPreferences
import com.challenge.kippo.R
import com.challenge.kippo.util.Constants

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs : SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(Constants.Keys.USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(Constants.Keys.USER_TOKEN, null)
    }
}