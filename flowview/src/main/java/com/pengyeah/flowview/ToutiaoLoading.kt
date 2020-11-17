package com.pengyeah.flowview

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable

class ToutiaoLoading : Drawable {

    lateinit var transAnimator1: ValueAnimator
    lateinit var transAnimator2: ValueAnimator
    var mPaint: Paint

    var offset1: Int = 0
    var offset2: Int = 0

    constructor() {
        mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10F
        mPaint.color = Color.RED
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }


    override fun draw(canvas: Canvas) {

        val path: Path = Path()
        path.moveTo(bounds.left.toFloat() + mPaint.strokeWidth, bounds.top.toFloat() + mPaint.strokeWidth + offset1 + bounds.height() / 10)
        path.lineTo(bounds.right.toFloat() - mPaint.strokeWidth, bounds.top.toFloat() + mPaint.strokeWidth + offset2)
        path.lineTo(bounds.right.toFloat() - mPaint.strokeWidth, bounds.bottom.toFloat() - mPaint.strokeWidth - offset2 - bounds.height() / 10)
        path.lineTo(bounds.left.toFloat() + mPaint.strokeWidth, bounds.bottom.toFloat() - mPaint.strokeWidth - offset1)
        path.close()
        canvas.drawPath(path, mPaint)
    }

    fun start(): ToutiaoLoading {
        transAnimator1 = ValueAnimator.ofInt(0, 0, bounds.height() * 9 / 10, 0)
        transAnimator1.duration = 1000L
        transAnimator1.addUpdateListener {
            offset1 = it.animatedValue as Int
            invalidateSelf()
        }
        transAnimator1.repeatCount = -1
        transAnimator1.start()

        transAnimator2 = ValueAnimator.ofInt(0, bounds.height() * 9 / 10, 0, 0)
        transAnimator2.duration = 1000L
        transAnimator2.addUpdateListener {
            offset2 = it.animatedValue as Int
            invalidateSelf()
        }
        transAnimator2.repeatCount = -1
        transAnimator2.start()
        return this
    }

}