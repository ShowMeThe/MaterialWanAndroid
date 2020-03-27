package com.showmethe.skinlib

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.ArrayMap
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.CardViewBindingAdapter
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author: showMeThe
 * Update Time: 2020/1/17 14:28
 * Package Name:com.showmethe.skinlib
 */
class SkinManager private constructor(var context: Context){

    companion object{

        private var instant: SkinManager? = null

        @Synchronized
        fun init(context: Context) : SkinManager {
            if(instant == null){
                instant = SkinManager(context)
            }
            return instant!!
        }

        fun getInstant() : SkinManager {
            return instant!!
        }

        private val attrs = intArrayOf(
            R.attr.theme_viewGroup_background, R.attr.theme_viewGroup_backgroundColor,
            R.attr.theme_card_backgroundColor, R.attr.theme_card_strokeColor,

            R.attr.theme_text_color,R.attr.theme_text_background,
                R.attr.theme_text_backgroundColor,R.attr.theme_text_drawableTint,

            R.attr.theme_button_textColor,R.attr.theme_button_background,
            R.attr.theme_button_backgroundColor,R.attr.theme_button_drawableTint,R.attr.theme_button_iconTint,

            R.attr.theme_radio_textColor,R.attr.theme_radio_background,
            R.attr.theme_radio_backgroundColor,R.attr.theme_radio_drawableTint,R.attr.theme_radio_buttonTint

            )


        private val styleName = arrayOf(
            "theme_viewGroup_background","theme_viewGroup_backgroundColor",
            "theme_card_backgroundColor", "theme_card_strokeColor",

            "theme_text_color","theme_text_background",
                "theme_text_backgroundColor","theme_text_drawableTint",

            "theme_button_textColor",
            "theme_button_background", "theme_button_backgroundColor",
            "theme_button_drawableTint","theme_button_iconTint",

            "theme_radio_textColor","theme_radio_background",
            "theme_radio_backgroundColor","theme_radio_drawableTint","theme_radio_buttonTint"
            )


        fun patchView(view: View,attr:String){
            getInstant().patchView(view, attr)
        }

        fun patchView(view: ViewGroup,attr:String){
            getInstant().patchView(view, attr)
        }

    }

    var enableSkin = false

    private val bindings = ArrayList<WeakReference<ViewDataBinding?>>()
    private val styles = ArrayList<Pair<String,Int>>()
    private val themes = ArrayMap<String,ArrayMap<String,Int>>()
    private var currentStyle = ""

    fun addStyle(vararg style:Pair<String,Int>) : SkinManager {
        styles.addAll(style.toList())
        return  this
    }

    fun build(){
        buildTheme()
    }

    private fun  buildTheme(){
        if(styles.isEmpty()) return
        styles.forEach {
            if(themes[it.first] == null){
                themes[it.first] = obtainAttr(it.second)
            }
        }

    }

    fun bindings(binding: ViewDataBinding?){
        bindings.add(WeakReference(binding))
    }


    fun invalidateAll(){
        bindings.forEach {
            it.get()?.apply {
                invalidateAll()
            }
        }
    }

    fun switchThemeByName(name:String){
        enableSkin = true//开启
        for(style in styles){
            if(style.first == name){
                currentStyle = style.first
                break
            }
        }
        invalidateAll()
    }


    private fun obtainAttr(style:Int) : ArrayMap<String,Int>{
        val map = ArrayMap<String,Int>()
        val wrapper = ContextThemeWrapper(context,style)
        val array  = wrapper.obtainStyledAttributes(style, attrs)
        for(i in attrs.indices){
            map[styleName[i]]= array.getResourceId(i,-1)
        }
        array.recycle()
        return map
    }

