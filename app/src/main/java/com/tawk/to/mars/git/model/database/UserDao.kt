package com.tawk.to.mars.git.model.database

import androidx.room.*
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserUpdate
import com.tawk.to.mars.git.model.entity.UserUpdateNote
import com.tawk.to.mars.git.model.entity.UserUpdateProfile
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    //Inserts users into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(data:List<User>)
    //Insert a user into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(data:User):Long
    //Updates user profile using UserUpdateProfile object
    @Update(entity = User::class)
    abstract fun update(obj:UserUpdateProfile)
    //Updates user basic data using UserUpdate object
    @Update(entity = User::class)
    abstract fun update(obj:UserUpdate)
    //Updates user's note using UserUpdateNote object
    @Update(entity = User::class)
    abstract fun update(obj: UserUpdateNote)
    //Query the database from users whose id is greater than since and limited to limit (page size)
    @Query("SELECT * FROM User where id > :since LIMIT :limit")
    abstract fun getSince(since:Int, limit:Int):List<User>
    //Retrieve a specific user using ID
    @Query("SELECT * FROM User where id = :id")
    abstract fun get(id:Int): User
    @Deprecated("Does not support searching within a string")
    @Query(" SELECT * FROM User JOIN UserFTS ON User.login = UserFTS.login where UserFTS MATCH '%'||:query||'%'")
    abstract fun searchFTS(query: String): List<User>
    //Retrieve a users whose login or note contains the query string

    @Query(" SELECT * FROM User where login LIKE '%'||:query||'%' OR note LIKE '%'||:query||'%'")
    abstract fun search(query: String): List<User>

    @Query("DELETE FROM User")
    abstract fun nukeTable()

}