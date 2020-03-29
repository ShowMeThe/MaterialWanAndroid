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
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.CardViewBindingAdapter
import com.showmethe.skinlib.plugin.IPlugin
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Author: showMeThe
 * Update Time: 2020/1/17 14:28
 * Package Name:com.showmethe.skinlib
 */
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
            R.attr.theme_button_background,
            R.attr.theme_button_backgroundColor,
            R.attr.theme_button_drawableTint,
            R.attr.theme_button_iconTint,

            R.attr.theme_radio_textColor,
            R.attr.theme_radio_background,
            R.attr.theme_radio_backgroundColor,
            R.attr.theme_radio_drawableTint,
            R.attr.theme_radio_buttonTint,

            R.attr.theme_bottom_navigation_iconTint,
            R.attr.theme_bottom_navigation_textColor,

            R.attr.theme_imageView_tint,

            R.attr.theme_floating_tint, R.attr.theme_floating_backgroundColor,

            R.attr.them_edit_textColor, R.attr.them_edit_hintColor, R.attr.them_edit_cursorDrawable
        )


        private val styleName = arrayOf(
            "theme_viewGroup_background", "theme_viewGroup_backgroundColor",
            "theme_card_backgroundColor", "theme_card_strokeColor",

            "theme_text_color", "theme_text_background",
            "theme_text_backgroundColor", "theme_text_drawableTint",

            "theme_button_textColor",
            "theme_button_background", "theme_button_backgroundColor",
            "theme_button_drawableTint", "theme_button_iconTint",

            "theme_radio_textColor", "theme_radio_background",
            "theme_radio_backgroundColor", "theme_radio_drawableTint", "theme_radio_buttonTint",

            "theme_bottom_navigation_iconTint", "theme_bottom_navigation_textColor",

            "theme_imageView_tint",

            "theme_floating_tint", "theme_floating_backgroundColor",

            "them_edit_textColor", "them_edit_hintColor","them_edit_cursorDrawable"
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
    private val themes = ArrayMap<String, ArrayMap<String, Int>>()
    private val plugins = ArrayList<IPlugin<*>>()

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

    fun build() {
        buildTheme()
    }

    private fun buildTheme() {
        if (styles.isEmpty()) return
        styles.forEach {
            if (themes[it.first] == null) {
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
        iPlugin?.individuate(view, currentStyle)
    }


    fun patchView(view: ViewGroup, attr: String) {
        if (!enableSkin) return
        val theme = themes[currentStyle]!!
        val attrs = attr.split("|")
        if (attr.isEmpty()) return
        when (view.viewType()) {
            ViewType.CardView, ViewType.MaterialCardView -> {
                attrs.forEach {
                    when {
                        it.trim() == "divideColor" && view.viewType() == ViewType.MaterialCardView -> {
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

    fun patchView(view: View, attr: String) {
        if (!enableSkin) return
        val attrs = attr.split("|")
        val theme = themes[currentStyle]
        if (attr.isEmpty()) return
        when (view.viewType()) {
            ViewType.EditText -> {
                theme?.apply {
                    val ed = view as EditText
                    attrs.forEach {
                        when {
                            it.trim() == "textColor" -> {
                                this["them_edit_textColor"]?.apply {
                                    ed.setTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "hintColor" -> {
                                this["them_edit_hintColor"]?.apply {
                                    ed.setHintTextColor(getColorStateList())
                                }
                            }
                            it.trim() == "cursor" ->{
                                this["them_edit_cursorDrawable"]?.apply {
                                    android.widget.TextView::class.java.methods
                                        .filter { method -> method.name == "setTextCursorDrawable" }[1]
                                        .invoke(ed,getDrawable())
                                }
                            }
                        }
                    }
                }
            }
            ViewType.FloatingActionButton -> {
                theme?.apply {
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
                                        getColorStateList())
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
                theme?.apply {
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

            }
        }
    }

    private var onStyleChange: ((style: String) -> Unit)? = null
    fun setOnStyleChangeListener(onStyleChange: ((style: String) -> Unit)) {
        this.onStyleChange = onStyleChange
    }

    private fun Int.getDrawable(): Drawable? {
        return if (this != -1) {
            ContextCompat.getDrawable(context, this)
        } else {
            null
        }
    }

    private fun Int.getColor(): Int {
        return if (this != -1) {
            return ContextCompat.getColor(context, this)
        } else {
            Color.WHITE
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
