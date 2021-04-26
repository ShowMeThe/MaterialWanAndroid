package com.show.bundle_inject

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BundleInject(val key:String)
