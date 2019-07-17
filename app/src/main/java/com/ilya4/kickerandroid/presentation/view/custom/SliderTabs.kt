/*
 * Copyright (C) 2018 Vitaly Sulimov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilya4.kickerandroid.presentation.view.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import com.ilya4.kickerandroid.R


/**
 * A SliderTabs is a two-state tab widget that can select between two
 * options. The user can click on the left / right sides to move tab to the
 * left or to the right respectively. You can call [selectLeftTab]
 * and [selectRightTab] to manually set tab position (this action
 * will be performed without animation). To listen for tab position changes
 * you can set the listener by calling [setOnTabSelectedListener].
 */
class SliderTabs : View {

    /**
     * Separate Paint objects for rectangles and text drawing.
     */
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Four float coordinates rectangles for widget background.
     * and slider
     */
    private val backgroundRectF = RectF()
    private val sliderRectF = RectF()

    /**
     * Value animator for tab slider toggle animation.
     */
    private val sliderValueAnimator = ValueAnimator()

    /**
     * Resolved widget colors initialized with default values.
     */
    @ColorInt
    private var _backgroundColor = Color.parseColor(DEFAULT_BG_COLOR_HEX)
    @ColorInt
    private var backgroundColorPressed = Color.parseColor(DEFAULT_BG_PRESSED_COLOR_HEX)
    @ColorInt
    private var sliderColor = Color.parseColor(DEFAULT_SLIDER_COLOR_HEX)
    @ColorInt
    private var tabTextColor = Color.parseColor(DEFAULT_TAB_TEXT_COLOR_HEX)

    /**
     * Tab texts initialized with default values.
     */
    private var leftTabText = context.getString(R.string.st_lefttab_default_text)
    private var rightTabText = context.getString(R.string.st_righttab_default_text)

    /**
     * Resolved animation duration initialized with the default value.
     */
    private var animationDuration = DEFAULT_ANIMATION_DURATION_MS

    /**
     * Density dependent values. Will be calculated during
     * [onMeasure] pass.
     */
    private var backgroundRectRadius = 0.0f
    private var sliderRectInset = 0.0f
    private var sliderRectRadius = 0.0f
    private var tabTextSize = 0.0f

    /**
     * Mutable properties which represents current view state.
     */
    private var sliderXOffset = 0.0f
    private var isViewPressed = false
    private var sliderState = SliderState.IDLE_LEFT

    private var tabSelectedListener: OnTabSelectedListener? = null

    constructor(context: Context) : super(context) {
        performViewInitialization(resources.displayMetrics)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let { consumeAttributeSet(context, it) }
        performViewInitialization(resources.displayMetrics)
    }

    private fun consumeAttributeSet(context: Context, attrs: AttributeSet) {
        val typedArray = context
            .theme
            .obtainStyledAttributes(attrs, R.styleable.SliderTabs, DEFAULT_STYLE_ATTRIBUTE, DEFAULT_STYLE_RESOURCE)
        try {
            _backgroundColor = typedArray.getColor(
                R.styleable.SliderTabs_st_backgroundColor,
                Color.parseColor(DEFAULT_BG_COLOR_HEX)
            )
            backgroundColorPressed = typedArray.getColor(
                R.styleable.SliderTabs_st_backgroundColorPressed,
                Color.parseColor(DEFAULT_BG_PRESSED_COLOR_HEX)
            )
            sliderColor = typedArray.getColor(
                R.styleable.SliderTabs_st_sliderColor,
                Color.parseColor(DEFAULT_SLIDER_COLOR_HEX)
            )
            tabTextColor = typedArray.getColor(
                R.styleable.SliderTabs_st_tabTextColor,
                Color.parseColor(DEFAULT_TAB_TEXT_COLOR_HEX)
            )
            leftTabText = resolveLeftTabText(context, typedArray)
            rightTabText = resolveRightTabText(context, typedArray)
            animationDuration = typedArray.getInt(
                R.styleable.SliderTabs_st_animationDuration,
                DEFAULT_ANIMATION_DURATION_MS
            )
        } finally {
            typedArray.recycle()
        }
    }

    private fun resolveLeftTabText(context: Context, typedArray: TypedArray): String {
        val leftTabText = typedArray.getString(R.styleable.SliderTabs_st_leftTabText)
        return leftTabText ?: context.getString(R.string.st_lefttab_default_text)
    }

    private fun resolveRightTabText(context: Context, typedArray: TypedArray): String {
        val rightTabText = typedArray.getString(R.styleable.SliderTabs_st_rightTabText)
        return rightTabText ?: context.getString(R.string.st_righttab_default_text)
    }

