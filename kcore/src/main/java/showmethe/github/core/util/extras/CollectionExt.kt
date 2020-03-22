package showmethe.github.core.util.extras

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import java.text.FieldPosition

/**
 * Author: showMeThe
 * Update Time: 2019/11/7 10:36
 * Package Name:showmethe.github.core.util.extras
 */

fun <T> ArrayList<T>.clearAfter(lastPosition: Int){
    val temp = ArrayList<T>()
    if(lastPosition < size * 0.5){
        for((index,t) in withIndex()){
            if(index <= lastPosition){
                temp.add(t)
            }else{
                break
            }
        }
        clear()
        addAll(temp)
    }else {
        for(index in (size -1) downTo lastPosition){
            temp.add(this[index])
        }
        clear()
        addAll(temp)
    }
}


inline fun <T> Iterable<T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}

inline fun <T> Collection<T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}

inline fun <T> Array<out T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}