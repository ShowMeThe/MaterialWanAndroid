package com.showmethe.skinlib

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.ArrayMap
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.CardViewBindingAdapter
import com.showmethe.skinlib.entity.ColorEntity
import com.showmethe.skinlib.plugin.IPlugin
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author: showMeThe
 * Update Time: 2020/1/17 14:28
 * Package Name:com.showmethe.skinlib
 */

fun String.getColorExtras() : ArrayList<String>?{
    return SkinManager.getInstant().themesJson[this]?.colorObjects
}

fun String.isStyleFromJson() = SkinManager.getInstant().themesJson[this] != null

class SkinManager private constructor(var context: Context) {

    companion object {

        var currentStyle = ""
        private var instant: SkinManager? = null

        @Synchronized
        fun init(context: Context): SkinManager {
            if (instant == null) {
                instant = SkinManager(context)
            }
            return instant!!
        }

        fun getInstant(): SkinManager {
            return instant!!
        }


        private val attrs = intArrayOf(
            R.attr.theme_viewGroup_background,
            R.attr.theme_viewGroup_backgroundColor,
            R.attr.theme_card_backgroundColor,
            R.attr.theme_card_strokeColor,

            R.attr.theme_text_color,
            R.attr.theme_text_background,
            R.attr.theme_text_backgroundColor,
            R.attr.theme_text_drawableTint,

            R.attr.theme_button_textColor,
            R.attr.theme_button_rippleColor,
            R.attr.theme_button_background,
            R.attr.theme_button_backgroundColor,
            R.attr.theme_button_drawableTint,
            R.attr.theme_button_iconTint,
            R.attr.theme_button_strokeColor,

            R.attr.theme_radio_textColor,
            R.attr.theme_radio_background,
            R.attr.theme_radio_backgroundColor,
            R.attr.theme_radio_drawableTint,
            R.attr.theme_radio_buttonTint,

            R.attr.theme_bottom_navigation_iconTint,
            R.attr.theme_bottom_navigation_textColor,

            R.attr.theme_imageView_tint,

            R.attr.theme_floating_tint,
            R.attr.theme_floating_backgroundColor,

            R.attr.theme_edit_textColor,
            R.attr.theme_edit_hintColor,
            R.attr.theme_edit_cursorDrawable,
            R.attr.theme_edit_highlightColor,

            R.attr.theme_inputLayout_boxColor,
            R.attr.theme_inputLayout_hintColor
        )


        private val styleName = arrayOf(
            "theme_viewGroup_background", "theme_viewGroup_backgroundColor",
            "theme_card_backgroundColor", "theme_card_strokeColor",

            "theme_text_color", "theme_text_background",
            "theme_text_backgroundColor", "theme_text_drawableTint",

            "theme_button_textColor", "theme_button_rippleColor",
            "theme_button_background", "theme_button_backgroundColor",
            "theme_button_drawableTint", "theme_button_iconTint", "theme_button_strokeColor",

            "theme_radio_textColor", "theme_radio_background",
            "theme_radio_backgroundColor", "theme_radio_drawableTint", "theme_radio_buttonTint",

            "theme_bottom_navigation_iconTint", "theme_bottom_navigation_textColor",

            "theme_imageView_tint",

            "theme_floating_tint", "theme_floating_backgroundColor",

            "theme_edit_textColor", "theme_edit_hintColor",
            "theme_edit_cursorDrawable","theme_edit_highlightColor",

            "theme_inputLayout_boxColor", "theme_inputLayout_hintColor"
        )


        fun patchView(view: View, attr: String) {
            getInstant().patchView(view, attr)
        }

        fun patchView(view: ViewGroup, attr: String) {
            getInstant().patchView(view, attr)
        }

        fun patchPlugin(view: View, name: String) {
            getInstant().patchPlugin(view, name)
        }
    }

    var enableSkin = false

