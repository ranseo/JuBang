package com.example.jubang.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [SaveLiquorEntity::class,
        SaveLiquorDetail::class,
        FinalList::class], version = 1
)
@TypeConverters(Converters::class)
abstract class SaveLiquorDatabase : RoomDatabase() {
    abstract fun saveLiquorDAO(): SaveLiquorDAO

    //데이터베이스를 만드는 작업은 메모리를 많이 만들어내는 작업이기에
    //리소스를 많이 잡아먹는 일이기 때문에 앱 전체 프로세스 안에서
    //딱 한번만 객체를 만드는 것이 유리함 -> 싱글톤 객체.
    companion object {
        private var INSTANCE: SaveLiquorDatabase? = null

        fun getInstance(context: Context): SaveLiquorDatabase? {
            if (INSTANCE == null) {
                synchronized(SaveLiquorDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SaveLiquorDatabase::class.java, "memo.db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }

}