package com.show.kcore.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


open class DataBindingViewHolder<T : ViewBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)