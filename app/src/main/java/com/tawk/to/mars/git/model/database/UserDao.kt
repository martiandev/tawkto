package com.tawk.to.mars.git.model.database

import androidx.room.*
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserUpdate
import com.tawk.to.mars.git.model.entity.UserUpdateProfile
import io.reactivex.Completable
import io.reactivex.Single
@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(data:List<User>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(data:User):Long
    @Update(entity = User::class)
    abstract fun updateProfile(obj:UserUpdateProfile)
    @Update(entity = User::class)
    abstract fun update(obj:UserUpdate)

    @Query("SELECT * FROM User where id > :since LIMIT :limit")
    abstract fun getSince(since:Int, limit:Int):List<User>

    @Query("SELECT * FROM User where id = :id")
    abstract fun get(id:Int):User

    @Delete
    abstract fun delete(data:User)
    @Query("DELETE FROM User")
    abstract fun clear()



}