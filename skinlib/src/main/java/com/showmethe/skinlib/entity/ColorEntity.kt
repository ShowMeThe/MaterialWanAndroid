package com.showmethe.skinlib.entity

import java.util.ArrayList

data class ColorEntity(var theme_viewGroup_background :String? =null,
                       var theme_viewGroup_backgroundColor :String? = null,
                       var theme_card_backgroundColor :String? = null,
                       var theme_card_strokeColor :String? = null,
                       var theme_text_color :String? = null,
                       var theme_text_background :String? = null,
                       var theme_text_backgroundColor :String? = null,
                       var theme_text_drawableTint :String? = null,
                       var theme_button_textColor :String? = null,
                       var theme_button_rippleColor :String? = null,
                       var theme_button_background :String? = null,
                       var theme_button_backgroundColor :String? = null,
                       var theme_button_drawableTint :String? = null,
                       var theme_button_iconTint :String? = null,
                       var theme_button_strokeColor :String? = null,
                       var theme_radio_textColor :String? = null,
                       var theme_radio_background :String? = null,
                       var theme_radio_backgroundColor :String? = null,
                       var theme_radio_drawableTint :String? = null,
                       var theme_radio_buttonTint :String? = null,
                       var theme_bottom_navigation_iconTint :String? = null,
                       var theme_bottom_navigation_textColor :String? = null,
                       var theme_bottom_navigation_rippleColor:String? = null,
                       var theme_imageView_tint :String? = null,
                       var theme_floating_tint :String? = null,
                       var theme_floating_backgroundColor :String? = null,
                       var theme_edit_textColor :String? = null,
                       var theme_edit_hintColor :String? = null,
                       var theme_edit_cursorDrawable :String? = null,
                       var theme_edit_highlightColor :String? = null,
                       var theme_inputLayout_boxColor :String? = null,
                       var theme_inputLayout_hintColor :String? = null,
                       var colorObjects : List<String> = ArrayList()
                       )