    private val bindings = ArrayList<WeakReference<ViewDataBinding?>>()
    private val styles = ArrayList<Pair<String, Int>>()
    val themes = ArrayMap<String, ArrayMap<String, Int>>()
    val themesJson = ArrayMap<String, ColorEntity>()
    val plugins = ArrayList<IPlugin<*>>()

    /**
     * 第一个添加的style为默认主题
     */
    fun addStyle(vararg style: Pair<String, Int>): SkinManager {
        styles.addAll(style.toList())
        return this
    }

    fun addPlugin(vararg iPlugin: IPlugin<*>): SkinManager {
        plugins.addAll(iPlugin.toList())
        return this
    }

    fun addJson(vararg json : Pair<String,ColorEntity>) : SkinManager{
        json.forEach {
            styles.add(it.first to -1)
            themesJson[it.first] = it.second
        }
        return this
    }

    fun build() {
        buildTheme()
    }

    private fun buildTheme() {
        if (styles.isEmpty()) return
        styles.forEach {
            if (themes[it.first] == null && it.second!=-1) {
                themes[it.first] = obtainAttr(it.second)
            }
        }
        currentStyle = styles[0].first
    }

    fun bindings(binding: ViewDataBinding?) {
        bindings.add(WeakReference(binding))
    }

    fun invalidateAll() {
        bindings.forEach {
            it.get()?.apply {
                invalidateAll()
            }
        }
    }

    fun switchThemeByName(name: String) {
        enableSkin = true//开启
        for (style in styles) {
            if (style.first == name) {
                currentStyle = style.first
                break
            }
        }
        onStyleChange?.invoke(name)
        invalidateAll()
    }

    /**
     * 仅修改某一个binding,适合新的binding同步主题，而非全部binding相关切换主题
     */
    fun autoTheme(name: String, binding: ViewDataBinding?) {
        if (name.isEmpty()) return
        enableSkin = true//开启
        for (style in styles) {
            if (style.first == name) {
                currentStyle = style.first
                break
            }
        }
        bindings.add(WeakReference(binding))
        binding?.invalidateAll()
    }


    private fun obtainAttr(style: Int): ArrayMap<String, Int> {
        val map = ArrayMap<String, Int>()
        val wrapper = ContextThemeWrapper(context, style)
        val array = wrapper.obtainStyledAttributes(style, attrs)
        for (i in attrs.indices) {
            map[styleName[i]] = array.getResourceId(i, -1)
        }
        array.recycle()
        return map
    }




    fun patchPlugin(view: View, name: String) {
        var iPlugin: IPlugin<View>? = null
        run Breaking@{
            plugins.forEach {
                if (it::class.java.simpleName == name) {
                    iPlugin = it as IPlugin<View>
                    return@Breaking
                }
            }
        }
        if(currentStyle.isStyleFromJson()){
            iPlugin?.individuate(view, currentStyle, currentStyle.getColorExtras())
        }else{
            iPlugin?.individuate(view, currentStyle)
        }
    }


    fun patchView(view: ViewGroup, attr: String) {
        if (!enableSkin) return
        val theme = themes[currentStyle]
        val themeJson = themesJson[currentStyle]
        val attrs = attr.split("|")
        if (attr.isEmpty()) return
        if(theme!=null){
            patchViewGroupByThemes(view, attrs, theme)
        }else if(themeJson!=null){
            patchViewGroupByJson(view,attrs,themeJson)
        }
    }



