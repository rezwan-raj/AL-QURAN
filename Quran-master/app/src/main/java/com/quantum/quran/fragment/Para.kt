package com.quantum.quran.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.quran.adapter.ParaListAdap
import com.quantum.quran.constant.Para
import com.quantum.quran.databinding.FParaBinding

class Para : Fragment() {

    private var binding: FParaBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FParaBinding.inflate(inflater, container, false)

        binding?.paraRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.paraRecycler?.adapter = ParaListAdap(
            requireContext(), Para().Position()
        )

        return binding?.root
    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }
}