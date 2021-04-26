package com.show.kcore.rden

import androidx.room.Database
import androidx.room.RoomDatabase
import com.show.kcore.rden.RoomBean
import com.show.kcore.rden.RoomDao


@Database(entities = [RoomBean::class],version = 1)
abstract class DatabaseCreator : RoomDatabase() {

   abstract fun roomDao() : RoomDao

}