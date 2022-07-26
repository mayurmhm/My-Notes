package projects.mayurmhm.mynotes.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import projects.mayurmhm.mynotes.R

/**
 * Launcher Activity
 */
class SplashScreenActivity : AppCompatActivity() {
    // timeout
    private val splashTimeOut = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // check for logged in user in shared prefs
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)
    }
}