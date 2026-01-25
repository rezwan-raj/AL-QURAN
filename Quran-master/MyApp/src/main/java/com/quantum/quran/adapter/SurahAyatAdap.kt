package com.quantum.quran.adapter

import android.app.Activity
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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.quantum.quran.R
import com.quantum.quran.database.Application_D
import com.quantum.quran.model.`AL-QURAN`
import com.quantum.quran.process.Aodio_Proce
import com.quantum.quran.sql.`Al-Quran_H`
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SurahAyatAdap(val context: Context, val name: String, val meaning: String,
                    private val details: String, val data: ArrayList<`AL-QURAN`>):
    RecyclerView.Adapter<SurahAyatAdap.ViewHolder>() {

    private var reading = -1
    private val quran = `Al-Quran_H`(context)

    inner class ViewHolder(view: View, val type: Int): RecyclerView.ViewHolder(view) {
        var name: TextView? = null
        var details: TextView? = null
        var meaning: TextView? = null

        var pron: TextView? = null
        var play: ImageView? = null
        var share: ImageView? = null
        var arabic: TextView? = null
        var ayatNo: TextView? = null
        var bookmark: ImageView? = null
        var translation: TextView? = null

        init {
            if (type > 0) {
                pron = view.findViewById(R.id.pron)
                play = view.findViewById(R.id.play)
                share = view.findViewById(R.id.share)
                arabic = view.findViewById(R.id.arabic)
                ayatNo = view.findViewById(R.id.ayat_no)
                bookmark = view.findViewById(R.id.bookmark)
                translation = view.findViewById(R.id.translation)

                play!!.clipToOutline = true
                share!!.clipToOutline = true
                bookmark!!.clipToOutline = true
            } else {
                name = view.findViewById(R.id.name)
                details = view.findViewById(R.id.details)
                meaning = view.findViewById(R.id.meaning)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahAyatAdap.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    if (viewType == 0)
                        R.layout.lout_ayat_header
                    else {
//                        if (viewType == reading)
                        R.layout.lout_ayat_reading
//                        else  R.layout.layout_ayat_reading
                    },
                    parent, false
                ), viewType
        )
    }

    override fun onBindViewHolder(holder: SurahAyatAdap.ViewHolder, position: Int) {
        if (getItemViewType(position) > 0) {
            data[position].let {
                holder.run {
                    if (it.reading) {
                        itemView.findViewById<LinearLayout>(R.id.main_layout)
                            .background = ResourcesCompat.getDrawable(
                            context.resources, R.drawable.rea_di_ng_reading_bg, null)
                    } else itemView.findViewById<LinearLayout>(R.id.main_layout).background = null
                    ayatNo?.text = numberFormat.format(it.ayat)//it.ayat.toString()
                    arabic?.text = if (Application_D(context).arabic) it.utsmani else it.indopak

                    if (Application_D(context).transliteration) {
                        pron?.text = it.latin
                        pron?.visibility = View.VISIBLE
                        translation?.visibility = View.VISIBLE
                        translation(translation, it)
                    } else {
                        translation?.visibility = View.GONE
                        pron?.visibility = View.VISIBLE
                        setPron(pron, it)
                    }

                    maintainClicks(play, share, bookmark, it)
                    arabic?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).arabicFontSize)
                    pron?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).transliterationFontSize)
                    translation?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).translationFontSize)
                }
            }
        } else {
            holder.name?.text = name
            holder.details?.text = details
            holder.meaning?.text = meaning
        }
    }

    private fun translation(translation: TextView?, it: `AL-QURAN`) {
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
    }

    private fun setPron(pron: TextView?, it: `AL-QURAN`) {
        pron?.text =
            when(Application_D(context).translation) {
                Application_D.TAISIRUL -> textToHtml(it.terjemahan)
                Application_D.MUHIUDDIN -> textToHtml(it.jalalayn)
                Application_D.ENGLISH -> textToHtml(it.englishT)
                else -> {
                    pron?.visibility = View.GONE
                    ""
                }
            }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun textToHtml(it: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        else Html.fromHtml(it)
    }

    private fun maintainClicks(play: ImageView?, share: ImageView?, bookmark: ImageView?, it: `AL-QURAN`) {
        share?.setOnClickListener { v->
            var text = "${context.resources.getString(R.string.surah)}: $name, " +
                    "${context.resources.getString(R.string.ayat)}: ${it.ayat}\n\n"
            text += if (Application_D(context).arabic) it.utsmani else it.indopak
            text += "\n\n${context.resources.getString(R.string.meaning)} " +
                    ":  "+when(Application_D(context).translation) {
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
                if (this.quran.readAyatNo(it.pos)!!.englishPro == "T")
                    R.drawable.icc_bline_bmark_24
                else R.drawable.icc_base_line_book_mark_border_24,
                null
            )
        )

        bookmark?.setOnClickListener { _->
            if (this.quran.readAyatNo(it.pos)!!.englishPro == "T") {
                this.quran.insertData(it, "F")
            } else this.quran.insertData(it, "T")
            bookmark.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    if (this.quran.readAyatNo(it.pos)!!.englishPro == "T")
                        R.drawable.icc_bline_bmark_24
                    else R.drawable.icc_base_line_book_mark_border_24,
                    null
                )
            )
        }

        play?.setOnClickListener { _->
            Aodio_Proce(context as Activity).play(it.surah, it.ayat)
        }
    }

    fun read(pos: Int) {
        data[pos] = data[pos].apply { reading = true }
        if (reading > 0) {
            data[reading] = data[reading].apply { reading = false }
        }
        notifyItemChanged(pos)
        notifyItemChanged(reading)
        reading = pos
    }

    private val numberFormat: NumberFormat =
        NumberFormat.getInstance(Locale(Application_D(context).language))
}