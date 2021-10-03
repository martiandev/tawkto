package com.tawk.to.mars.git.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
@Deprecated("Does not support searching within a string")
@Entity
@Fts4(contentEntity = User::class)
data class UserFTS (@ColumnInfo(name="login") val login:String,@ColumnInfo(name="note") val note:String)
