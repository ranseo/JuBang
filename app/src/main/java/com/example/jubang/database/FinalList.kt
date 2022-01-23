package com.example.jubang.database

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull


@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "finallist")
data class FinalList(

        @Embedded
        val title: SaveLiquorEntity = SaveLiquorEntity(),

        @PrimaryKey
        @TypeConverters(Converters::class)
        val liquor: List<SaveLiquorDetail>
) {
    constructor() : this(SaveLiquorEntity(), listOf())
}


class Converters {
    @TypeConverter
    fun listToJson(value: List<SaveLiquorDetail>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SaveLiquorDetail>::class.java).toList()
}
