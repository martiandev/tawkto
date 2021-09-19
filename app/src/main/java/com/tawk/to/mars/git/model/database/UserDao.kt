package com.tawk.to.mars.git.model.database

import androidx.room.*
import com.tawk.to.mars.git.model.entity.User
import io.reactivex.Completable
import io.reactivex.Single
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data:List<User>):Completable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data:User):Completable
    @Query("SELECT * FROM User where id > :since LIMIT :limit")
    abstract fun getSince(since:Int, limit:Int): Single<List<User>>
    @Delete
    abstract fun delete(data:User):Completable
    @Query("DELETE FROM User")
    abstract fun clear():Completable
}