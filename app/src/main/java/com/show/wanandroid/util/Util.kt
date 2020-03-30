package com.show.wanandroid.util

import android.util.Log
import showmethe.github.core.util.rden.RDEN

object Util {


    private const val SEARCH_KEY = "SEARCH_KEY"

    fun clearRecently(){
       RDEN.put(SEARCH_KEY,"")
    }

    fun String.saveRecently(){
        val lastSearch = RDEN.get(SEARCH_KEY,"")
        if(isEmpty()) return
        val last =  ArrayList<String>(lastSearch.split(","))
        var insert = false
        for(s in last){
            if(s == this){
                last.remove(s)
                last.add(0,s)
                insert = true
                break
            }
        }
        if(!insert){
            if(last.size >= 10){
                last.removeAt(last.size - 1)
            }
            last.add(0,this)
        }
        val sb = StringBuilder()
        for(s in last){
            if(s.isNotEmpty())
            sb.append(s).append(",")
        }
        if(sb.isNotEmpty()){
            sb.deleteCharAt(sb.length - 1)
        }
        RDEN.put(SEARCH_KEY,sb.toString())
    }

    fun getSaveRecently() : ArrayList<String>{
        val lastSearch = RDEN.get(SEARCH_KEY,"")
        return if(lastSearch.isEmpty()){
            ArrayList<String>(0)
        }else{
            ArrayList(lastSearch.split(","))
        }
    }

}