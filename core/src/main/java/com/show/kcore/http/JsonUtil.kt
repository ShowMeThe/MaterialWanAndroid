package com.show.kcore.http

import android.util.Log
import com.google.gson.*
import com.google.gson.JsonParser.parseString
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


val moshi by lazy { Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build() }


val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    val builder = GsonBuilder()
    builder.registerTypeAdapter(
        JsonObject::class.java,
        JsonDeserializer<Any> { jsonElement, type, jsonDeserializationContext -> jsonElement.asJsonObject })
    builder
        .serializeNulls()
        .disableHtmlEscaping().create()
}


inline fun <reified T> ArrayList<T>.listToJson(): String {
    val token = object : TypeToken<ArrayList<T>>() {}.type
    return gson.toJson(this, token)
}

inline fun <reified T> String.jsonToList(token: TypeToken<ArrayList<T>>): ArrayList<T> {
    return gson.fromJson(this, token.type)
}


inline fun <reified T> T.clazzToJson(): String {
    return moshi.adapter<T>(T::class.java).toJson(this)
}

inline fun <reified T> String.jsonToClazz(): T? {
    return moshi.adapter<T>(T::class.java)
        .failOnUnknown()
        .lenient()
        .nullSafe()
        .serializeNulls()
        .fromJson(this)
}

inline fun <reified T> Any.jsonToClazz(): T? {
    return moshi.adapter<T>(T::class.java)
        .failOnUnknown()
        .lenient()
        .nullSafe()
        .serializeNulls()
        .fromJsonValue(this)
}