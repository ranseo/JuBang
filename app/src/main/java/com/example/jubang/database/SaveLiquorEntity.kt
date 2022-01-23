package com.example.jubang.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "titletable")
data class SaveLiquorEntity(
        @PrimaryKey (autoGenerate = true) val titleId: Int?,
        val store : String,
        val date : String
        )
{
        constructor() : this(null," "," ")
}


