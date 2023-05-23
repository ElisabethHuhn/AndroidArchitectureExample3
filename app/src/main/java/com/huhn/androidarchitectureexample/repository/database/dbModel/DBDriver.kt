package com.huhn.androidarchitectureexample.repository.database.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DBDriver(
    @PrimaryKey val uid: Int,
//    @ColumnInfo(name = "name") val name: String?,
)