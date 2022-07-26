package projects.mayurmhm.mynotes.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import projects.mayurmhm.mynotes.utils.Constants.PREFS_TOKEN_FILE
import projects.mayurmhm.mynotes.utils.Constants.USER_TOKEN
import javax.inject.Inject

// Token Manager
class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    // save token
    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // get token
    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}