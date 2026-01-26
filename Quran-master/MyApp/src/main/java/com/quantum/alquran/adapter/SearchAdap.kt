package com.quantum.alquran.adapter

import android.annotation.SuppressLint
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
import com.quantum.alquran.R
import com.quantum.alquran.activity.SurahActi
import com.quantum.alquran.database.Application_D
import com.quantum.alquran.model.`AL-QURAN`
import com.quantum.alquran.model.Sea_Mod
import com.quantum.alquran.sql.`Al-Quran_H`
import com.quantum.alquran.sql.Su_R_Ah_H

class SearchAdap(val context: Context, val data: ArrayList<Sea_Mod>):
    RecyclerView.Adapter<SearchAdap.ViewHolder>() {

    private val quran = `Al-Quran_H`(context)

    inner class ViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder(view) {
        var count: TextView? = null
        var name: TextView? = null
        var from: TextView? = null

        var arabic: TextView? = null

        var pron: TextView? = null
        var share: ImageView? = null
        var ayatNo: TextView? = null
        var surahName: TextView? = null
        var bookmark: ImageView? = null
        var translation: TextView? = null

        init {
            when (viewType) {
                SURAH -> {
                    count = view.findViewById(R.id.count)
                    name = view.findViewById(R.id.name)
                    from = view.findViewById(R.id.from)
                    arabic = view.findViewById(R.id.arabic)
                }
                else -> {
                    pron = view.findViewById(R.id.pron)
                    share = view.findViewById(R.id.share)
                    arabic = view.findViewById(R.id.arabic)
                    ayatNo = view.findViewById(R.id.ayat_no)
                    bookmark = view.findViewById(R.id.bookmark)
                    surahName = view.findViewById(R.id.surah_name)
                    translation = view.findViewById(R.id.translation)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdap.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    when(viewType) {
                        SURAH -> R.layout.lout_surah_name
                        else -> R.layout.l_ayat
                    }, parent, false
                ), viewType
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchAdap.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            SURAH -> {
                holder.count?.text = data[position].pos.toString()
                holder.name?.text = data[position].name
                holder.from?.text = "${data[position].revelation}   |   ${data[position].verse} VERSES"
                holder.arabic?.text = data[position].nameAr

                holder.itemView.setOnClickListener {
                    SurahActi.launch(context, data[position].pos-1, 0)
                }
            }
            else -> {
                data[position].let {
                    holder.run {
                        ayatNo?.text = it.ayat.toString()
                        arabic?.text = if (Application_D(context).arabic)
                            textToHtml(it.utsmani) else textToHtml(it.indopak)

                        if (Application_D(context).transliteration) {
                            pron?.text = it.latin
                            pron?.visibility = View.VISIBLE
                        } else {
                            pron?.visibility = View.GONE
                        }

                        surahName?.text = Su_R_Ah_H(context).readDataAt(it.surah)?.name

                        translation?.visibility = View.VISIBLE
                        translation?.text = textToHtml(it.trans)

                        maintainClicks(share, bookmark, holder, it)

                        arabic?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).arabicFontSize)
                        pron?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).transliterationFontSize)
                        translation?.setTextSize(TypedValue.COMPLEX_UNIT_SP, Application_D(context).translationFontSize)
                    }
                }
            }
        }
    }

    private fun maintainClicks(
        share: ImageView?,
        bookmark: ImageView?,
        holder: ViewHolder,
        it: Sea_Mod) {
        share?.setOnClickListener { v ->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, it.indopak)
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

        bookmark?.setImageDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                if (quran.readAyatNo(it.pos)!!.englishPro == "T")
                    R.drawable.icc_bline_bmark_24
                else R.drawable.icc_base_line_book_mark_border_24,
                null
            )
        )

        bookmark?.setOnClickListener { _ ->
            if (quran.readAyatNo(it.pos)!!.englishPro == "T") {
                quran.insertData(modelExchange(it), "F")
            } else quran.insertData(modelExchange(it), "T")
            holder.bookmark?.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    if (quran.readAyatNo(it.pos)!!.englishPro == "T")
                        R.drawable.icc_bline_bmark_24
                    else R.drawable.icc_base_line_book_mark_border_24,
                    null
                )
            )
        }

        holder.itemView.setOnClickListener { _ ->
            SurahActi.launch(context, it.surah - 1, it.ayat)
        }
    }

    private fun modelExchange(temp: Sea_Mod): `AL-QURAN` {
        return `AL-QURAN`(
            pos = temp.pos,
            surah = temp.surah,
            ayat = temp.ayat,
            indopak = temp.indopak,
            utsmani = temp.utsmani,
            jalalayn = temp.jalalayn,
            latin = temp.latin,
            terjemahan = temp.terjemahan,
            englishPro = temp.englishPro,
            englishT = temp.englishT
        )
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun textToHtml(it: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
        else Html.fromHtml(it)
    }

    companion object {
        const val AYAT = 1
        const val SURAH = 0
    }
}