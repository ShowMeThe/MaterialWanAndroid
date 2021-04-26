package com.show.kcore.rden

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RoomDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(bean : RoomBean)

    @Query("select * from RoomBean where storeKey  = (:key)")
    fun get(key :String ) : RoomBean?

    @Query("select * from RoomBean where storeKey  = (:key)")
    fun getLiveData(key :String ) : LiveData<RoomBean?>

    @Query("delete from RoomBean where storeKey = (:key)")
    fun deleteData(key: String)

}
