package com.show.kcore.rden

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "RoomBean")
class RoomBean {


    @PrimaryKey
    lateinit var storeKey: String
    var stringValue: String? = null
}
