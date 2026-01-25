package com.quantum.quran.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quantum.quran.R
import com.quantum.quran.database.Application_D
import com.quantum.quran.databinding.ALanguageBinding
import com.quantum.quran.theme.ApplicationTheme
import com.quantum.quran.utils.ContextUtils
import java.util.*

class LanguageActi : AppCompatActivity() {

    private lateinit var binding: ALanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationTheme(this)
        binding = ALanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener { finish() }

        when(Application_D(this).language) {
            "en" -> binding.languageGroup.check(R.id.english)
            "bn" -> binding.languageGroup.check(R.id.bangla)
            "tr" -> binding.languageGroup.check(R.id.turkish)
        }

        binding.languageGroup.setOnCheckedChangeListener { group, checkedId ->
            Application_D(this).language =
                when(checkedId) {
                    R.id.english -> "en"
                    R.id.bangla -> "bn"
                    R.id.turkish -> "tr"
                    else -> "en"
                }
            recreate()
        }
        setResult(Activity.RESULT_OK, Intent())
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(Application_D(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}