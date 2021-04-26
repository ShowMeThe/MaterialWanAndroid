package com.show.kcore.base

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.show.bundle_inject.BundleInject
import java.io.Serializable
import java.util.ArrayList

object BundleAutoInject {


    fun inject(activity: ComponentActivity) {
        val extras = activity.intent.extras
        val fields = activity::class.java.declaredFields
        fields.forEach {
            val field = it
            val isAnnotationTarget = field.isAnnotationPresent(BundleInject::class.java)

            if (isAnnotationTarget) {
                val annotationTarget = field.getAnnotation(BundleInject::class.java) as BundleInject
                val key = annotationTarget.key
                field.isAccessible = true
                if (extras != null) {
                    field.set(activity, getFromBundle(field.type, extras, key))
                }
            }
        }
    }

    fun inject(activity: Fragment) {
        val extras = activity.arguments
        val fields = activity::class.java.declaredFields
        fields.forEach {
            val field = it
            val isAnnotationTarget = field.isAnnotationPresent(BundleInject::class.java)
            if (isAnnotationTarget) {
                val annotationTarget = field.getAnnotation(BundleInject::class.java) as BundleInject
                val key = annotationTarget.key
                field.isAccessible = true
                if (extras != null) {
                    field.set(activity, getFromBundle(field.type, extras, key))
                }
            }
        }
    }


    private fun getFromBundle(type: Class<*>, bundle: Bundle, key: String): Any? {
        return when (type) {
            String::class.java -> bundle.getString(key)
            Int::class.java -> bundle.getInt(key)
            Long::class.java -> bundle.getLong(key)
            Double::class.java -> bundle.getDouble(key)
            Float::class.java -> bundle.getFloat(key)
            Byte::class.java -> bundle.getByte(key)
            Char::class.java -> bundle.getChar(key)
            Short::class.java -> bundle.getShort(key)
            Boolean::class.java -> bundle.getBoolean(key)
            ByteArray::class.java -> bundle.getByteArray(key)
            CharArray::class.java -> bundle.getCharArray(key)
            CharSequence::class.java -> bundle.getCharSequence(key)
            Array<CharSequence>::class.java -> bundle.getCharSequenceArray(key)
            FloatArray::class.java -> bundle.getFloatArray(key)
            LongArray::class.java -> bundle.getLongArray(key)
            IntArray::class.java -> bundle.getIntArray(key)
            DoubleArray::class.java -> bundle.getDoubleArray(key)
            ShortArray::class.java -> bundle.getShortArray(key)
            BooleanArray::class.java -> bundle.getBooleanArray(key)
            Array<String>::class.java -> bundle.getStringArray(key)
            IBinder::class.java -> bundle.getBinder(key)
            Size::class.java -> bundle.getSize(key)
            SizeF::class.java -> bundle.getSizeF(key)
            Bundle::class.java -> bundle.getBundle(key)
            ArrayList<String>()::class.java -> bundle.getStringArrayList(key)
            ArrayList<Int>()::class.java -> bundle.getIntegerArrayList(key)
            ArrayList<CharSequence>()::class.java -> bundle.getCharSequenceArrayList(key)
            ArrayList<Parcelable>()::class.java -> bundle.getParcelableArrayList<Parcelable>(key)
            else -> {
                return when {
                    Parcelable::class.java.isAssignableFrom(type) -> {
                        bundle.getParcelable<Parcelable>(key)
                    }
                    Serializable::class.java.isAssignableFrom(type) -> {
                        bundle.getSerializable(key)
                    }
                    else -> {
                        return null
                    }
                }
            }
        }
    }
}