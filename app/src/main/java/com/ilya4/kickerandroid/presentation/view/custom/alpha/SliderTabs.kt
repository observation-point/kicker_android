package com.ilya4.kickerandroid.presentation.view.custom.alpha

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.ilya4.kickerandroid.R

class SliderTabs : View {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let { consumeAttributeSet(context, it)}
    }

    @ColorInt
    private var _backgroundColor = Color.parseColor(DEFAULT_BG_COLOR_HEX)
    @ColorInt
    private var sliderColor = Color.parseColor(DEFAULT_SLIDER_BG_COLOR_HEX)
    @ColorInt
    private var backgroundColorPress = Color.parseColor(DEFAULT_BG_PRESSED_COLOR_HEX)
    @ColorInt
    private var tabTextColor = Color.parseColor(DEFAULT_TAB_TEXT_COLOR_HEX)

    private var leftTabText = context.getString(R.string.st_lefttab_default_text)
    private var rightTabText = context.getString(R.string.st_righttab_default_text)

    private val backgroundRectF = RectF()
    private val sliderRectF = RectF()

    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var backgroundRectRadius = 0.0f //закругления
    private var sliderRectRadius = 0.0f

    private var sliderRectInset = 0.0f // расстояние между табом и бэкграундом.
    private var sliderRectXOffset = 0.0f // смещение???
    private var sliderState = SliderState.IDLE_LEFT
    private var isViewPressed = false

    private val sliderValueAnimator = ValueAnimator()

    private fun consumeAttributeSet(context: Context, attrs: AttributeSet) {
        val typedArray = context
            .theme
            .obtainStyledAttributes(attrs, R.styleable.SliderTabs, 0, 0)
        try {
            _backgroundColor = typedArray.getColor(
                R.styleable.SliderTabs_st_backgroundColor,
                Color.parseColor(DEFAULT_BG_COLOR_HEX)
            )

            sliderColor = typedArray.getColor(
                R.styleable.SliderTabs_st_sliderColor,
                Color.parseColor(DEFAULT_SLIDER_BG_COLOR_HEX)
            )
            leftTabText = resolveLeftTabText(context, typedArray)
            rightTabText = resolveRightTabText(context, typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun resolveLeftTabText(context: Context, typedArray: TypedArray) : String {
        val leftTabText = typedArray.getString(R.styleable.SliderTabs_st_leftTabText)
        return leftTabText ?: context.getString(R.string.st_lefttab_default_text)
    }

    private fun resolveRightTabText(context: Context, typedArray: TypedArray) : String {
        val rightTabText = typedArray.getString(R.styleable.SliderTabs_st_rightTabText)
        return rightTabText ?: context.getString(R.string.st_righttab_default_text)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = calculateDesiredWidth()
        val desiredHeight = calculateDesiredHeight()
        val measuredWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun calculateDesiredWidth(): Int {
        return suggestedMinimumWidth + paddingLeft + paddingRight
    }

    private fun calculateDesiredHeight(): Int {
        return suggestedMinimumHeight + paddingTop + paddingBottom
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (isViewVisible(w, h))
            calculateRectangles()
    }

    private fun calculateRectangles() {
        calculateBackgroundRectF()
        calculateSliderRectF()
    }

    private fun isViewVisible(w: Int, h: Int): Boolean {
        return w > 0 && h > 0
    }

    private fun calculateBackgroundRectF() {
        backgroundRectF.left = BACKGROUND_RECT_LEFT
        backgroundRectF.top = BACKGROUND_RECT_TOP
        backgroundRectF.right = width.toFloat()
        backgroundRectF.bottom = height.toFloat()
    }

    private fun calculateSliderRectF() {
        sliderRectF.left = calculateSliderRectLeft()
        sliderRectF.top = calculateSliderRectTop()
        sliderRectF.right = calculateSliderRectRight()
        sliderRectF.bottom = calculateSliderRectBottom()
    }

    private fun calculateSliderRectLeft(): Float {
        return SLIDER_RECT_LEFT + sliderRectInset + sliderRectXOffset
    }

    private fun calculateSliderRectTop(): Float {
        return SLIDER_RECT_TOP + sliderRectInset
    }

    private fun calculateSliderRectRight(): Float {
        return  width / TABS_COUNT - sliderRectInset + sliderRectXOffset
    }

    private fun calculateSliderRectBottom(): Float {
        return height - sliderRectInset
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawSlider(canvas)
        drawText(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        rectPaint.color = _backgroundColor
        canvas.drawRoundRect(backgroundRectF, backgroundRectRadius, backgroundRectRadius, rectPaint)
    }

    private fun drawSlider(canvas: Canvas) {
        rectPaint.color = sliderColor
        recalculateSliderRectSides()
        canvas.drawRoundRect(sliderRectF, sliderRectRadius, sliderRectRadius, rectPaint)
    }

    private fun recalculateSliderRectSides() {
        sliderRectF.left = calculateSliderRectLeft()
        sliderRectF.right = calculateSliderRectRight()
    }

    private fun drawText(canvas: Canvas) {
        drawLeftTabText(canvas)
        drawRightTabText(canvas)
    }

    private fun drawLeftTabText(canvas: Canvas) {
        val textWidth = textPaint.measureText(leftTabText)
        canvas
            .drawText(
                leftTabText,
                calculateTabTextX(LEFT_TAB_TEXT_CX, textWidth),
                calculateTabTextY(),
                textPaint
            )
    }

    private fun drawRightTabText(canvas: Canvas) {
        val textWidth = textPaint.measureText(rightTabText)
        canvas
            .drawText(
                rightTabText,
                calculateTabTextX(RIGHT_TAB_TEXT_CX, textWidth),
                calculateTabTextY(),
                textPaint
            )
    }

    private fun calculateTabTextX(textCX: Float, textWidth: Float): Float {
        return width * textCX - textWidth / 2.0f
    }

    private fun calculateTabTextY(): Float {
        return height / 2 - (textPaint.descent() - textPaint.ascent()) / 2
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var isEventHandled = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> isEventHandled = handleActionDownEvent(event.x)

            MotionEvent.ACTION_UP -> {
                onViewReleased()
                performClick()
                isEventHandled = true
            }

            MotionEvent.ACTION_CANCEL -> {
                onViewReleased()
                isEventHandled = true
            }
        }
        return isEventHandled
    }

    private fun handleActionDownEvent(x: Float): Boolean {
        return if (isClickableAreaPressed(x)) {
            isViewPressed = true
            invalidate()
            true
        } else
            false
    }

    private fun isClickableAreaPressed(x: Float): Boolean {
        return when (sliderState) {

            SliderState.IDLE_LEFT -> x > width / 2

            SliderState.IDLE_RIGHT -> x < width / 2

            SliderState.MOVING_LEFT -> x > width / 2

            SliderState.MOVING_RIGHT -> x < width / 2
        }
    }

    private fun onViewReleased() {
        isViewPressed = false
        invalidate()
    }

    override fun performClick(): Boolean {
        super.performClick()
        val newState = resolveNewSliderState()
        changeSliderState(newState)
        return true
    }

    private fun resolveNewSliderState(): SliderState {
        return when (sliderState) {

            SliderState.IDLE_LEFT -> SliderState.MOVING_RIGHT

            SliderState.IDLE_RIGHT -> SliderState.MOVING_LEFT

            SliderState.MOVING_RIGHT -> SliderState.MOVING_LEFT

            SliderState.MOVING_LEFT -> SliderState.MOVING_RIGHT
        }
    }

    private fun changeSliderState(newState: SliderState) {
        when (newState) {

            SliderState.IDLE_LEFT -> {
                sliderState = newState
                sliderRectXOffset = 0.0f
            }

            SliderState.IDLE_RIGHT -> {
                sliderState = newState
                sliderRectXOffset = width / 2.0f
            }
        }
        invalidate()
    }

    enum class SliderState {

        IDLE_LEFT,
        IDLE_RIGHT,
        MOVING_RIGHT,
        MOVING_LEFT
    }


    companion object {

        private const val TABS_COUNT = 2

        private const val DEFAULT_BG_COLOR_HEX = "#EAE9F0"
        private const val DEFAULT_SLIDER_BG_COLOR_HEX = "#FFFFFF"
        private const val DEFAULT_BG_PRESSED_COLOR_HEX = "#D4D0D9"
        private const val DEFAULT_TAB_TEXT_COLOR_HEX = "#5F5869"

        private const val BACKGROUND_RECT_LEFT = 0.0f
        private const val BACKGROUND_RECT_TOP = 0.0f

        private const val SLIDER_RECT_LEFT = 0.0f
        private const val SLIDER_RECT_TOP = 0.0f

        private const val LEFT_TAB_TEXT_CX = 0.25f
        private const val RIGHT_TAB_TEXT_CX = 0.75f


    }
}