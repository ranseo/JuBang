package com.example.jubang.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jubang.R
import com.example.jubang.adapter.SaveListAdapter
import com.example.jubang.database.FinalList
import com.example.jubang.database.SaveLiquorDatabase
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.database.SaveLiquorEntity
import com.example.jubang.dialog.OffDialog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_save.*
import kotlinx.android.synthetic.main.fragment_save.view.*
import kotlinx.coroutines.*


class SaveFragment(var db: SaveLiquorDatabase, val offDialog: OffDialog) : Fragment(), OnDeleteListener{

    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_save, container, false)




        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val layoutManager = LinearLayoutManager(requireContext())
        view.rcv_saveLiquorList.layoutManager = layoutManager
        view.rcv_saveLiquorList.setHasFixedSize(true)

        db.saveLiquorDAO().getFinalAll().observe(viewLifecycleOwner, Observer {

            finalList ->
            //e/recyclerview no adapter attached skipping layout kotlin 문제해결

            setSaveRecyclerView(finalList)

        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                    offDialog.offAlert()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    fun insert() {

    }


    fun delete(saveLiquor: FinalList) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().delete(saveLiquor)
        }
    }

    fun delete(saveLiquor: SaveLiquorEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().delete(saveLiquor)
        }
    }

    fun delete(saveLiquor: SaveLiquorDetail) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.saveLiquorDAO().delete(saveLiquor)
        }
    }


//    fun delete(saveLiquor: SaveLiquorDetailWithEntity) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            db.saveLiquorDAO().delete(saveLiquor)
//        }
//    }
//

    private fun setSaveRecyclerView(saveLiquor: List<FinalList>) {
        rcv_saveLiquorList.adapter = SaveListAdapter(requireActivity(),saveLiquor,this)

    }


    override fun onDeleteListener(saveLiquor: FinalList) {
        delete(saveLiquor)
    }

    override fun onDeleteListener(saveLiquor: SaveLiquorDetail) {
        delete(saveLiquor)
    }

    override fun onDeleteListener(saveLiquor: SaveLiquorEntity) {
        delete(saveLiquor)
    }


}