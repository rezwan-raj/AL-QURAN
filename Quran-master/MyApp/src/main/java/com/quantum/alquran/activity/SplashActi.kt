package com.quantum.alquran.activity

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.quantum.minar.database.User_D
import com.quantum.alquran.R
import com.quantum.alquran.database.Application_D
import com.quantum.alquran.sql.`Al-Quran_H`
import com.quantum.alquran.sql.Su_R_Ah_H
import com.quantum.alquran.theme.Appli_Them
import com.quantum.alquran.utils.Context_U
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class SplashActi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appli_Them(this)
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
        return if (User_D(this@SplashActi).quranLaunched) {
            try {
                if (Su_R_Ah_H(this@SplashActi).readData().size == 114
                    && `Al-Quran_H`(this@SplashActi).readData().size == 6236)
                    QuranMainActi::class.java
                else QuranActi::class.java
            } catch (e: Exception) {
                QuranActi::class.java
            }
        } else QuranActi::class.java
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(Application_D(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = Context_U.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}