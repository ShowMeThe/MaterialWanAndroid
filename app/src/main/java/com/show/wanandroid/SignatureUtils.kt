package com.show.wanandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

object SignatureUtils {
    @SuppressLint("PackageManagerGetSignatures")
    fun getSignature(context: Context): String? {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo?.signatures?.get(0)?.hashCode()?.toString()
    }
}