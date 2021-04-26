package com.show.kcore.http.coroutines

interface IFunction<T1,T2,R> {

    fun apply(t1: T1?,t2: T2?): R
}