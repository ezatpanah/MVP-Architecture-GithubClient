package com.ezatpanah.githubclient.utils

import android.content.Context
import android.content.SharedPreferences
import com.ezatpanah.githubclient.utils.Constants.PREF_ACCESS_TOKEN
import com.ezatpanah.githubclient.utils.Constants.PREF_MODE
import com.ezatpanah.githubclient.utils.Constants.PREF_NAME

class SharedPref(context: Context) {
    private var preference = context.getSharedPreferences(PREF_NAME, PREF_MODE)

    var accessToken: String?
        get() = preference.getString(PREF_ACCESS_TOKEN.first, PREF_ACCESS_TOKEN.second)
        set(value) = preference.edit{
            it.putString(PREF_ACCESS_TOKEN.first, value)
        }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
    val editor = edit()
    operation(editor)
    editor.apply()
}