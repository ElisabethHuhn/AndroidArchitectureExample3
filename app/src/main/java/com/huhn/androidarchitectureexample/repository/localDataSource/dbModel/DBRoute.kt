package com.huhn.androidarchitectureexample.repository.localDataSource.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DBRoute(
    @PrimaryKey val uid: Int,
//    @ColumnInfo(name = "name") val name: String?,
//    @ColumnInfo(name = "type") val type: String?,
)