    private fun performViewInitialization(displayMetrics: DisplayMetrics) {
        calculateDensityDependentValues(displayMetrics)
        configureTextPaint()
        configureValueAnimator()
    }

    private fun calculateDensityDependentValues(displayMetrics: DisplayMetrics) {
        backgroundRectRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            BACKGROUND_RECT_RADIUS_DP,
            displayMetrics
        )
        sliderRectInset = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            SLIDER_RECT_INSET_DP,
            displayMetrics
        )
        sliderRectRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            SLIDER_RECT_RADIUS_DP,
            displayMetrics
        )
        tabTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            TAB_TEXT_SIZE_SP,
            displayMetrics
        )
    }

    private fun configureTextPaint() {
        textPaint.color = tabTextColor
        textPaint.textSize = tabTextSize
    }

    private fun configureValueAnimator() {
        sliderValueAnimator.duration = animationDuration.toLong()
        sliderValueAnimator.interpolator = AccelerateDecelerateInterpolator()
        sliderValueAnimator.addUpdateListener { animation ->
            sliderXOffset = animation.animatedValue as Float
            invalidate()
        }
        sliderValueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                sliderState = when (sliderState) {
                    SliderState.MOVING_RIGHT -> SliderState.IDLE_RIGHT
                    SliderState.MOVING_LEFT -> SliderState.IDLE_LEFT
                    else -> throw IllegalStateException("Animation finished while slider in idle state")
                }
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (isViewVisible(w, h)) {
            calculateRectangles()
            recalculateSliderXOffset()
        }
    }

    private fun isViewVisible(w: Int, h: Int): Boolean {
        return w > 0 && h > 0
    }

    private fun calculateRectangles() {
        calculateBackgroundRectF()
        calculateSliderRectF()
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
        return SLIDER_RECT_LEFT + sliderRectInset + sliderXOffset
    }

    private fun calculateSliderRectTop(): Float {
        return SLIDER_RECT_TOP + sliderRectInset
    }

    private fun calculateSliderRectRight(): Float {
        return width / TABS_COUNT - sliderRectInset + sliderXOffset
    }

    private fun calculateSliderRectBottom(): Float {
        return height - sliderRectInset
    }

    private fun recalculateSliderXOffset() {
        if (sliderState === SliderState.IDLE_LEFT) {
            sliderXOffset = 0.0f
        } else if (sliderState === SliderState.IDLE_RIGHT) {
            sliderXOffset = width / 2.0f
        }
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

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawSlider(canvas)
        drawText(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val backgroundColor = resolveBackgroundColor()
        rectPaint.color = backgroundColor
        canvas.drawRoundRect(backgroundRectF, backgroundRectRadius, backgroundRectRadius, rectPaint)
    }

    @ColorInt
    private fun resolveBackgroundColor(): Int {
        return if (isViewPressed) {
            backgroundColorPressed
        } else {
            _backgroundColor
        }
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
        canvas.drawText(
            leftTabText,
            calculateTabTextX(LEFT_TAB_TEXT_CX, textWidth),
            calculateTabTextY(),
            textPaint
        )
    }

    private fun drawRightTabText(canvas: Canvas) {
        val textWidth = textPaint.measureText(rightTabText)
        canvas.drawText(
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
        return height / 2 - (textPaint.descent() + textPaint.ascent()) / 2
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
        } else {
            false
        }
    }

    private fun isClickableAreaPressed(x: Float): Boolean {
        return when (sliderState) {

            SliderState.IDLE_LEFT -> x > width / 2

            SliderState.IDLE_RIGHT -> x < width / 2

            SliderState.MOVING_RIGHT -> x < width / 2

            SliderState.MOVING_LEFT -> x > width / 2
        }
    }

    private fun onViewReleased() {
        isViewPressed = false
        invalidate()
    }

    override fun performClick(): Boolean {
        super.performClick()
        val newState = resolveNewSliderState()
        changeStateAnimated(newState)
        return true
    }

    private fun resolveNewSliderState(): SliderState {
        return when (sliderState) {

            SliderState.IDLE_LEFT -> SliderState.MOVING_RIGHT

            SliderState.IDLE_RIGHT -> SliderState.MOVING_LEFT

            SliderState.MOVING_LEFT -> SliderState.MOVING_RIGHT

            SliderState.MOVING_RIGHT -> SliderState.MOVING_LEFT
        }
    }

    private fun changeStateAnimated(newState: SliderState) {
        when (newState) {

            SliderState.MOVING_RIGHT -> {
                sliderState = newState
                sliderValueAnimator.setFloatValues(sliderXOffset, width / 2.0f)
                notifyRightTabSelected()
            }

            SliderState.MOVING_LEFT -> {
                sliderState = newState
                sliderValueAnimator.setFloatValues(sliderXOffset, 0.0f)
                notifyLeftTabSelected()
            }

            else -> throw IllegalArgumentException(
                "Cannot perform animated transition " +
                        "to the given state. " +
                        "Expected enum value MOVING_RIGHT or MOVING_LEFT, but received $newState"
            )
        }
        sliderValueAnimator.start()
    }

    private fun notifyRightTabSelected() {
        tabSelectedListener?.onRightTabSelected()
    }

    private fun notifyLeftTabSelected() {
        tabSelectedListener?.onLeftTabSelected()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.sliderState = resolveSliderStateForSave()
        return savedState
    }

    private fun resolveSliderStateForSave(): SliderState {
        return when (sliderState) {

            SliderState.IDLE_LEFT -> SliderState.IDLE_LEFT

            SliderState.IDLE_RIGHT -> SliderState.IDLE_RIGHT

            SliderState.MOVING_RIGHT -> SliderState.IDLE_RIGHT

            SliderState.MOVING_LEFT -> SliderState.IDLE_LEFT
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        sliderState = state.sliderState
    }

    /**
     * Register a callback to be invoked when tab position changed.
     *
     * @param tabSelectedListener [OnTabSelectedListener] implementation
     */
    fun setOnTabSelectedListener(tabSelectedListener: OnTabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener
    }

    /**
     * Force select left tab. This action will be performed without animation and
     * listener registered through [setOnTabSelectedListener]
     * will not be notified.
     */
    fun selectLeftTab() {
        sliderState = SliderState.IDLE_LEFT
        sliderXOffset = 0.0f
        invalidate()
    }

    /**
     * Force select right tab. This action will be performed without animation and
     * listener registered through [setOnTabSelectedListener]
     * will not be notified.
     */
    fun selectRightTab() {
        sliderState = SliderState.IDLE_RIGHT
        sliderXOffset = width / 2.0f
        invalidate()
    }

    @Suppress("unused")
    internal class SavedState : View.BaseSavedState {

        var sliderState: SliderState = SliderState.IDLE_LEFT

        constructor(source: Parcel?) : super(source) {
            sliderState = source?.readSerializable() as SliderState
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeSerializable(sliderState)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    enum class SliderState {

        IDLE_LEFT,
        IDLE_RIGHT,
        MOVING_RIGHT,
        MOVING_LEFT
    }

    /**
     * Interface definition for a callback to be invoked when the tab position
     * of a slider changed.
     */
    interface OnTabSelectedListener {

        /**
         * Called when the left tab was selected
         */
        fun onLeftTabSelected()

        /**
         * Called when the right tab was selected
         */
        fun onRightTabSelected()
    }

    companion object {

        /**
         * Default style attributes for obtaining TypedArray from AttributeSet.
         */
        private const val DEFAULT_STYLE_ATTRIBUTE = 0
        private const val DEFAULT_STYLE_RESOURCE = 0

        /**
         * Only two tabs allowed
         */
        private const val TABS_COUNT = 2

        /**
         * Default color HEX values for this widget.
         * This values can be overridden through XML attributes.
         */
        private const val DEFAULT_BG_COLOR_HEX = "#EAE9F0"
        private const val DEFAULT_BG_PRESSED_COLOR_HEX = "#D4D0D9"
        private const val DEFAULT_SLIDER_COLOR_HEX = "#FFFFFF"
        private const val DEFAULT_TAB_TEXT_COLOR_HEX = "#5F5869"

        /**
         * Constant configuration values for background.
         */
        private const val BACKGROUND_RECT_LEFT = 0.0f
        private const val BACKGROUND_RECT_TOP = 0.0f
        private const val BACKGROUND_RECT_RADIUS_DP = 24.0f

        /**
         * Constant configuration values for slider.
         */
        private const val SLIDER_RECT_LEFT = 0.0f
        private const val SLIDER_RECT_TOP = 0.0f
        private const val SLIDER_RECT_INSET_DP = 2.0f
        private const val SLIDER_RECT_RADIUS_DP = 18.0f

        /**
         * Constant configuration values for text.
         */
        private const val TAB_TEXT_SIZE_SP = 14.0f
        private const val LEFT_TAB_TEXT_CX = 0.25f
        private const val RIGHT_TAB_TEXT_CX = 0.75f

        /**
         * Default animation duration for tab toggling action.
         * This value can be overridden through XML attribute.
         */
        private const val DEFAULT_ANIMATION_DURATION_MS = 300
    }
}
