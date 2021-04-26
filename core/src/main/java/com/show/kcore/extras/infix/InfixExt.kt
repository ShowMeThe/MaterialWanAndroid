package com.show.kcore.extras.infix

import android.view.View
import androidx.lifecycle.MutableLiveData



infix fun View.alpha(alphaFloat:Float){
    alpha = alphaFloat
}

infix fun View.translationY(x:Float){
    translationY = x
}

infix fun View.translationX(x:Float){
    translationX = x
}

infix fun View.scaleX(x:Float){
   scaleX = x
}

infix fun View.scaleY(y:Float){
    scaleY = y
}


/**
 *  判断内容是否为空
 */
fun MutableLiveData<*>.valueIsNull() = this.value == null


/**
 * 简化MutableLiveData<Int> 加法 value为null时候从0开始+
 */
infix fun MutableLiveData<Int>.plus(x:Int) : MutableLiveData<Int> {
    if(!this.valueIsNull()){
        postValue(value!!.plus(x))
    }else{
        postValue(0.plus(x))
    }
    return this
}

/**
 * 简化MutableLiveData<Int> 减法value
 */
infix fun MutableLiveData<Int>.sub(x:Int) : MutableLiveData<Int> {
    if(!this.valueIsNull()){
        postValue(value!!.minus(x))
    }
    return this
}

/**
 * 简化MutableLiveData<T> postValue
 */
infix fun <T:Any> MutableLiveData<T>?.post(newValue:T?){
    this?.postValue(newValue)
}

infix fun <T:Any> MutableLiveData<T>?.set(newValue:T?){
    this?.value = newValue
}


infix fun <T:Any> MutableLiveData<T>.valueEquals(equals : T) : Boolean{
    return if(value == null){
        false
    }else{
        value!!  ==  equals
    }
}

