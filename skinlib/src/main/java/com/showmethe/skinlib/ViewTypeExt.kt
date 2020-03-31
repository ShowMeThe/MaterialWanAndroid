package com.showmethe.skinlib

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NavigationRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

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

private const val BottomNavigationView = "com.google.android.material.bottomnavigation.BottomNavigationView"

private const val ImageView = "androidx.appcompat.widget.AppCompatImageView"

private const val FloatingActionButton = "com.google.android.material.floatingactionbutton.FloatingActionButton"

private const val EditText = "androidx.appcompat.widget.AppCompatEditText"
private const val TextInputEditText = "com.google.android.material.textfield.TextInputEditText"

private const val TextInputLayout = "com.google.android.material.textfield.TextInputLayout"


enum class ViewType{
    View,ViewGroup,TextView,MaterialTextView,
    Button,MaterialButton,RadioButton,
    MaterialRadioButton,CardView,MaterialCardView,
    BottomNavigationView,ImageView,FloatingActionButton,
    EditText,TextInputLayout
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
        ImageView ->{
            ViewType.ImageView
        }
        FloatingActionButton->{
            ViewType.FloatingActionButton
        }
        EditText,TextInputEditText ->{
            ViewType.EditText
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
        BottomNavigationView ->{
            ViewType.BottomNavigationView
        }
        TextInputLayout ->{
            ViewType.TextInputLayout
        }
        else -> ViewType.ViewGroup
    }
}