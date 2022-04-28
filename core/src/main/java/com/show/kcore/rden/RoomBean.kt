package com.show.kcore.rden

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.jetbrains.annotations.NotNull


@Entity(tableName = "RoomBean")
class RoomBean {


    @NotNull
    @PrimaryKey
    lateinit var storeKey: String
    var stringValue: String? = null
}
