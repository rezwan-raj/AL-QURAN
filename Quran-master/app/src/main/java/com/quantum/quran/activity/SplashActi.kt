package com.quantum.quran.activity

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.quantum.minar.database.UserData
import com.quantum.quran.R
import com.quantum.quran.database.ApplicationData
import com.quantum.quran.sql.QuranHelper
import com.quantum.quran.sql.SurahHelper
import com.quantum.quran.theme.ApplicationTheme
import com.quantum.quran.utils.ContextUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class SplashActi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationTheme(this)
        setContentView(R.layout.a_splash)

        launch()
    }

    private fun launch() {
        CoroutineScope(Dispatchers.Default).launch {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(
                        this@SplashActi,
                        activity()
                    )
                )
                finish()
            },1000)
        }
    }

    private fun activity(): Class<*> {
        return if (UserData(this@SplashActi).quranLaunched) {
            try {
                if (SurahHelper(this@SplashActi).readData().size == 114
                    && QuranHelper(this@SplashActi).readData().size == 6236)
                    QuranMainActi::class.java
                else QuranActi::class.java
            } catch (e: Exception) {
                QuranActi::class.java
            }
        } else QuranActi::class.java
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(ApplicationData(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}