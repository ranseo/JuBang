package com.example.jubang.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jubang.R
import com.example.jubang.database.FinalList
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.databinding.LiquorSaveListBinding
import com.example.jubang.dialog.SaveFragDialog
import com.example.jubang.fragment.OnDeleteListener

class SaveListAdapter(val context: Context, private var list : List<FinalList>, private var onDeleteListener: OnDeleteListener): RecyclerView.Adapter<SaveListAdapter.MyViewHolder>(){

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LiquorSaveListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        val (title, liquorList) = item

        holder.tvStore.text = title.store
        holder.tvDate.text = title.date

        //두번 째 recyclerView만들기.
        val layoutManager = LinearLayoutManager(context)
        holder.recyclerview.layoutManager = layoutManager
        holder.recyclerview.setHasFixedSize(true)
        holder.recyclerview.adapter = SaveListSecondAdapter(context, liquorList)


        holder.clBackground.setOnClickListener {

        }
        holder.clBackground.setOnLongClickListener {
            val dlg = SaveFragDialog(context, item, onDeleteListener)
            dlg.start()
            true
        }
    }

    inner class MyViewHolder(binding: LiquorSaveListBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvDate : TextView = binding.tvDate
        val tvStore : TextView = binding.tvStore
        val recyclerview : RecyclerView = binding.adapterSaveRecyclerView
        val clBackground : LinearLayout = binding.clBackground
    }

}