package com.showmethe.skinlib

import android.view.View
import android.view.ViewGroup

/**
 * com.example.ken.kmvvm.skin
 * 2020/1/15
 **/

private const val TextView = "androidx.appcompat.widget.AppCompatTextView"
private const val MaterialTextView = "com.google.android.material.textview.MaterialTextView"

private const val Button = "androidx.appcompat.widget.AppCompatButton"
private const val MaterialButton = "com.google.android.material.button.MaterialButton"

private const val RadioButton = "androidx.appcompat.widget.AppCompatRadioButton"
private const val MaterialRadioButton = "com.google.android.material.radiobutton.MaterialRadioButton"

private const val CardView = "androidx.cardview.widget.CardView"
private const val MaterialCardView = "com.google.android.material.card.MaterialCardView"



enum class ViewType{
    View,ViewGroup,TextView,MaterialTextView,Button,MaterialButton,RadioButton,MaterialRadioButton,CardView,MaterialCardView
}

fun View.viewType (): ViewType {
    return when (this::class.java.name) {
        TextView -> {
            ViewType.TextView
        }
        MaterialTextView -> {
            ViewType.MaterialTextView
        }
        Button -> {
            ViewType.Button
        }
        MaterialButton -> {
            ViewType.MaterialButton
        }
        RadioButton -> {
            ViewType.RadioButton
        }
        MaterialRadioButton -> {
            ViewType.MaterialRadioButton
        }
        else -> ViewType.View
    }
}

fun ViewGroup.viewType (): ViewType {
    return when (this::class.java.name) {
        CardView -> {
            ViewType.CardView
        }
        MaterialCardView -> {
            ViewType.MaterialCardView
        }
        else -> ViewType.ViewGroup
    }
}