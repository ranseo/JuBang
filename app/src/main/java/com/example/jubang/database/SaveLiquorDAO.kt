package com.example.jubang.database

import androidx.lifecycle.LiveData
import androidx.room.*
import org.jetbrains.annotations.NotNull

@Dao
interface  SaveLiquorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saveLiquor: SaveLiquorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saveLiquor: SaveLiquorDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(finalList: FinalList)

    @Update
    fun update(saveLiquor: SaveLiquorEntity)

    @Delete
    fun delete(saveLiquor: SaveLiquorEntity)

    @Delete
    fun delete(saveLiquor: SaveLiquorDetail)

    @Delete
    fun delete(saveLiquor: FinalList)

    @NotNull
    @Query("SELECT * FROM titletable WHERE titleId = :titleId")
    fun getTitleTable(titleId: Int?) : SaveLiquorEntity

    @NotNull
    @Query("SELECT * FROM liquortable WHERE titleId = :titleId ORDER BY name")
    fun getLiquorTableList(titleId: Int?) : List<SaveLiquorDetail>

    @NotNull
    @Query("SELECT MAX(titleId) FROM titletable")
    fun getTitleId() : Int

    @NotNull
    @Query("SELECT titleId FROM liquortable WHERE titleId > 0")
    fun getLiquorTitleId() : LiveData<List<Int>>


    @Query("SELECT * FROM titletable")
    fun getAll() : LiveData<List<SaveLiquorEntity>>

    @Transaction
    @Query("SELECT * FROM finallist ORDER BY date DESC")
    fun getFinalAll() : LiveData<List<FinalList>>



}