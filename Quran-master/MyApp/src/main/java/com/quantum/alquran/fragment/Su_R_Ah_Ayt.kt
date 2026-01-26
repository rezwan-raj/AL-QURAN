package com.quantum.alquran.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.alquran.R
import com.quantum.alquran.adapter.SurahAyatAdap
import com.quantum.alquran.application.Cnstnt
import com.quantum.alquran.constant.Nam
import com.quantum.alquran.database.Application_D
import com.quantum.alquran.database.Lst_Read
import com.quantum.alquran.databinding.FSurahAyatBinding
import com.quantum.alquran.model.`AL-QURAN`
import com.quantum.alquran.sql.`Al-Quran_H`
import com.quantum.alquran.sql.Su_R_Ah_H
import com.quantum.alquran.utils.Keyb_Uti
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.LinearSmoothScroller

import androidx.recyclerview.widget.RecyclerView


class Su_R_Ah_Ayt(private val position: Int, val ayat: Int, private val scroll: Boolean) : Fragment() {

    private var search = ""
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private val data = ArrayList<`AL-QURAN`>()
    private var ayatFollower: BroadcastReceiver? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var adapterSurah: SurahAyatAdap? = null
    private var binding: FSurahAyatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FSurahAyatBinding.inflate(inflater, container, false)
        binding?.searchIcon?.clipToOutline = true

        layoutManager = LinearLayoutManager(requireContext())

        smoothScroller = object : LinearSmoothScroller(activity) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            data.add(`AL-QURAN`(0, 0, 0, "", "",
                    "", "", "", "", "")
            )
            data.addAll(`Al-Quran_H`(requireContext()).readSurahNo(position + 1))
            val temp = Su_R_Ah_H(requireContext()).readData()[position]
            val t = "${revelation(temp.revelation)}   |   ${NumberFormat.getInstance(
                Locale(Application_D(requireContext()).language)).format(temp.verse)}" +
                    "  " + resources.getString(R.string.verses)
            adapterSurah = SurahAyatAdap(
                requireContext(),
                temp.name, Nam().data()[position],
                t, data
            )

            activity?.runOnUiThread {
                binding?.ayatRecycler?.layoutManager = layoutManager
                binding?.ayatRecycler?.adapter = adapterSurah
                if (scroll) binding?.ayatRecycler?.scrollToPosition(ayat)
            }
        }

        click()

        binding?.searchIcon?.setOnClickListener {
            binding?.searchText?.text.toString().let { e->
                search = if (e.isNotEmpty()) {
                    try {
                        filter(e.toInt())
                    } catch (ex: Exception) {
                        filter(e)
                    }
                    e
                } else {
                    filter("")
                    ""
                }
            }
            closeKeyboard(binding?.searchText)
        }

        return binding?.root
    }

    private fun revelation(revelation: String): String {
        return if (revelation == "Meccan")
            resources.getString(R.string.meccan)
        else resources.getString(R.string.medinan)
    }

    private fun click() {
        binding?.searchText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val key = binding?.searchText?.text.toString()
                search = if (key.isNotEmpty()) {
                    try {
                        filter(key.toInt())
                    } catch (ex: Exception) {
                        filter(key)
                    }
                    key
                } else {
                    filter("")
                    ""
                }
                closeKeyboard(binding?.searchText)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun filter(pos: Int) {
        val l = data.size
        data.clear()
        adapterSurah?.notifyItemRangeRemoved(0, l)
        data.add(`AL-QURAN`(0, 0, 0, "", "",
                "", "", "", "", "")
        )
        data.addAll(`Al-Quran_H`(requireContext()).readSurahNo(position + 1))
        adapterSurah?.notifyItemRangeInserted(0, data.size)
        if (pos < data.size) {
            binding?.ayatRecycler?.scrollToPosition(pos)
        }
    }

    private fun filter(filter: String) {
        adapterSurah?.notifyItemRangeRemoved(0, data.size)
        CoroutineScope(Dispatchers.Default).launch {
            data.clear()
            data.add(`AL-QURAN`(0, 0, 0, "", "",
                "", "", "", "", "")
            )
            val a = `Al-Quran_H`(requireContext()).readData().filter {
                when(Application_D(requireContext()).translation) {
                    Application_D.TAISIRUL -> it.terjemahan
                    Application_D.MUHIUDDIN -> it.jalalayn
                    else -> it.englishT.lowercase(Locale.getDefault())
                }.contains(filter.lowercase())
            }
            a.forEach {
                val temp = when(Application_D(requireContext()).translation) {
                    Application_D.TAISIRUL -> it.terjemahan
                    Application_D.MUHIUDDIN -> it.jalalayn
                    else -> it.englishT
                }
                val start = temp.lowercase(Locale.getDefault())
                    .indexOf(filter.lowercase(Locale.getDefault()))
                val translation = "${temp.substring(0, start)}<b><font color=#2979FF>" +
                        "${temp.substring(start, start+filter.length)}</font></b>${temp.substring(start+filter.length)}"

                data.add(
                    `AL-QURAN`(
                        pos = it.pos,
                        surah = it.surah,
                        ayat = it.ayat,
                        indopak = it.indopak,
                        utsmani = it.utsmani,
                        jalalayn = if (Application_D(requireContext()).translation
                            == Application_D.MUHIUDDIN) translation else it.jalalayn,
                        latin = it.latin,
                        terjemahan = if (Application_D(requireContext()).translation
                            == Application_D.TAISIRUL) translation else it.terjemahan,
                        englishPro = it.englishPro,
                        englishT = if (Application_D(requireContext()).translation
                            == Application_D.ENGLISH) translation else it.englishT
                    )
                )
            }
            requireActivity().runOnUiThread {
                adapterSurah?.notifyItemRangeRemoved(0, data.size)
                Toast.makeText(context,
                    "${data.size} Search result found."
                    , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun closeKeyboard(edit: EditText?) {
        edit?.let {
            it.clearFocus()
            Keyb_Uti.hideKeyboard(it)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", (Cnstnt.SURAH+position).toString())
        ayatFollower = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    binding?.appBar?.setExpanded(false, true)
                    smoothScroller.targetPosition = intent.getIntExtra("AYAT", 0)+1
                    layoutManager.startSmoothScroll(smoothScroller)
                    adapterSurah?.read(intent.getIntExtra("AYAT", 0)+1)
                }
            }
        }

        activity?.registerReceiver(ayatFollower, IntentFilter(Cnstnt.SURAH+position))
    }

    override fun onPause() {
        activity?.unregisterReceiver(ayatFollower)
        if (Lst_Read(requireContext()).surahNo == position) {
            Lst_Read(requireContext()).ayatNo =
                layoutManager.findFirstCompletelyVisibleItemPosition().let {
                    if (it < 0)
                        layoutManager.findFirstVisibleItemPosition()
                    else it
            }
        }
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }
}