package com.tawk.to.mars.git.model.database

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserFTS
import com.tawk.to.mars.git.model.entity.converter.DateConverter
import com.tawk.to.mars.git.util.SingletonHolder
import com.tawk.to.mars.git.view.app.TawkTo

@TypeConverters(DateConverter::class)
@Database(entities = arrayOf(
    User::class,
    UserFTS::class
),
  version = 1
)
abstract class TTDatabase :RoomDatabase()
{
    init {
    }
    companion object : SingletonHolder<TTDatabase, TawkTo>({
        Room.databaseBuilder(it.applicationContext, TTDatabase::class.java, "talktwo").build()
    })

    public abstract fun userDao(): UserDao
}