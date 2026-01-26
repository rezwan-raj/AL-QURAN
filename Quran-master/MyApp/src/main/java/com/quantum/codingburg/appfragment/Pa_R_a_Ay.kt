package com.quantum.codingburg.appfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.codingburg.myadapter.ParaAyatAdap
import com.quantum.codingburg.appconstant.Nam
import com.quantum.codingburg.appconstant.P
import com.quantum.codingburg.databinding.FParaAyatBinding
import com.quantum.codingburg.appmodel.Pa_R_Ah_Ay
import com.quantum.codingburg.appmodel.`AL-QURAN`
import com.quantum.codingburg.appsql.`Al-Quran_H`
import com.quantum.codingburg.appsql.Su_R_Ah_H
import com.quantum.codingburg.apputils.Keyb_Uti
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Pa_R_a_Ay(private val position: Int) : Fragment() {

    private var search = ""
    private val data = ArrayList<Pa_R_Ah_Ay>()
    private var suRAhH: Su_R_Ah_H? = null
    private var `al-quranH`: `Al-Quran_H`? = null
    private var adapterSurah: ParaAyatAdap? = null
    private var binding: FParaAyatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FParaAyatBinding.inflate(inflater, container, false)

        initiate()

        binding?.searchText?.setOnEditorActionListener(
            TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val key = binding?.searchText?.text.toString()
                search = if (key.isNotEmpty()) {
                    try {
                        filter(key.toInt())
                    } catch (ex: Exception) {
                        Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
                    }
                    key
                } else {
                    clearAll()
                    ""
                }
                closeKeyboard(binding?.searchText)
                return@OnEditorActionListener true
            }
            false
        })

        binding?.searchIcon?.setOnClickListener {
            searchIconClick()
        }

        return binding?.root
    }

    private fun initiate() {
        CoroutineScope(Dispatchers.Default).launch {
            suRAhH = Su_R_Ah_H(requireContext())
            `al-quranH` = `Al-Quran_H`(requireContext())
            `Al-Quran_H`(requireContext()).readAyatXtoY(
                P().Position()[position-1].startPos
                , P().Position()[position-1].endPos
            ).forEach {
                if (it.ayat == 1)
                    data.add(modelExchange(it, true))
                data.add(modelExchange(it, false))
            }
            adapterSurah = ParaAyatAdap(
                requireContext(), data
            )
            activity?.runOnUiThread {
                binding?.ayatRecycler?.layoutManager = LinearLayoutManager(requireContext())
                binding?.ayatRecycler?.adapter = adapterSurah
            }
        }
    }

    private fun clearAll() {
        data.clear()
        adapterSurah?.notifyDataSetChanged()
        CoroutineScope(Dispatchers.Default).launch {
            `Al-Quran_H`(requireContext()).readAyatXtoY(
                P().Position()[position - 1].startPos,
                P().Position()[position - 1].endPos
            ).forEach {
                if (it.ayat == 1)
                    data.add(modelExchange(it, true))
                data.add(modelExchange(it, false))
            }
            activity?.runOnUiThread {
                adapterSurah?.notifyDataSetChanged()
            }
        }
    }

    private fun searchIconClick() {
        binding?.searchText?.text.toString().let { e->
            search = if (e.isNotEmpty()) {
                try {
                    filter(e.toInt())
                } catch (ex: Exception) {
                    Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
                }
                e
            } else {
                data.clear()
                adapterSurah?.notifyDataSetChanged()
                CoroutineScope(Dispatchers.Default).launch {
                    `Al-Quran_H`(requireContext()).readAyatXtoY(
                        P().Position()[position - 1].startPos,
                        P().Position()[position - 1].endPos
                    ).forEach {
                        if (it.ayat == 1)
                            data.add(modelExchange(it, true))
                        data.add(modelExchange(it, false))
                    }
                    activity?.runOnUiThread {
                        adapterSurah?.notifyDataSetChanged()
                    }
                }
                ""
            }
        }
        closeKeyboard(binding?.searchText)
    }

    private fun filter(pos: Int) {
        data.clear()
        adapterSurah?.notifyDataSetChanged()
        `Al-Quran_H`(requireContext()).readAyatXtoY(
            P().Position()[position-1].startPos
            , P().Position()[position-1].endPos
        ).forEach {
            if (it.ayat == 1)
                data.add(modelExchange(it, true))
            data.add(modelExchange(it, false))
        }
        adapterSurah?.notifyDataSetChanged()
        if (pos < data.size) {
            binding?.ayatRecycler?.scrollToPosition(pos)
        }
    }

    private fun closeKeyboard(edit: EditText?) {
        edit?.let {
            it.clearFocus()
            Keyb_Uti.hideKeyboard(it)
        }
    }

    private fun modelExchange(temp: `AL-QURAN`, name: Boolean): Pa_R_Ah_Ay {
        if (name) {
            val s = suRAhH!!.readDataAt(`al-quranH`!!.readAyatNo(temp.pos)!!.surah)
            return Pa_R_Ah_Ay(
                type = 1,
                pos = temp.pos,
                surah = temp.surah,
                ayat = temp.ayat,
                indopak = temp.indopak,
                utsmani = temp.utsmani,
                jalalayn = temp.jalalayn,
                latin = temp.latin,
                terjemahan = temp.terjemahan,
                englishPro = temp.englishPro,
                englishT = temp.englishT,
                name = s!!.name,
                meaning = Nam().data()[temp.surah-1],
                details = "${s.revelation}   |   ${s.verse} VERSES"
            )
        } else {
            return Pa_R_Ah_Ay(
                type = 0,
                pos = temp.pos,
                surah = temp.surah,
                ayat = temp.ayat,
                indopak = temp.indopak,
                utsmani = temp.utsmani,
                jalalayn = temp.jalalayn,
                latin = temp.latin,
                terjemahan = temp.terjemahan,
                englishPro = temp.englishPro,
                englishT = temp.englishT,
                name = "",
                meaning = "",
                details = ""
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }
}