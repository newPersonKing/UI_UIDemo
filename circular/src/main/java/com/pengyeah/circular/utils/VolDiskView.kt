package com.pengyeah.circular.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt

/**
 *  Created by pengyeah on 2020/9/16
 *  佛祖开光，永无bug
 *  God bless U
 */
class VolDiskView : View {

    final val TAG: String = VolDiskView::class.java.simpleName

    var paint: Paint = Paint()

    @ColorInt
    var color: Int = Color.RED

    /**
     * 旋钮半径
     */
    var radius: Float = 0F

    /**
     * 刻度线长度
     */
    var scaleWidth: Float = 0F

    /**
     * 控件中心点
     */
    var centerX: Float = 0F
    var centerY: Float = 0F

    /**
     * 圆盘类控件辅助工具
     */
    var circularOpUtils: CircularOpUtils = CircularOpUtils()

    /**
     * 手指按下的点的坐标
     */
    var downX: Float = 0F
    var downY: Float = 0F

    /**
     * 当前选中刻度宽度
     */
    var curSelScaleWith: Float = 0F

    /**
     * 当前选中刻度颜色
     */
    @ColorInt
    var curSelScaleColor: Int = Color.RED

    /**
     * 刻度间的夹角
     */
    var scaleSpace: Float = 10F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = color
//        paint.strokeWidth = 10F

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        scaleWidth = width / 12F
        curSelScaleWith = width / 12F
        radius = (width - 3 * scaleWidth) / 2
        centerX = width / 2F
        centerY = height / 2F
        circularOpUtils.centerX = centerX
        circularOpUtils.centerY = centerY
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        canvas?.let {
            drawScale(it)
            drawShadow(it)
            drawIndicator(it)
        }
    }

    private fun drawScale(canvas: Canvas) {
        Log.i(TAG, "curDegree==>" + circularOpUtils.curDegree)
        canvas.save()
        paint.color = Color.GRAY
        var scaleCount = 360 / scaleSpace.toInt()
        for (i in 0 until scaleCount) {
            //绘制当前指示
            if ((i * scaleSpace <= circularOpUtils.curDegree) && ((i + 1) * scaleSpace > circularOpUtils.curDegree)) {
                Log.i(TAG, "i*scaleSpace==>" + i * scaleSpace)
                paint.color = curSelScaleColor
                canvas.drawRect(width - scaleWidth, centerY - 4F, width - scaleWidth + curSelScaleWith, centerY + 4F, paint)
                paint.color = Color.GRAY
            } else {
                canvas.drawRect(width - scaleWidth, centerY - 4F, width.toFloat(), centerY + 4F, paint)
            }
            canvas.rotate(scaleSpace, centerX, centerY)

        }
        canvas.restore()
    }

    private fun drawIndicator(canvas: Canvas) {
        canvas.save()
        canvas.rotate(circularOpUtils.curDegree, centerX, centerY)
        paint.color = Color.RED
        canvas.drawRect(RectF(centerX + radius / 4, centerY - 4, centerX + radius * 3 / 4, centerY + 4), paint)
        canvas.restore()
    }

    private fun drawShadow(canvas: Canvas) {
        paint.color = Color.WHITE
        paint.setShadowLayer(shadowSize, 0F, 15F, Color.GRAY)
        canvas.drawCircle(centerX, centerY, radius, paint)
        paint.clearShadowLayer()
    }

    var shrinkScaleAnim: ValueAnimator? = null
    var expandScaleAnim: ValueAnimator? = null

    var shadowAnim: ValueAnimator? = null
    var shadowSize: Float = 30F

    private fun startDownShadowAnim() {
        shadowAnim?.cancel()
        shadowAnim = ValueAnimator.ofFloat(shadowSize, 20F)
        with(shadowAnim!!) {
            duration = 300L
            addUpdateListener {
                shadowSize = animatedValue as Float
                postInvalidate()
            }
            start()
        }
    }

    private fun startUpShadowAnim() {
        shadowAnim?.cancel()
        shadowAnim = ValueAnimator.ofFloat(shadowSize, 30F)
        with(shadowAnim!!) {
            duration = 300L
            addUpdateListener {
                shadowSize = animatedValue as Float
                postInvalidate()
            }
            start()
        }
    }

    private fun shrinkScaleAnim() {
        shrinkScaleAnim?.cancel()
        if (curSelScaleWith > 10F) {
            shrinkScaleAnim = ValueAnimator.ofFloat(curSelScaleWith, 10F)

            with(shrinkScaleAnim!!) {
                duration = 300L
                addUpdateListener {
                    curSelScaleWith = it.animatedValue as Float
                    postInvalidate()
                }
                start()
            }
        }
    }

    private fun expandScaleAnim() {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    circularOpUtils.resetPreDegree()
                    downX = 0F
                    downY = 0F
                    startUpShadowAnim()
                }
                MotionEvent.ACTION_MOVE -> {
                    circularOpUtils.computeCurAngle(downX, downY, it.x, it.y)
                    shrinkScaleAnim()
                }
                MotionEvent.ACTION_DOWN -> {
                    downX = it.x
                    downY = it.y
                    startDownShadowAnim()
                }
                else -> {
                }
            }
            postInvalidate()
        }
        return true
    }

}