package com.metin.projectnasa.presentation.component.bottomtabbar

import android.graphics.RectF

data class BottomTabBarItem(
    var title: String,
    var contentDescription: String,
    var rect: RectF = RectF(),
    var alpha: Int
)
