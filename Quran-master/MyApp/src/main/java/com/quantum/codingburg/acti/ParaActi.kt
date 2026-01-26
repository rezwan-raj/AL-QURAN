package com.quantum.codingburg.acti

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.quantum.codingburg.R
import com.quantum.codingburg.myadapter.PagerAdap
import com.quantum.codingburg.appconstant.P
import com.quantum.codingburg.appdatabase.Application_D
import com.quantum.codingburg.databinding.AParaBinding
import com.quantum.codingburg.appfragment.Pa_R_a_Ay
import com.quantum.codingburg.apptheme.Appli_Them
import com.quantum.codingburg.apputils.Context_U
import java.text.NumberFormat
import java.util.*

class ParaActi : AppCompatActivity() {

    companion object {
        fun launch(context: Context, paraNo: Int) {
            context.startActivity(
                Intent(
                    context,
                    ParaActi::class.java
                ).putExtra("PARA_NO", paraNo)
            )
        }
    }
    private var binding: AParaBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appli_Them(this)
        binding = AParaBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.back?.setOnClickListener { finish() }

        val numberFormat: NumberFormat =
            NumberFormat.getInstance(Locale(Application_D(this).language))
        binding?.qPager?.adapter = PagerAdap(
            supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ).apply {
            P().Position().reversed()
                .forEach {
                    addFragment(Pa_R_a_Ay(it.paraNo))
                    binding?.tabLayout?.newTab()?.setText(
                        resources.getString(R.string.para) +
                            " -> ${numberFormat.format(it.paraNo)}")?.let { it1 ->
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
            }
        })

        binding?.qPager?.offscreenPageLimit = 3
        binding?.qPager?.currentItem = P().Position().size -
                intent.getIntExtra("PARA_NO", 0)
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(Application_D(newBase!!).language)
        val localeUpdatedContext: ContextWrapper = Context_U.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}