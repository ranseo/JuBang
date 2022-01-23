package com.example.jubang.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liquortable")
data class SaveLiquorDetail(
    @PrimaryKey(autoGenerate = true) val liquorId : Int?,

    val name : String,
    val currCup: Int,
    val currBot: Int,
    val maxBot: Int,
    val titleId : Int?
) {
    constructor() : this(null, " ", 0, 0 , 0 , null)
}