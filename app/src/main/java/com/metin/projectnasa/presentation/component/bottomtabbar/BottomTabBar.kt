package com.metin.projectnasa.presentation.component.bottomtabbar

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.animation.DecelerateInterpolator
import android.widget.PopupMenu
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FontRes
import androidx.annotation.XmlRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.metin.projectnasa.R
import com.metin.projectnasa.common.ContextExtensions.dp2px

class BottomTabBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var exploreByTouchHelper: AccessibleExploreByTouchHelper

    private var itemWidth: Float = 0F
    private var indicatorLocation = barSideMargins
    private var rect = RectF()
    private var items: List<BottomTabBarItem> = listOf()

    // Attribute Defaults
    @ColorInt
    private var _barBackgroundColor = Color.WHITE

    @ColorInt
    private var _barIndicatorColor = Color.parseColor(DEFAULT_INDICATOR_COLOR)

    @Dimension
    private var _barIndicatorRadius = context.dp2px(DEFAULT_INDICATOR_CORNER_RADIUS)

    @Dimension
    private var _barSideMargins = context.dp2px(DEFAULT_SIDE_MARGIN)

    @Dimension
    private var _barCornerRadius = context.dp2px(DEFAULT_BAR_CORNER_RADIUS)
    private var _barCorners = DEFAULT_BAR_CORNERS

    @Dimension
    private var _itemPadding = context.dp2px(DEFAULT_ITEM_PADDING)
    private var _itemAnimDuration = DEFAULT_ANIM_DURATION

    @ColorInt
    private var _itemTextColor = Color.WHITE

    @Dimension
    private var _itemTextSize = context.dp2px(DEFAULT_TEXT_SIZE)

    @FontRes
    private var _itemFontFamily: Int = INVALID_RES

    @XmlRes
    private var _itemMenuRes: Int = INVALID_RES
    private var _itemActiveIndex: Int = 0
    private lateinit var menu: Menu

    // Core Attributes
    private var barBackgroundColor: Int
        @ColorInt get() = _barBackgroundColor
        set(@ColorInt value) {
            _barBackgroundColor = value
            paintBackground.color = value
            invalidate()
        }

    private var barIndicatorColor: Int
        @ColorInt get() = _barIndicatorColor
        set(@ColorInt value) {
            _barIndicatorColor = value
            paintIndicator.color = value
            invalidate()
        }

    private var barIndicatorRadius: Float
        @Dimension get() = _barIndicatorRadius
        set(@Dimension value) {
            _barIndicatorRadius = value
            invalidate()
        }

    private var barSideMargins: Float
        @Dimension get() = _barSideMargins
        set(@Dimension value) {
            _barSideMargins = value
            invalidate()
        }

    private var barCornerRadius: Float
        @Dimension get() = _barCornerRadius
        set(@Dimension value) {
            _barCornerRadius = value
            invalidate()
        }

    private var barCorners: Int
        get() = _barCorners
        set(value) {
            _barCorners = value
            invalidate()
        }

    private var itemTextSize: Float
        @Dimension get() = _itemTextSize
        set(@Dimension value) {
            _itemTextSize = value
            paintText.textSize = value
            invalidate()
        }

    private var itemTextColor: Int
        @ColorInt get() = _itemTextColor
        set(@ColorInt value) {
            _itemTextColor = value
            paintText.color = value
            invalidate()
        }

    private var itemPadding: Float
        @Dimension get() = _itemPadding
        set(@Dimension value) {
            _itemPadding = value
            invalidate()
        }

    private var itemAnimDuration: Long
        get() = _itemAnimDuration
        set(value) {
            _itemAnimDuration = value
        }

    private var itemFontFamily: Int
        @FontRes get() = _itemFontFamily
        set(@FontRes value) {
            _itemFontFamily = value
            if (value != INVALID_RES) {
                paintText.typeface = ResourcesCompat.getFont(context, value)
                invalidate()
            }
        }

    private var itemMenuRes: Int
        @XmlRes get() = _itemMenuRes
        set(value) {
            _itemMenuRes = value
            val popupMenu = PopupMenu(context, null)
            popupMenu.inflate(value)
            this.menu = popupMenu.menu
            if (value != INVALID_RES) {
                items = BottomBarParser(context, value).parse()
                invalidate()
            }
        }

    var itemActiveIndex: Int
        get() = _itemActiveIndex
        set(value) {
            _itemActiveIndex = value
            applyItemActiveIndex()
        }

    // Listeners
    var onItemSelectedListener: OnItemSelectedListener? = null

    var onItemReselectedListener: OnItemReselectedListener? = null

    var onItemSelected: ((Int) -> Unit)? = null

    var onItemReselected: ((Int) -> Unit)? = null

    // Paints
    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barIndicatorColor
    }

    private val paintIndicator = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barIndicatorColor
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = itemTextColor
        textSize = itemTextSize
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
        exploreByTouchHelper = AccessibleExploreByTouchHelper(this, items, ::onClickAction)

        ViewCompat.setAccessibilityDelegate(this, exploreByTouchHelper)
    }

    // Set attributes when app compiled
    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BottomNavigationBar,
            defStyleAttr,
            0
        )

        try {
            barBackgroundColor = typedArray.getColor(
                R.styleable.BottomNavigationBar_backgroundColor,
                barBackgroundColor
            )
            barIndicatorColor = typedArray.getColor(
                R.styleable.BottomNavigationBar_indicatorColor,
                barIndicatorColor
            )
            barIndicatorRadius = typedArray.getDimension(
                R.styleable.BottomNavigationBar_indicatorRadius,
                barIndicatorRadius
            )
            barSideMargins = typedArray.getDimension(
                R.styleable.BottomNavigationBar_sideMargins,
                barSideMargins
            )
            barCornerRadius = typedArray.getDimension(
                R.styleable.BottomNavigationBar_cornerRadius,
                barCornerRadius
            )
            barCorners = typedArray.getInteger(
                R.styleable.BottomNavigationBar_corners,
                barCorners
            )
            itemPadding = typedArray.getDimension(
                R.styleable.BottomNavigationBar_itemPadding,
                itemPadding
            )
            itemTextColor = typedArray.getColor(
                R.styleable.BottomNavigationBar_textColor,
                itemTextColor
            )
            itemTextSize = typedArray.getDimension(
                R.styleable.BottomNavigationBar_textSize,
                itemTextSize
            )
            itemActiveIndex = typedArray.getInt(
                R.styleable.BottomNavigationBar_activeItem,
                itemActiveIndex
            )
            itemFontFamily = typedArray.getResourceId(
                R.styleable.BottomNavigationBar_itemFontFamily,
                itemFontFamily
            )
            itemAnimDuration = typedArray.getInt(
                R.styleable.BottomNavigationBar_duration,
                itemAnimDuration.toInt()
            ).toLong()
            itemMenuRes = typedArray.getResourceId(
                R.styleable.BottomNavigationBar_menu,
                itemMenuRes
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        var lastX = barSideMargins
        itemWidth = (width - (barSideMargins * 2)) / items.size

        // reverse items layout order if layout direction is RTL
        val itemsToLayout = if (layoutDirection == LAYOUT_DIRECTION_RTL) items.reversed() else items

        for (item in itemsToLayout) {
            // Prevent text overflow by shortening the item title
            var shorted = false
            while (paintText.measureText(item.title) > itemWidth - (itemPadding * 2)) {
                item.title = item.title.dropLast(1)
                shorted = true
            }

            // Add ellipsis character to item text if it is shorted
            if (shorted) {
                item.title = item.title.dropLast(1)
                item.title += context.getString(R.string.ellipsis)
            }

            item.rect = RectF(lastX, 0f, itemWidth + lastX, height.toFloat())
            lastX += itemWidth
        }

        // Set initial active item
        applyItemActiveIndex()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background
        if (barCornerRadius > 0) {
            canvas.drawRoundRect(
                0f, 0f,
                width.toFloat(),
                height.toFloat(),
                minOf(barCornerRadius, height.toFloat() / 2),
                minOf(barCornerRadius, height.toFloat() / 2),
                paintBackground
            )

            if (barCorners != ALL_CORNERS) {

                if ((barCorners and TOP_LEFT_CORNER) != TOP_LEFT_CORNER) {
                    // Draw a box to cover the curve on the top left
                    canvas.drawRect(
                        0f, 0f, width.toFloat() / 2,
                        height.toFloat() / 2, paintBackground
                    )
                }

                if ((barCorners and TOP_RIGHT_CORNER) != TOP_RIGHT_CORNER) {
                    // Draw a box to cover the curve on the top right
                    canvas.drawRect(
                        width.toFloat() / 2, 0f, width.toFloat(),
                        height.toFloat() / 2, paintBackground
                    )
                }

                if ((barCorners and BOTTOM_LEFT_CORNER) != BOTTOM_LEFT_CORNER) {
                    // Draw a box to cover the curve on the bottom left
                    canvas.drawRect(
                        0f, height.toFloat() / 2, width.toFloat() / 2,
                        height.toFloat(), paintBackground
                    )
                }

                if ((barCorners and BOTTOM_RIGHT_CORNER) != BOTTOM_RIGHT_CORNER) {
                    // Draw a box to cover the curve on the bottom right
                    canvas.drawRect(
                        width.toFloat() / 2, height.toFloat() / 2, width.toFloat(),
                        height.toFloat(), paintBackground
                    )
                }
            }
        } else {
            canvas.drawRect(
                0f, 0f,
                width.toFloat(),
                height.toFloat(),
                paintBackground
            )
        }

        // Draw indicator
        rect.left = indicatorLocation + context.dp2px(
            DEFAULT_SIDE_MARGIN
        )
        rect.top = (items[itemActiveIndex].rect.centerY() - itemPadding - (context.dp2px(
            DEFAULT_SIDE_MARGIN
        ) / 1.5)).toFloat()
        rect.right = indicatorLocation + itemWidth - context.dp2px(
            DEFAULT_SIDE_MARGIN
        )
        rect.bottom = (items[itemActiveIndex].rect.centerY() + itemPadding + (context.dp2px(
            DEFAULT_SIDE_MARGIN
        ) / 1.5)).toFloat()

        canvas.drawRoundRect(
            rect,
            barIndicatorRadius,
            barIndicatorRadius,
            paintIndicator
        )
        val textHeight = (paintText.descent() + paintText.ascent()) / 2

        if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            for (item in items) {
                paintText.alpha = item.alpha
                canvas.drawText(
                    item.title,
                    item.rect.centerX(),
                    item.rect.centerY() - textHeight, paintText
                )
            }

        } else {
            for (item in items) {
                paintText.alpha = item.alpha
                canvas.drawText(
                    item.title,
                    item.rect.centerX(),
                    item.rect.centerY() - textHeight, paintText
                )
            }
        }
    }

    /**
     * Handle item clicks
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                for ((i, item) in items.withIndex()) {
                    if (item.rect.contains(event.x, event.y)) {
                        onClickAction(i)
                        break
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        return exploreByTouchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event)
    }

    private fun onClickAction(viewId: Int) {
        exploreByTouchHelper.invalidateVirtualView(viewId)
        if (viewId != itemActiveIndex) {
            itemActiveIndex = viewId
            onItemSelected?.invoke(viewId)
            onItemSelectedListener?.onItemSelect(viewId)
        } else {
            onItemReselected?.invoke(viewId)
            onItemReselectedListener?.onItemReselect(viewId)
        }
        exploreByTouchHelper.sendEventForVirtualView(
            viewId,
            AccessibilityEvent.TYPE_VIEW_CLICKED
        )
    }

    private fun applyItemActiveIndex() {
        if (items.isNotEmpty()) {
            for (item in items) {
                animateAlpha(item)
            }

            // change indicator's location
            ValueAnimator.ofFloat(indicatorLocation, items[itemActiveIndex].rect.left).apply {
                duration = itemAnimDuration
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    indicatorLocation = animation.animatedValue as Float
                }
                start()
            }
        }
    }

    private fun animateAlpha(item: BottomTabBarItem) {
        ValueAnimator.ofInt(item.alpha, 255).apply {
            duration = DEFAULT_ANIM_DURATION
            addUpdateListener {
                val value = it.animatedValue as Int
                item.alpha = value
                invalidate()
            }
            start()
        }
    }

    fun setOnItemSelectedListener(listener: (position: Int) -> Unit) {
        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int): Boolean {
                listener.invoke(pos)
                return true
            }
        }
    }

    fun setOnItemReselectedListener(listener: (position: Int) -> Unit) {
        onItemReselectedListener = object : OnItemReselectedListener {
            override fun onItemReselect(pos: Int) {
                listener.invoke(pos)
            }
        }
    }

    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_INDICATOR_COLOR = "#2DFFFFFF"

        private const val TOP_LEFT_CORNER = 1
        private const val TOP_RIGHT_CORNER = 2
        private const val BOTTOM_RIGHT_CORNER = 4
        private const val BOTTOM_LEFT_CORNER = 8
        private const val ALL_CORNERS = 15

        private const val DEFAULT_SIDE_MARGIN = 10f
        private const val DEFAULT_ITEM_PADDING = 10f
        private const val DEFAULT_ANIM_DURATION = 200L
        private const val DEFAULT_TEXT_SIZE = 16F
        private const val DEFAULT_INDICATOR_CORNER_RADIUS = 10F
        private const val DEFAULT_BAR_CORNER_RADIUS = 10F
        private const val DEFAULT_BAR_CORNERS = TOP_LEFT_CORNER or TOP_RIGHT_CORNER
    }
}