    /**
     * 通过style找皮肤资源
     */
    private fun patchViewGroupByThemes(view: ViewGroup,attrs: List<String>,theme:ArrayMap<String, Int>){
        when (view.viewType()) {
            ViewType.TextInputLayout -> {
                attrs.forEach {
                    when {
                        it.trim() == "boxColor" -> {
                            theme["theme_inputLayout_boxColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setBoxStrokeColor" }[0].invoke(
                                    view,
                                    getColor()
                                )
                            }
                        }
                        it.trim() == "hintColor" -> {
                            theme["theme_inputLayout_hintColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setHintTextColor" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            ViewType.CardView, ViewType.MaterialCardView -> {
                attrs.forEach {
                    when {
                        it.trim() == "strokeColor" && view.viewType() == ViewType.MaterialCardView -> {
                            theme["theme_card_strokeColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setStrokeColor" }[1].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                        it.trim() == "backgroundColor" -> {
                            theme["theme_card_backgroundColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setCardBackgroundColor" }[1].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            ViewType.BottomNavigationView -> {
                attrs.forEach {
                    when {
                        it.trim() == "iconTint" -> {
                            theme["theme_bottom_navigation_iconTint"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setItemIconTintList" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                        it.trim() == "textColor" -> {
                            theme["theme_bottom_navigation_textColor"]?.apply {
                                view::class.java.methods.filter { method -> method.name == "setItemTextColor" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                attrs.forEach {
                    when {
                        it.trim() == "background" -> {
                            theme["theme_viewGroup_background"]?.apply {
                                view.background = getDrawable()
                            }
                        }
                        it.trim() == "backgroundColor" -> {
                            theme["theme_viewGroup_backgroundColor"]?.apply {
                                view.setBackgroundColor(
                                    getColor()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 通过json找资源
     */
    private fun patchViewGroupByJson(view: ViewGroup,attrs: List<String>,theme:ColorEntity){
        when (view.viewType()) {
            ViewType.TextInputLayout -> {
                attrs.forEach {
                    when {
                        it.trim() == "boxColor" -> {
                            theme.theme_inputLayout_boxColor?.apply {
                                view::class.java.methods.filter { method -> method.name == "setBoxStrokeColor" }[0].invoke(
                                    view,
                                    getColor()
                                )
                            }
                        }
                        it.trim() == "hintColor" -> {
                            theme.theme_inputLayout_hintColor?.apply {
                                view::class.java.methods.filter { method -> method.name == "setHintTextColor" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            ViewType.CardView, ViewType.MaterialCardView -> {
                attrs.forEach {
                    when {
                        it.trim() == "strokeColor" && view.viewType() == ViewType.MaterialCardView -> {
                            theme.theme_card_strokeColor?.apply {
                                view::class.java.methods.filter { method -> method.name == "setStrokeColor" }[1].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                        it.trim() == "backgroundColor" -> {
                            theme.theme_card_backgroundColor?.apply {
                                view::class.java.methods.filter { method -> method.name == "setCardBackgroundColor" }[1].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            ViewType.BottomNavigationView -> {
                attrs.forEach {
                    when {
                        it.trim() == "iconTint" -> {
                            theme.theme_bottom_navigation_iconTint?.apply {
                                view::class.java.methods.filter { method -> method.name == "setItemIconTintList" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                        it.trim() == "textColor" -> {
                            theme.theme_bottom_navigation_textColor?.apply {
                                view::class.java.methods.filter { method -> method.name == "setItemTextColor" }[0].invoke(
                                    view,
                                    getColorStateList()
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                attrs.forEach {
                    when {
                        it.trim() == "background" -> {
                            theme.theme_viewGroup_background?.apply {
                                getDrawable{ drawable->
                                    view.background = drawable
                                }
                            }
                        }
                        it.trim() == "backgroundColor" -> {
                            theme.theme_viewGroup_backgroundColor?.apply {
                                view.setBackgroundColor(
                                    getColor()
                                )
                            }
                        }
                    }
                }
            }
        }

    }



    fun patchView(view: View, attr: String) {
        if (!enableSkin) return
        val attrs = attr.split("|")
        val theme = themes[currentStyle]
        val themeJson = themesJson[currentStyle]
        if (attr.isEmpty()) return
        if(theme!=null){
            patchViewByThemes(view, attrs, theme)
        }else if(themeJson!=null){
            patchViewByJson(view, attrs, themeJson)
        }
    }


    /**
     * 通过style找皮肤资源
     */
    private fun patchViewByThemes(view: View,attrs: List<String>,theme:ArrayMap<String, Int>){
        when (view.viewType()) {
            ViewType.EditText -> {
                theme.apply {
                    val ed = view as EditText
                    attrs.forEach {
                        when {
                            it.trim() == "highlightColor" ->{
                                this["theme_edit_highlightColor"]?.apply {
                                    android.widget.TextView::class.java.methods
                                        .filter { method -> method.name == "setHighlightColor" }[0]
                                        .invoke(ed, getColor())
                                }
                            }
                            it.trim() == "textColor" -> {
                                this["theme_edit_textColor"]?.apply {
                                    ed.setTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "hintColor" -> {
                                this["theme_edit_hintColor"]?.apply {
                                    ed.setHintTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "cursor" -> {
                                this["theme_edit_cursorDrawable"]?.apply {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        ed.textCursorDrawable = getDrawable()
                                    } else {
                                        try {
                                            val field = android.widget.TextView::class.java.getDeclaredField("mCursorDrawableRes")
                                            field.isAccessible = true
                                            field.set(ed,this)
                                        }catch (e:Exception){
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.FloatingActionButton -> {
                theme.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "backgroundColor" -> {
                                this["theme_floating_backgroundColor"]?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setBackgroundTintList" }[0].invoke(
                                        view,
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "tint" -> {
                                this["theme_floating_tint"]?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setImageTintList" }[0].invoke(
                                        view,
                                        getColorStateList()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            ViewType.ImageView -> {
                theme?.apply {
                    val iv = view as ImageView
                    attrs.forEach {
                        when {
                            it.trim() == "tint" -> {
                                this["theme_imageView_tint"]?.apply {
                                    iv.imageTintList = getColorStateList()
                                }
                            }
                        }
                    }
                }
            }
            ViewType.TextView, ViewType.MaterialTextView -> {
                val tv = view as TextView
                theme.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                this["theme_text_color"]?.apply { tv.setTextColor(getColorStateList()) }
                            }
                            it.trim() == "background" -> {
                                this["theme_text_background"]?.apply {
                                    tv.background = getDrawable()
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                this["theme_text_backgroundColor"]?.apply {
                                    tv.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_text_drawableTint"]?.apply {
                                        tv.compoundDrawableTintList = getColorStateList()
                                    }

                                } else {
                                    tv.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_text_drawableTint"]?.apply {
                                                drawable.setTintList(
                                                    getColorStateList()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.Button, ViewType.MaterialButton -> {
                val button = view as Button
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                this["theme_button_textColor"]?.apply {
                                    button.setTextColor(
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "rippleColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    this["theme_button_rippleColor"]?.apply {
                                        view::class.java.methods.filter { method -> method.name == "setRippleColor" }[0].invoke(
                                            view,
                                            getColorStateList()
                                        )
                                    }
                                }
                            }
                            it.trim() == "background" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    this["theme_button_background"]?.apply {
                                        button.backgroundTintList = getColorStateList()
                                    }
                                } else {
                                    this["theme_button_background"]?.apply {
                                        button.background = getDrawable()
                                    }
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    this["theme_button_backgroundColor"]?.apply {
                                        button.backgroundTintList = getColorStateList()
                                    }
                                } else {
                                    this["theme_button_backgroundColor"]?.apply {
                                        button.setBackgroundColor(
                                            getColor()
                                        )
                                    }

                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_button_drawableTint"]?.apply {
                                        button.compoundDrawableTintList = getColorStateList()
                                    }

                                } else {
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_button_drawableTint"]?.apply {
                                                drawable.setTintList(
                                                    getColorStateList()
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                            it.trim() == "iconTint" -> {
                                this["theme_button_iconTint"]?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setIconTint" }[0].invoke(
                                        button,
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "strokeColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    this["theme_button_strokeColor"]?.apply {
                                        view::class.java.methods.filter { method -> method.name == "setStrokeColor" }[0].invoke(
                                            button,
                                            getColorStateList()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.RadioButton, ViewType.MaterialRadioButton -> {
                val button = view as RadioButton
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                this["theme_radio_textColor"]?.apply {
                                    button.setTextColor(
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "background" -> {
                                this["theme_radio_background"]?.apply {
                                    button.background = getDrawable()
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                this["theme_radio_backgroundColor"]?.apply {
                                    button.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    this["theme_radio_drawableTint"]?.apply {
                                        button.compoundDrawableTintList = getColorStateList()
                                    }
                                } else {
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme["theme_radio_drawableTint"]?.apply {
                                                setTintList(
                                                    getColorStateList()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            it.trim() == "buttonTint" -> {
                                this["theme_radio_buttonTint"]?.apply {
                                    button.buttonTintList = getColorStateList()
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "background" -> {
                                theme["theme_viewGroup_background"]?.apply {
                                    view.background = getDrawable()
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                theme["theme_viewGroup_backgroundColor"]?.apply {
                                    view.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun patchViewByJson(view: View,attrs: List<String>,theme:ColorEntity){
        when (view.viewType()) {
            ViewType.EditText -> {
                theme?.apply {
                    val ed = view as EditText
                    attrs.forEach {
                        when {
                            it.trim() == "highlightColor" ->{
                                theme_edit_highlightColor?.apply {
                                    android.widget.TextView::class.java.methods
                                        .filter { method -> method.name == "setHighlightColor" }[0]
                                        .invoke(ed, getColor())
                                }
                            }
                            it.trim() == "textColor" -> {
                                theme_edit_textColor?.apply {
                                    ed.setTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "hintColor" -> {
                                theme_edit_hintColor?.apply {
                                    ed.setHintTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "cursor" -> {
                                theme_edit_cursorDrawable?.apply {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        ed.textCursorDrawable = getCursorDrawable()
                                    }else{
                                        val field = android.widget.TextView::class.java.
                                            getDeclaredField("mCursorDrawable")
                                        field.isAccessible = true
                                        field.set(ed,getCursorDrawable())
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.FloatingActionButton -> {
                theme.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "backgroundColor" -> {
                                theme_floating_backgroundColor?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setBackgroundTintList" }[0].invoke(
                                        view,
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "tint" -> {
                                theme_floating_tint?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setImageTintList" }[0].invoke(
                                        view,
                                        getColorStateList()
                                    )
                                }
                            }
                        }
                    }
                }
            }
            ViewType.ImageView -> {
                theme?.apply {
                    val iv = view as ImageView
                    attrs.forEach {
                        when {
                            it.trim() == "tint" -> {
                                theme_imageView_tint?.apply {
                                    iv.imageTintList = getColorStateList()
                                }
                            }
                        }
                    }
                }
            }
            ViewType.TextView, ViewType.MaterialTextView -> {
                val tv = view as TextView
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                theme_text_color?.apply { tv.setTextColor(getColorStateList()) }
                            }
                            it.trim() == "background" -> {
                                theme_text_background?.apply {
                                    getDrawable{d->
                                        tv.background = d
                                    }
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                theme_text_backgroundColor?.apply {
                                    tv.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    theme_text_drawableTint?.apply {
                                        tv.compoundDrawableTintList = getColorStateList()
                                    }

                                } else {
                                    tv.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme_text_drawableTint?.apply {
                                                drawable.setTintList(
                                                    getColorStateList()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.Button, ViewType.MaterialButton -> {
                val button = view as Button
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                theme_button_textColor?.apply {
                                    button.setTextColor(
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "rippleColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    theme_button_rippleColor?.apply {
                                        view::class.java.methods.filter { method -> method.name == "setRippleColor" }[0].invoke(
                                            view,
                                            getColorStateList()
                                        )
                                    }
                                }
                            }
                            it.trim() == "background" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    theme_button_background?.apply {
                                        button.backgroundTintList = getColorStateList()
                                    }
                                } else {
                                    theme_button_background?.apply {
                                          getDrawable{d->
                                              button.background = d
                                          }
                                    }
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    theme_button_backgroundColor?.apply {
                                        button.backgroundTintList = getColorStateList()
                                    }
                                } else {
                                    theme_button_backgroundColor?.apply {
                                        button.setBackgroundColor(
                                            getColor()
                                        )
                                    }

                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    theme_button_drawableTint?.apply {
                                        button.compoundDrawableTintList = getColorStateList()
                                    }

                                } else {
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme_button_drawableTint?.apply {
                                                drawable.setTintList(
                                                    getColorStateList()
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                            it.trim() == "iconTint" -> {
                                theme_button_iconTint?.apply {
                                    view::class.java.methods.filter { method -> method.name == "setIconTint" }[0].invoke(
                                        button,
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "strokeColor" -> {
                                if (view.viewType() == ViewType.MaterialButton) {
                                    theme_button_strokeColor?.apply {
                                        view::class.java.methods.filter { method -> method.name == "setStrokeColor" }[0].invoke(
                                            button,
                                            getColorStateList()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ViewType.RadioButton, ViewType.MaterialRadioButton -> {
                val button = view as RadioButton
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                theme_radio_textColor?.apply {
                                    button.setTextColor(
                                        getColorStateList()
                                    )
                                }
                            }
                            it.trim() == "background" -> {
                                theme_radio_background?.apply {
                                    getDrawable{d ->
                                        button.background = d
                                    }
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                theme_radio_backgroundColor?.apply {
                                    button.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                            it.trim() == "drawableTint" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    theme_radio_drawableTint?.apply {
                                        button.compoundDrawableTintList = getColorStateList()
                                    }
                                } else {
                                    button.compoundDrawables.forEach { drawable ->
                                        drawable?.apply {
                                            theme_radio_drawableTint?.apply {
                                                setTintList(
                                                    getColorStateList()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            it.trim() == "buttonTint" -> {
                                theme_radio_buttonTint?.apply {
                                    button.buttonTintList = getColorStateList()
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                theme?.apply {
                    attrs.forEach {
                        when {
                            it.trim() == "background" -> {
                                theme_viewGroup_background?.apply {
                                    getDrawable{ d ->
                                        view.background = d
                                    }
                                }
                            }
                            it.trim() == "backgroundColor" -> {
                                theme_viewGroup_backgroundColor?.apply {
                                    view.setBackgroundColor(
                                        getColor()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private var onStyleChange: ((style: String) -> Unit)? = null
    fun setOnStyleChangeListener(onStyleChange: ((style: String) -> Unit)) {
        this.onStyleChange = onStyleChange
    }

    private fun String.getCursorDrawable() : Drawable?{
        return try {
            val drawable = GradientDrawable()
            drawable.color = ColorStateList.valueOf(Color.parseColor("#$this"))
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setSize(2,-1)
            drawable
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    private fun String.getDrawable(result : (drawable:Drawable?)->Unit) {
        if(this.contains("Https",true)||this.contains("http",true)){
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val call = Drawable.createFromStream(URL(this@getDrawable).openStream(),"")
                    withContext(Dispatchers.Main){
                        result.invoke(call)
                    }
                }catch (e: IOException){
                    result.invoke(null)
                }
            }
        }else{
            val drawable = ColorDrawable(Color.parseColor("#$this"))
            result.invoke(drawable)
        }
    }


    private fun Int.getDrawable(): Drawable? {
        return if (this != -1) {
            ContextCompat.getDrawable(context, this)
        } else {
            null
        }
    }


    private fun String.getColor(): Int {
        return if (this.isNotEmpty()) {
            return Color.parseColor("#$this")
        } else {
            Color.WHITE
        }
    }

    private fun Int.getColor(): Int {
        return if (this != -1) {
            ContextCompat.getColor(context, this)
        } else {
            Color.WHITE
        }
    }

    private fun String.getColorStateList(): ColorStateList? {
        return if (this.isNotEmpty()) {
            ColorStateList.valueOf(Color.parseColor("#$this"))
        } else {
            ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun Int.getColorStateList(): ColorStateList? {
        return if (this != -1) {
            ContextCompat.getColorStateList(context, this)
        } else {
            ColorStateList.valueOf(Color.WHITE)
        }
    }


}
