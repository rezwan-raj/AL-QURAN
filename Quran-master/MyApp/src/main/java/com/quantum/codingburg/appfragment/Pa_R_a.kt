package com.quantum.codingburg.appfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.codingburg.myadapter.ParaListAdap
import com.quantum.codingburg.appconstant.P
import com.quantum.codingburg.databinding.FParaBinding

class Pa_R_a : Fragment() {

    private var binding: FParaBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FParaBinding.inflate(inflater, container, false)

        binding?.paraRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.paraRecycler?.adapter = ParaListAdap(
            requireContext(), P().Position()
        )

        return binding?.root
    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }
}