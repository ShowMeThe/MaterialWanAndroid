package com.show.kcore.extras.string

import androidx.annotation.IntRange
import java.lang.StringBuilder
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

inline val String.builder get() = StringBuilder(this)

fun  String.append(str:String): StringBuilder = StringBuilder(this).append(str)
fun  String.append() = StringBuilder(this)

val String.md5
    get() = string2MD5(this)


fun string2MD5(inStr: String): String {
    var md5: MessageDigest? = null
    try {
        md5 = MessageDigest.getInstance("MD5")
    } catch (e: Exception) {
        println(e.toString())
        e.printStackTrace()
        return ""
    }

    val charArray = inStr.toCharArray()
    val byteArray = ByteArray(charArray.size)

    for (i in charArray.indices)
        byteArray[i] = charArray[i].toByte()
    val md5Bytes = md5!!.digest(byteArray)
    val hexValue = StringBuffer()
    for (i in md5Bytes.indices) {
        val `val` = md5Bytes[i].toInt() and 0xff
        if (`val` < 16)
            hexValue.append("0")
        hexValue.append(Integer.toHexString(`val`))
    }
    return hexValue.toString()

}

val Double.to2Decimal
    get() = toXDeDecimal(2,this)

val Float.to2Decimal
    get() = toXDeDecimal(2,this.toDouble())


val Double.to1Decimal
    get() = toXDeDecimal(1,this)

val Float.to1Decimal
    get() = toXDeDecimal(1,this.toDouble())

/**
 * 保留多少位小数
 * @param num
 * @return
 */
fun toXDeDecimal(@IntRange(from = 1) digit:Int,num: Double):String{
    val df = DecimalFormat()
    df.maximumFractionDigits = digit
    df.groupingSize = 0
    df.roundingMode = RoundingMode.FLOOR
    val style = "###0.".append()
    for(i in 0 until digit){
        style.append("0")
    }
    df.applyPattern(style.toString())
    return df.format(num)
}