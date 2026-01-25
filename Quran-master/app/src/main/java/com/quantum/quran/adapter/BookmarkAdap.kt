package com.quantum.quran.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.quantum.quran.R
import com.quantum.quran.`interface`.B_Mark
import com.quantum.quran.activity.SurahActi
import com.quantum.quran.database.Application_D
import com.quantum.quran.model.`AL-QURAN`
import com.quantum.quran.sql.`Al-Quran_H`
import com.quantum.quran.sql.Su_R_Ah_H

class BookmarkAdap(val context: Context, val data: ArrayList<`AL-QURAN`>,
                   private val BMarkInterface: B_Mark)
    : RecyclerView.Adapter<BookmarkAdap.ViewHolder>() {

    private val quran = `Al-Quran_H`(context)

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var pron: TextView? = null
        var play: ImageView? = null
        var share: ImageView? = null
        var arabic: TextView? = null
        var ayatNo: TextView? = null
        var bookmark: ImageView? = null
        var surahName: TextView? = null
        var translation: TextView? = null

        init {
            pron = view.findViewById(R.id.pron)
            play = view.findViewById(R.id.play)
            share = view.findViewById(R.id.share)
            arabic = view.findViewById(R.id.arabic)
            ayatNo = view.findViewById(R.id.ayat_no)
            bookmark = view.findViewById(R.id.bookmark)
            surahName = view.findViewById(R.id.surah_name)
            translation = view.findViewById(R.id.translation)

            play!!.clipToOutline = true
            share!!.clipToOutline = true
            bookmark!!.clipToOutline = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdap.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.l_ayat,
                    parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: BookmarkAdap.ViewHolder, position: Int) {
        data[position].let {
            holder.run {
                ayatNo?.text = it.ayat.toString()
                arabic?.text = if (Application_D(context).arabic) it.utsmani else it.indopak

                surahName?.text = Su_R_Ah_H(context).readDataAt(it.surah)?.name

                translation?.visibility = View.VISIBLE
                translation?.text =
                    when(Application_D(context).translation) {
                        Application_D.TAISIRUL -> textToHtml(it.terjemahan)
                        Application_D.MUHIUDDIN -> textToHtml(it.jalalayn)
                        Application_D.ENGLISH -> textToHtml(it.englishT)
                        else -> {
                            translation?.visibility = View.GONE
                            ""
                        }
                    }

                share?.setOnClickListener { v->
                    var text = "Surah: ${Su_R_Ah_H(context).readDataAt(it.surah)!!.name}" +
                            ", Ayat: ${it.ayat}\n\n"
                    text += if (Application_D(context).arabic) it.utsmani else it.indopak
                    text += "\n\nঅর্থ :  "+when(Application_D(context).translation) {
                        Application_D.TAISIRUL -> it.terjemahan
                        Application_D.MUHIUDDIN -> it.jalalayn
                        Application_D.ENGLISH -> it.englishT
                        else -> ""
                    }
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text)
                    sendIntent.type = "text/plain"
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }

                bookmark?.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_baseline_bookmark_24,
                        null
                    )
                )

                bookmark?.setOnClickListener { _->
                    quran.insertData(it, "F")
                    BMarkInterface.removed(position)
                }

                holder.itemView.setOnClickListener { _->
                    SurahActi.launch(context, it.surah-1, it.ayat)
                }


                setPron(pron, it)
                arabic?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).arabicFontSize)
                pron?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).transliterationFontSize)
                translation?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).translationFontSize)
            }
        }
    }

    private fun setPron(pron: TextView?, it: `AL-QURAN`) {
        if (Application_D(context).transliteration) {
            pron?.text = it.latin
            pron?.visibility = View.VISIBLE
        } else {
            pron?.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun textToHtml(it: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        else Html.fromHtml(it)
    }
}