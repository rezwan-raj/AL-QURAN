package com.quantum.codingburg.myadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quantum.codingburg.R
import com.quantum.codingburg.acti.ParaActi
import com.quantum.codingburg.appdatabase.Application_D
import com.quantum.codingburg.appmodel.J_Mod
import com.quantum.codingburg.appsql.`Al-Quran_H`
import com.quantum.codingburg.appsql.Su_R_Ah_H
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ParaListAdap(val context: Context, val data: ArrayList<J_Mod>):
    RecyclerView.Adapter<ParaListAdap.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val from: TextView = view.findViewById(R.id.from)
        val count: TextView = view.findViewById(R.id.count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParaListAdap.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.lout_para,
                    parent, false
                )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ParaListAdap.ViewHolder, position: Int) {
        data[position].let {
            val quran = `Al-Quran_H`(context).readAyatNo(it.startPos)!!
            holder.run {
                count.text = numberFormat.format(it.paraNo) //"${it.paraNo}"
                name.text = "${context.resources.getString(R.string.para)} " +
                        "${numberFormat.format(it.paraNo)} -> ${numberFormat.format(
                            it.endPos+1-it.startPos)} " +
                        context.resources.getString(R.string.verses)
                from.text = "${context.resources.getString(R.string.starts_at)}: " +
                        "${Su_R_Ah_H(context).readDataAt(quran.surah)!!.name}," +
                        " "+context.resources.getString(R.string.verses)+" ${numberFormat.format(quran.ayat)}"

                holder.itemView.setOnClickListener { _->
                    ParaActi.launch(context, it.paraNo)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private val numberFormat: NumberFormat =
        NumberFormat.getInstance(Locale(Application_D(context).language))
}