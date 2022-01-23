package com.example.jubang.fragment

import com.example.jubang.database.FinalList
import com.example.jubang.database.SaveLiquorDetail
import com.example.jubang.database.SaveLiquorEntity

interface OnDeleteListener {
    fun onDeleteListener(saveLiquor: FinalList)
    fun onDeleteListener(saveLiquor: SaveLiquorDetail)
    fun onDeleteListener(saveLiquor: SaveLiquorEntity)

}