    fun patchView(view: ViewGroup,attr:String){
        if(!enableSkin) return
        val theme = themes[currentStyle]!!
        val attrs = attr.split("|")
        if(attr.isEmpty()) return
        when(view.viewType()){
            ViewType.CardView, ViewType.MaterialCardView->{
                attrs.forEach {
                    when{
                        it.trimLower() == "divideColor" && view.viewType() == ViewType.MaterialCardView ->{
                            theme["theme_card_strokeColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setStrokeColor" }[1].invoke(view,getColorStateList())
                            }
                        }
                        it.trimLower() == "backgroundColor" ->{
                            theme["theme_card_backgroundColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setCardBackgroundColor"  }[1].invoke(view,getColorStateList())
                            }
                        }
                    }
                }
            }
            else ->{
                attrs.forEach {
                    when{
                        it.trimLower().toLowerCase(Locale.ENGLISH) == "background" ->{
                            theme["theme_viewGroup_background"]?.apply { view.background = getDrawable() }
                        }
                        it.trimLower() == "backgroundColor" ->{
                            theme["theme_viewGroup_backgroundColor"]?.apply { view.setBackgroundColor(getColor()) }
                        }
                    }
                }
            }
        }
    }

    fun patchView(view: View,attr:String){
        if(!enableSkin) return
        val attrs = attr.split("|")
        val theme = themes[currentStyle]
        if(attr.isEmpty()) return
        when(view.viewType()){
            ViewType.TextView, ViewType.MaterialTextView ->{
                val tv = view as TextView
                 theme?.apply {
                    attrs.forEach {
                        when {
                            it.trimLower() == "textColor" -> {
                                this["theme_text_color"]?.apply { tv.setTextColor(getColorStateList()) }
                            }
                            it.trimLower()== "background" -> {
                                this["theme_text_background"]?.apply { tv.background = getDrawable() }
                            }
                            it.trimLower() == "backgroundColor" -> {
                                this["theme_text_backgroundColor"]?.apply {   tv.setBackgroundColor(getColor()) }
                            }
                            it.trimLower() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_text_drawableTint"]?.apply {   tv.compoundDrawableTintList = getColorStateList() }

                                }else{
                                    tv.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_text_drawableTint"]?.apply { drawable.setTintList(getColorStateList())}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.Button, ViewType.MaterialButton ->{
                val button = view as Button

                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trimLower() == "textColor" -> {
                                this["theme_button_textColor"]?.apply { button.setTextColor(getColorStateList()) }
                            }
                            it.trimLower()== "background" -> {
                                if(view.viewType() == ViewType.MaterialButton){
                                    this["theme_button_background"]?.apply {   button.backgroundTintList = getColorStateList() }
                                }else{
                                    this["theme_button_background"]?.apply { button.background = getDrawable() }
                                }
                            }
                            it.trimLower() == "backgroundColor" -> {
                                if(view.viewType() == ViewType.MaterialButton){
                                    this["theme_button_backgroundColor"]?.apply {  button.backgroundTintList = getColorStateList() }
                                }else{
                                    this["theme_button_backgroundColor"]?.apply {   button.setBackgroundColor(getColor()) }

                                }
                            }
                            it.trimLower() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_button_drawableTint"]?.apply { button.compoundDrawableTintList = getColorStateList() }

                                }else{
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_button_drawableTint"]?.apply { drawable.setTintList(getColorStateList())}

                                        }
                                    }
                                }
                            }
                            it.trimLower() == "iconTint" ->{
                                this["theme_button_iconTint"]?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setIconTint" }[0].invoke(button,getColorStateList()) }
                            }
                        }
                    }
                }
            }
            ViewType.RadioButton, ViewType.MaterialRadioButton ->{
                val button = view as RadioButton

                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trimLower() == "textColor" -> {
                                this["theme_radio_textColor"]?.apply { button.setTextColor(getColorStateList()) }
                            }
                            it.trimLower()== "background" -> {
                                this["theme_radio_background"]?.apply { button.background = getDrawable() }
                            }
                            it.trimLower() == "backgroundColor" -> {
                                this["theme_radio_backgroundColor"]?.apply {  button.setBackgroundColor(getColor()) }
                            }
                            it.trimLower() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_radio_drawableTint"]?.apply { button.compoundDrawableTintList =  getColorStateList()}
                                }else{
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_radio_drawableTint"]?.apply { setTintList(getColorStateList()) }
                                        }
                                    }
                                }
                            }
                            it.trimLower() == "buttonTint" ->{
                                this["theme_radio_buttonTint"]?.apply { button.buttonTintList = getColorStateList() }
                            }
                        }
                    }
                }
            }
            else ->{

            }
        }
    }




    private fun Int.getDrawable() : Drawable?{
        return if(this != -1){
            ContextCompat.getDrawable(context,this)
        }else{
            null
        }
    }

    private fun Int.getColor() : Int{
        return ContextCompat.getColor(context,this)
    }

    private fun Int.getColorStateList() : ColorStateList?{
        return if(this != -1){
            ContextCompat.getColorStateList(context,this)
        }else{
            ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun String.trimLower() = trim().toLowerCase(Locale.ENGLISH)


}
