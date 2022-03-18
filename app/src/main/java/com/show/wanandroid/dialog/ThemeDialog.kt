package com.show.wanandroid.dialog

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.shape.CornerFamily
import com.show.kcore.adapter.divider.GridSpaceItemDecoration
import com.show.kcore.dialog.SimpleDialogFragment
import com.show.kcore.dialog.WindowParam
import com.show.wanandroid.R
import com.show.wanandroid.databinding.DialogThemeBinding
import com.show.wanandroid.dialog.adapter.ThemeAdapter
import com.show.wanandroid.flutter.FlutterRouter
import com.show.wanandroid.themes_res


@WindowParam(outSideCanceled = true, canceled = true)
class ThemeDialog : SimpleDialogFragment() {

    private val themes = ObservableArrayList<Int>().apply {
        clear()
        addAll(themes_res)
    }
    private val adapter by lazy { ThemeAdapter(requireContext(), themes) }

    override fun build(savedInstanceState: Bundle?) {
        buildDialog {
            R.layout.dialog_theme
        }
        onView {
            onBindingView<DialogThemeBinding> {
                rv.adapter = adapter
                rv.layoutManager = GridLayoutManager(context, 3)
                rv.addItemDecoration(GridSpaceItemDecoration(3, 15))

                adapter.setOnItemClickListener { view, data, position ->
                    FlutterRouter.updateColor(position)
                    onThemeClick?.invoke(position)
                    dialog?.dismiss()
                }
            }
        }
    }

    private var onThemeClick: ((position: Int) -> Unit)? = null
    fun setOnThemeClickListener(onThemeClick: ((position: Int) -> Unit)) {
        this.onThemeClick = onThemeClick
    }

}