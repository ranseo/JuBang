package com.example.jubang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jubang.R
import com.example.jubang.database.SaveLiquorEntity
import com.example.jubang.dialog.HomeFragSaveDialog
import com.example.jubang.dialog.LiquorNameAndColor
import com.example.jubang.dialog.PlusFragDialog
import kotlinx.android.synthetic.main.fragment_plus_liquor.*

class PlusFragment : Fragment() {

    private lateinit var liquorNameAndColor: LiquorNameAndColor


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_plus_liquor, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_fragment_plus.setOnClickListener {
                val dlg = PlusFragDialog(requireContext(), fragmentManager!!)
            dlg.setOnOKClickedListener { _liquorNameAndColor ->
                liquorNameAndColor = _liquorNameAndColor
                val intent = Intent("PlusFragment_action").apply{
                    putExtra("Result", liquorNameAndColor)
                }

                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
                dlg.start()
        }
    }


}