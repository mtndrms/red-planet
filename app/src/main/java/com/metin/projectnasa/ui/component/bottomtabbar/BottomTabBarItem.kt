package com.metin.projectnasa.ui.component.bottomtabbar

import android.graphics.RectF

data class BottomTabBarItem(
    var title: String,
    var contentDescription: String,
    var rect: RectF = RectF(),
    var alpha: Int
)
