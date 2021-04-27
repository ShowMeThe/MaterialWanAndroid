package com.show.kcore.base

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BundleInject(val key:String)
