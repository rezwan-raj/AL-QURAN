package com.quantum.quran.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.quran.adapter.SurahListAdap
import com.quantum.quran.databinding.FSurahBinding
import com.quantum.quran.model.Su_R_Ah_Li
import com.quantum.quran.sql.Su_R_Ah_H
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Su_R_Ah : Fragment() {

    private val data = ArrayList<Su_R_Ah_Li>()
    private var adapter: SurahListAdap? = null
    private var binding: FSurahBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FSurahBinding.inflate(inflater, container, false)

        adapter = SurahListAdap(requireContext(), data)

        binding?.surahRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.surahRecycler?.adapter = adapter

        loadData()

        return binding?.root
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Default).launch {
            data.clear()
            data.addAll(Su_R_Ah_H(requireContext()).readData())
            activity?.runOnUiThread { adapter?.notifyItemRangeChanged(0, data.size) }
        }
    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }
}