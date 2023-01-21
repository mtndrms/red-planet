package com.metin.projectnasa.ui.components

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class BottomTabBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var indicatorLocation = barSideMargins
    private var rect = RectF()
    private var items = listOf<BottomTabBarItem>()

    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_INDICATOR_COLOR = "#2DFFFFFF"
        private const val DEFAULT_TINT = "#C8FFFFFF"

        // corner flags
        private const val NO_CORNERS = 0
        private const val TOP_LEFT_CORNER = 1
        private const val TOP_RIGHT_CORNER = 2
        private const val BOTTOM_RIGHT_CORNER = 4
        private const val BOTTOM_LEFT_CORNER = 8
        private const val ALL_CORNERS = 15

        private const val DEFAULT_SIDE_MARGIN = 10f
        private const val DEFAULT_ITEM_PADDING = 10f
        private const val DEFAULT_ANIM_DURATION = 200L
        private const val DEFAULT_ICON_SIZE = 18F
        private const val DEFAULT_ICON_MARGIN = 4F
        private const val DEFAULT_TEXT_SIZE = 12F
        private const val DEFAULT_CORNER_RADIUS = 10F
        private const val DEFAULT_BAR_CORNER_RADIUS = 20F
        private const val DEFAULT_BAR_CORNERS = TOP_LEFT_CORNER or TOP_RIGHT_CORNER

        private const val OPAQUE = 255
        private const val TRANSPARENT = 0
    }
}