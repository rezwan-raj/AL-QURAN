package com.quantum.quran.uiClass

import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.quantum.quran.R

class Custo_Toa(private val activity: Activity) {

    companion object {
        const val TOAST_POSITIVE = 0
        const val TOAST_NEGATIVE = 1
    }

    fun show(text: String, type: Int) {
        activity.run {
            val layout = layoutInflater.inflate(
                R.layout.lout_toast,
                findViewById(R.id.toast_linear)
            )
            val  toastLayout = layout.findViewById<ImageView>(R.id.image)
            when(type) {
                TOAST_POSITIVE -> {
                    layout.setBackgroundResource(R.drawable.to_ast_posit_iv_e_postitive)
                    toastLayout.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.iicc_ch_eck, null)
                    )
                }
                TOAST_NEGATIVE ->  {
                    layout.setBackgroundResource(R.drawable.to_a_st_neg)
                    toastLayout.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.er_r_or, null)
                    )
                }
            }
            layout.findViewById<TextView>(R.id.text).text = text
            val toast = Toast(this)
            toast.duration = Toast.LENGTH_LONG
            @Suppress("DEPRECATION")
            toast.view = layout
            toast.show()
        }
    }
}