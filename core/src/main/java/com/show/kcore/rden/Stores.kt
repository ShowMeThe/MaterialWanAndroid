package com.show.kcore.rden

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import com.show.kcore.http.clazzToJson
import com.show.kcore.http.jsonToClazz
import com.tencent.mmkv.MMKV
import org.jetbrains.annotations.NotNull


object Stores {


    lateinit var creator: DatabaseCreator
    private val mmkv by lazy { MMKV.mmkvWithID("RDEN", MMKV.MULTI_PROCESS_MODE)!! }

    fun initialize(context: Context) {
        MMKV.initialize(context)
        creator = Room.databaseBuilder(
            context.applicationContext,
            DatabaseCreator::class.java,
            "RDENRoom"
        )
            .enableMultiInstanceInvalidation()
            .allowMainThreadQueries().build()
    }


    fun put(@NotNull key: String, @NotNull value: String) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Float) {
        mmkv.encode(key, value)
    }

    fun put(@NotNull key: String, @NotNull value: Boolean) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Int) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: Long) {
        mmkv.encode(key, value)
    }


    fun put(@NotNull key: String, @NotNull value: ByteArray) {
        mmkv.encode(key, value)
    }


    fun getString(@NotNull key: String, @NotNull default: String): String? =
        mmkv.decodeString(key, default)

    fun getFloat(@NotNull key: String, @NotNull default: Float) = mmkv.decodeFloat(key, default)

    fun getBoolean(@NotNull key: String, @NotNull default: Boolean) = mmkv.decodeBool(key, default)

    fun getInt(@NotNull key: String, @NotNull default: Int) = mmkv.decodeInt(key, default)

    fun getLong(@NotNull key: String, @NotNull default: Long) = mmkv.decodeLong(key, default)

    fun getByteArray(@NotNull key: String, @NotNull default: ByteArray) =
        mmkv.decodeBytes(key, default)

    inline fun <reified T> putObject(@NotNull key: String, value: T?) {
        val jsonData = value?.clazzToJson<T>()
        var bean = creator.roomDao().get(key)
        if (bean == null) {
            bean = RoomBean()
                .apply {
                    storeKey = key
                    stringValue = jsonData
                }
        } else {
            bean.apply {
                stringValue = jsonData
            }
        }
        creator.roomDao().put(bean)
    }

    inline fun <reified T> getLive(owner: LifecycleOwner,@NotNull key: String,crossinline onChange:(t:T?)->Unit){
        creator.roomDao().getLiveData(key).observe(owner){
            val data = it?.stringValue
            val out = data?.jsonToClazz<T>()
            onChange.invoke(out)
        }
    }

     fun delete(key: String){
        creator.roomDao().deleteData(key)

    }

    inline fun <reified T> getObject(@NotNull key: String, default: T): T? {
        val bean = creator.roomDao().get(key)
        return if (bean != null) {
            val jsonData = bean.stringValue
            if (jsonData != null) {
                jsonData.jsonToClazz()
            } else {
                default
            }
        } else {
            default
        }
    }


}
