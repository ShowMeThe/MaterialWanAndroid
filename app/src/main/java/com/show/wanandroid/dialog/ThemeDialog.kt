package com.show.wanandroid.dialog

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.shape.CornerFamily
import com.show.wanandroid.R
import com.show.wanandroid.dialog.adapter.ThemeAdapter
import com.show.wanandroid.themes_res
import kotlinx.android.synthetic.main.dialog_theme.view.*
import showmethe.github.core.adapter.GridSpaceItemDecoration
import showmethe.github.core.dialog.SimpleDialogFragment
import showmethe.github.core.dialog.WindowParam
import showmethe.github.core.util.extras.CornerType.Companion.ALL
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.createDrawable

@WindowParam(outSideCanceled = true,canceled = true)
class ThemeDialog  : SimpleDialogFragment() {
    private lateinit var adapter: ThemeAdapter
    private val themes = ObList<Int>()
    override fun build(savedInstanceState: Bundle?) {
        buildDialog {
            R.layout.dialog_theme
        }
        onView {
            it.apply {
                adapter = ThemeAdapter(context,themes)
                rv.adapter = adapter
                rv.layoutManager = GridLayoutManager(context,3)
                rv.addItemDecoration(GridSpaceItemDecoration(3,15))

                bg.background = createDrawable(context,CornerFamily.CUT,35, Color.WHITE,ALL)

                adapter.setOnItemClickListener { view, position ->
                    onThemeClick?.invoke(position)
                    dialog?.dismiss()
                }

                container.setOnClickListener {
                    dialog?.dismiss()
                }

            }
        }
    }

    private var onThemeClick : ((position : Int)->Unit)? = null
    fun setOnThemeClickListener(onThemeClick : ((position : Int)->Unit)){
        this.onThemeClick = onThemeClick
    }


    fun setThemes(){
        themes.clear()
        themes.addAll(themes_res)
    }
}