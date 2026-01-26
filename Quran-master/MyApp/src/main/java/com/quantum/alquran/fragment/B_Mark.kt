package com.quantum.alquran.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantum.alquran.`interface`.B_Mark
import com.quantum.alquran.adapter.BookmarkAdap
import com.quantum.alquran.databinding.FBookmarkBinding
import com.quantum.alquran.model.`AL-QURAN`
import com.quantum.alquran.sql.`Al-Quran_H`
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class B_Mark : Fragment() {

    private val data = ArrayList<`AL-QURAN`>()
    private var adapter: BookmarkAdap? = null
    private var binding: FBookmarkBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FBookmarkBinding.inflate(inflater, container, false)

        adapter = BookmarkAdap(requireContext(), data
            , object : B_Mark {
                override fun removed(pos: Int) {
                    data.removeAt(pos)
                    adapter?.notifyItemRemoved(pos)
                    adapter?.notifyItemRangeChanged(pos, data.size)
                    binding?.noBookmark?.visibility =
                        if (data.size > 0) View.GONE
                        else View.VISIBLE
                }
            })
        binding?.ayatRecycler?.layoutManager = LinearLayoutManager(requireContext())
        binding?.ayatRecycler?.adapter = adapter

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Default).launch {
            data.clear()
            data.addAll(`Al-Quran_H`(requireContext()).readBookmark())
            activity?.runOnUiThread {
                binding?.noBookmark?.visibility =
                    if (data.size > 0) View.GONE
                    else View.VISIBLE
                adapter?.notifyDataSetChanged()
            }
        }
    }
}