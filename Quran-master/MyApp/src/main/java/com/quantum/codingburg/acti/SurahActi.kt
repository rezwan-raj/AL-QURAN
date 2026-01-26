package com.quantum.codingburg.acti

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.quantum.codingburg.myadapter.PagerAdap
import com.quantum.codingburg.appdatabase.Application_D
import com.quantum.codingburg.appdatabase.Lst_Read
import com.quantum.codingburg.databinding.ASurahBinding
import com.quantum.codingburg.appfragment.Su_R_Ah_Ayt
import com.quantum.codingburg.appsql.Su_R_Ah_H
import com.quantum.codingburg.apptheme.Appli_Them
import com.quantum.codingburg.apputils.Context_U
import java.util.*

class SurahActi : AppCompatActivity() {

    companion object {
        fun launch(context: Context, surahNo: Int, ayat: Int?) {
            context.startActivity(
                Intent(
                    context,
                    SurahActi::class.java
                ).putExtra("SURAH_NO", surahNo)
                    .putExtra("AYAT", ayat)
            )
        }
    }
    private var surahNo: Int? = null
    private var binding: ASurahBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appli_Them(this)
        binding = ASurahBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.back?.setOnClickListener { finish() }

        surahNo = intent.getIntExtra("SURAH_NO", 0)

        binding?.qPager?.adapter = PagerAdap(
            supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ).apply {
            Su_R_Ah_H(this@SurahActi)
                .readData().reversed().forEach {
                    addFragment(
                        Su_R_Ah_Ayt(it.pos-1,
                        intent.getIntExtra("AYAT", 0), it.pos==(surahNo!!+1))
                    )
                    binding?.tabLayout?.newTab()?.setText(it.name)?.let { it1 ->
                        binding?.tabLayout?.addTab(it1)
                    }
                }
        }

        binding?.tabLayout?.tabGravity = TabLayout.GRAVITY_FILL

        binding?.qPager?.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(binding?.tabLayout)
        )

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                /****/
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                /****/
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                binding?.qPager?.currentItem = tab.position
                Su_R_Ah_H(this@SurahActi).readDataAt(114-tab.position)?.let {
                    Lst_Read(this@SurahActi).surahName = it.name
                }
                Lst_Read(this@SurahActi).surahNo = 113-tab.position
            }
        })

        binding?.qPager?.offscreenPageLimit = 3
        binding?.qPager?.currentItem = Su_R_Ah_H(this@SurahActi)
            .readData().size - surahNo!!-1
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(Application_D(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = Context_U.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}