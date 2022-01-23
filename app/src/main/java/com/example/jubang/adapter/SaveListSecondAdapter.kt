package com.example.jubang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.databinding.LiquorSaveListSecondBinding

class SaveListSecondAdapter(context: Context, private val list: List<SaveLiquorDetail>) : RecyclerView.Adapter<SaveListSecondAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveListSecondAdapter.MyViewHolder {
        return MyViewHolder(LiquorSaveListSecondBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.tvLiquorName.text = item.name
        holder.tvBot.text = item.currBot.toString()
        holder.tvCup.text = item.currCup.toString()
        holder.tvMax.text = item.maxBot.toString()
    }

    inner class MyViewHolder(binding: LiquorSaveListSecondBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvCup : TextView = binding.adapterSaveLiquorCup
        val tvBot : TextView = binding.adapterSaveLiquorBottle
        val tvLiquorName : TextView= binding.adapterSaveLiquorName
        val tvMax :TextView = binding.adapterSaveLiquorMax
    }

}