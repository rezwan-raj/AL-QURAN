package com.quantum.codingburg.acti

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import com.quantum.codingburg.R
import com.quantum.codingburg.appdatabase.Application_D
import com.quantum.codingburg.databinding.ASettingsBinding
import com.quantum.codingburg.apptheme.Appli_Them
import com.quantum.codingburg.apputils.Context_U
import java.util.*

class SettingsActi : AppCompatActivity() {

    private lateinit var applicationData: Application_D
    private lateinit var binding: ASettingsBinding


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when(result.resultCode) {
                RESULT_OK -> {
                    reCreate()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appli_Them(this)
        binding = ASettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.clipToOutline = true
        binding.green.clipToOutline = true
        binding.blue.clipToOutline = true
        binding.orange.clipToOutline = true
        binding.openFont.clipToOutline = true

        binding.back.setOnClickListener { finish() }

        applicationData = Application_D(this)

        binding.language.setOnClickListener {
            startForResult.launch(Intent(this, LanguageActi::class.java))
        }

        binding.openFont.setOnClickListener {
            if (binding.fontSizeExpandable.isExpanded) {
//                binding.openFont.animate()
//                    .rotation(0f)
//                    .start()
                binding.openFont.rotation = 0f
                binding.fontSizeExpandable.collapse()
            }
            else {
//                binding.openFont.animate()
//                    .rotation(180f)
//                    .start()
                binding.openFont.rotation = 180f
                binding.fontSizeExpandable.expand()
            }
        }

        binding.about.setOnClickListener {
            startActivity(
                Intent(
                    this, AboutActi::class.java
                )
            )
        }

        binding.feedback.setOnClickListener {
         /*   startActivity(
                Intent.createChooser(
                    Intent(
                        Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",
                            EMAIL, null
                        )
                    ), "Send email...")
            )*/
        }

        binding.switchTheme.isChecked = applicationData.darkTheme
        binding.switchTheme.setOnCheckedChangeListener{ _, isChecked ->
            applicationData.darkTheme = isChecked
            reCreate()
        }

        //
        checkColor()
        initColorClick()
        //
        checkArabic()
        initArabicClick()
        //
        checkTranslation()
        initTranslationClick()
        //
        checkTransliteration()
        initTransliterationClick()
        //
        checkFontSeekBar()
        initFontSeekBarSeek()

    }

    private fun checkColor() {
        binding.greenCheck.visibility = View.GONE
        binding.blueCheck.visibility = View.GONE
        binding.orangeCheck.visibility = View.GONE
        when (applicationData.primaryColor) {
            Application_D.PURPLE -> binding.greenCheck.visibility = View.VISIBLE
            Application_D.BLUE -> binding.blueCheck.visibility = View.VISIBLE
            Application_D.ORANGE -> binding.orangeCheck.visibility = View.VISIBLE
        }
    }

    private fun initColorClick() {
        binding.green.setOnClickListener {
            applicationData.primaryColor = Application_D.PURPLE
            reCreate()
        }
        binding.blue.setOnClickListener {
            applicationData.primaryColor = Application_D.BLUE
            reCreate()
        }
        binding.orange.setOnClickListener {
            applicationData.primaryColor = Application_D.ORANGE
            reCreate()
        }
    }

    private fun checkArabic() {
        binding.arabicGroup.check(
            if (applicationData.arabic)
                R.id.uthmani else R.id.indoPk
        )
    }

    private fun initArabicClick() {
        binding.arabicGroup.setOnCheckedChangeListener { group, checkedId ->
            applicationData.arabic = checkedId != R.id.indoPk
        }
    }

    private fun checkTranslation() {
        binding.translationSwitch.isChecked = true
        binding.translationExpandable.expand(false)
        binding.translationGroup.check(
            when(applicationData.translation) {
                Application_D.TAISIRUL -> R.id.c_taisirul
                Application_D.MUHIUDDIN -> R.id.c_muhiuddin
                Application_D.ENGLISH -> R.id.c_eng
                else -> {
                    binding.translationExpandable.collapse(false)
                    binding.translationSwitch.isChecked = false
                    R.id.c_taisirul
                }
            }
        )
    }

    private fun initTranslationClick() {
        binding.translationGroup.setOnCheckedChangeListener { _, checkedId ->
            applicationData.translation = when(checkedId) {
                R.id.c_taisirul -> Application_D.TAISIRUL
                R.id.c_muhiuddin -> Application_D.MUHIUDDIN
                R.id.c_eng -> Application_D.ENGLISH
                else -> Application_D.OFF
            }
        }

        binding.translationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.translationGroup.check(R.id.c_muhiuddin)
                applicationData.translation = Application_D.MUHIUDDIN
                binding.translationExpandable.expand()
            } else {
                applicationData.translation = Application_D.OFF
                binding.translationExpandable.collapse()
            }
        }
    }

    private fun checkTransliteration() {
        binding.transliterationSwitch.isChecked = applicationData.transliteration
    }

    private fun initTransliterationClick() {
        binding.transliterationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            applicationData.transliteration = isChecked
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkFontSeekBar() {
        val arabic = applicationData.arabicFontSize.toInt()
        binding.arabicSizeText.text = "${arabic}sp"
        binding.arabicTranslationSeek.progress = (arabic-16)/2

        val transliteration = applicationData.transliterationFontSize.toInt()
        binding.transliterationSizeText.text = "${transliteration}sp"
        binding.transliterationFontSeek.progress = (transliteration-16)/2

        val translation = applicationData.translationFontSize.toInt()
        binding.translationSizeText.text = "${translation}sp"
        binding.translationSeek.progress = (translation-16)/2
    }

    private fun initFontSeekBarSeek() {
        binding.arabicTranslationSeek.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean) {
                    applicationData.arabicFontSize = ((progress*2)+16).toFloat()
                    binding.arabicSizeText.text = "${(progress*2)+16}sp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.e("onStartTrackingTouch", "onStartTrackingTouch")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.e("onStopTrackingTouch", "onStopTrackingTouch")
                }

            }
        )

        binding.transliterationFontSeek.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean) {
                    applicationData.transliterationFontSize = ((progress*2)+16).toFloat()
                    binding.transliterationSizeText.text = "${(progress*2)+16}sp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.e("Start", "Tracking")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.e("Stop", "Not Tracking")
                }

            }
        )

        binding.translationSeek.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean) {
                    applicationData.translationFontSize = ((progress*2)+16).toFloat()
                    binding.translationSizeText.text = "${(progress*2)+16}sp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.e("Start", "Tracking")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.e("Stop", "Not Tracking")
                }

            }
        )

        binding.termsCondition.setOnClickListener {
            /*TACPP(this).launch(1)*/
        }

        binding.policy.setOnClickListener {
           /* TACPP(this).launch(0)*/
        }
    }

    private fun reCreate() {
        setResult(Activity.RESULT_OK, Intent())
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(
                    this,
                    SettingsActi::class.java
                )
            )
            Objects.requireNonNull(
                overridePendingTransition(
                    R.anim.f_in,
                    R.anim.fa_o
                )
            )
            finish()
        }, 150)
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(Application_D(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = Context_U.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}