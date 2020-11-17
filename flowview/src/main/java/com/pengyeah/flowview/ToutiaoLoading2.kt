package com.pengyeah.flowview

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import com.pengyeah.flowview.coordinate.Coordinate
import com.pengyeah.flowview.func.Func1
import com.pengyeah.flowview.func.Func2
import com.pengyeah.flowview.func.Func3
import com.pengyeah.flowview.func.Func4

class ToutiaoLoading2 : Drawable {

    val pointA: Coordinate = Coordinate()

    val pointB: Coordinate = Coordinate()

    val pointC: Coordinate = Coordinate()

    val pointD: Coordinate = Coordinate()

    val mPath: Path = Path()

    val mPaint: Paint = Paint()

    constructor() {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.RED
        mPaint.strokeWidth = 10F
    }

    override fun draw(canvas: Canvas) {

        mPath.reset()
        mPath.moveTo(pointA.x, pointA.y)
        mPath.lineTo(pointB.x, pointB.y)
        mPath.lineTo(pointC.x, pointC.y)
        mPath.lineTo(pointD.x, pointD.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        //初始化A、B、C、D
        pointA.x = 2 * mPaint.strokeWidth
        pointA.y = 2 * mPaint.strokeWidth

        pointB.x = bounds.right - 2 * mPaint.strokeWidth
        pointB.y = bounds.top.toFloat() + mPaint.strokeWidth

        pointC.x = bounds.right - 2 * mPaint.strokeWidth
        pointC.y = bounds.bottom - 3 * mPaint.strokeWidth

        pointD.x = 2 * mPaint.strokeWidth
        pointD.y = bounds.bottom - 2 * mPaint.strokeWidth

        //初始化变化函数
        configFunc()
    }

    fun configABaABaFunc() {
        pointA.yFunc = Func1(pointA.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointB.yFunc = Func1(pointB.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointC.yFunc = Func3(pointC.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointD.yFunc = Func3(pointD.y, bounds.height() - 4 * mPaint.strokeWidth)
    }

    fun configToutiaoFunc() {
//        pointA.xFunc = Func1(pointA.x, bounds.height() - 4 * mPaint.strokeWidth)
//        pointB.xFunc = Func2(pointB.x, bounds.height() - 4 * mPaint.strokeWidth)
//        pointC.xFunc = Func3(pointC.x, bounds.height() - 4 * mPaint.strokeWidth)
//        pointD.xFunc = Func4(pointD.x, bounds.height() - 4 * mPaint.strokeWidth)

        pointA.yFunc = Func1(pointA.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointB.yFunc = Func2(pointB.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointC.yFunc = Func3(pointC.y, bounds.height() - 4 * mPaint.strokeWidth)
        pointD.yFunc = Func4(pointD.y, bounds.height() - 4 * mPaint.strokeWidth)
    }

    fun configFunc() {
        configToutiaoFunc()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    fun transform(offset: Float) {
        pointA.xFunc?.let {
            pointA.x = it.execute(offset.toFloat())
        }
        pointB.xFunc?.let {
            pointB.x = it.execute(offset.toFloat())
        }
        pointC.xFunc?.let {
            pointC.x = it.execute(offset.toFloat())
        }
        pointD.xFunc?.let {
            pointD.x = it.execute(offset.toFloat())
        }

        pointA.yFunc?.let {
            pointA.y = it.execute(offset.toFloat())
        }
        pointB.yFunc?.let {
            pointB.y = it.execute(offset.toFloat())
        }
        pointC.yFunc?.let {
            pointC.y = it.execute(offset.toFloat())
        }
        pointD.yFunc?.let {
            pointD.y = it.execute(offset.toFloat())
        }
        invalidateSelf()
    }

    var animator: ValueAnimator? = null

    fun startAnim() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0F, bounds.height() - 20F)
        animator?.duration = 800L
        animator?.addUpdateListener {
            val offset: Float = it.animatedValue as Float
            transform(offset)
        }
        animator?.reverse()
        animator?.repeatCount = -1
        animator?.start()
    }
}