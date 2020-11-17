package com.pengyeah.tear

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.pengyeah.tear.coordinate.Coordinate
import com.pengyeah.tear.func.CrimpSizeFunc
import com.pengyeah.tear.utils.BazierUtils

/**
 *  Created by pengyeah on 2020/10/16
 *  佛祖开光，永无bug
 *  God bless U
 */
class PaperLayout : RelativeLayout {

    val TAG: String = javaClass.simpleName

    /**
     * 纸张宽高
     */
    var paperWidth: Float = 0F
    var paperHeight: Float = 0F

    var mPaint: Paint = Paint()

    @ColorInt
    var paperColor: Int = Color.WHITE

    /**
     * 坐标点
     */
    var pointA: Coordinate = Coordinate()
    var pointB: Coordinate = Coordinate()
    var pointC: Coordinate = Coordinate()
    var pointD: Coordinate = Coordinate()
    var pointE: Coordinate = Coordinate()
    var pointF: Coordinate = Coordinate()
    var pointG: Coordinate = Coordinate()
    //outer状态下的顶点坐标
    var pointH: Coordinate = Coordinate()
    var pointI: Coordinate = Coordinate()

    /**
     * 内容路径
     */
    var contentPath: Path = Path()

    /**
     * 纸张卷角
     */
    var dogEaredPath: Path = Path()

    /**
     * 组合路径 = 内容路径+纸张卷角
     */
    var unionPath: Path = Path()

    var crimpSize: Float = 0F

    /**
     * 阴影颜色
     */
    @ColorInt
    var shadowColor: Int = Color.parseColor("#ffd5d5d5")

    /**
     * 卷角在内状态
     */
    val DRAW_STATE_INNER = 0x01

    /**
     * 卷角在外状态
     */
    val DRAW_STATE_OUTER = 0x02

    var drawState = DRAW_STATE_INNER

    /**
     * 各个变化函数
     */
    var crimpSizeFunc: CrimpSizeFunc? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        mPaint.color = paperColor
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL

        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paperWidth = w * 3 / 4F
        paperHeight = paperWidth

        configPoint(0F, paperHeight)
        combinePath()
        initFunc()
    }

    /**
     * 根据D点配置各个关键点坐标
     * @param dx D点x坐标
     * @param dy D点y坐标
     */
    private fun configPoint(dx: Float, dy: Float) {
        if (dx >= paperWidth) {
            drawState = DRAW_STATE_OUTER
        } else {
            drawState = DRAW_STATE_INNER
        }
        if (drawState == DRAW_STATE_INNER) {
            pointD.x = dx
            pointD.y = dy

            pointA.x = 0F
            pointA.y = pointD.y - crimpSize

            pointB.x = 0F
            pointB.y = pointD.y

            pointC.x = pointB.x + crimpSize
            pointC.y = pointD.y

            pointE.x = pointD.x
            pointE.y = paperHeight - crimpSize

            pointF.x = pointD.x
            pointF.y = paperHeight

            pointG.x = pointD.x + crimpSize
            pointG.y = paperHeight
        } else if (drawState == DRAW_STATE_OUTER) {
            pointD.x = dx
            pointD.y = dy

            pointH.x = pointD.x - paperWidth
            pointH.y = pointD.y

            pointI.x = pointD.x
            pointI.y = pointD.y + paperHeight

            pointA.x = pointD.x - paperWidth
            if (dy <= -paperHeight) {
                pointA.y = pointD.y - paperHeight
            } else {
                pointA.y = -crimpSize
            }

            pointB.x = pointA.x
            if (dy <= -paperHeight) {
                pointB.y = pointD.y - paperHeight
            } else {
                pointB.y = 0F
            }

            pointC.x = pointB.x + crimpSize
            pointC.y = pointB.y

            if (dx >= 2 * paperWidth) {
                pointE.x = pointD.x - paperWidth
            } else {
                pointE.x = paperWidth
            }
            pointE.y = paperHeight + pointD.y - crimpSize

            pointF.x = pointE.x
            pointF.y = paperHeight + pointD.y

            pointG.x = pointF.x + crimpSize
            pointG.y = pointF.y
        }
    }

    private fun combinePath() {
        if (drawState == DRAW_STATE_INNER) {
            contentPath.reset()
            contentPath.moveTo(0F, 0F)
            contentPath.lineTo(pointA.x, pointA.y)
            contentPath.quadTo(pointB.x, pointB.y, pointC.x, pointC.y)
            contentPath.lineTo(pointD.x, pointD.y)
            contentPath.lineTo(pointE.x, pointE.y)
            contentPath.quadTo(pointF.x, pointF.y, pointG.x, pointG.y)
            contentPath.lineTo(paperWidth, paperHeight)
            contentPath.lineTo(paperWidth, 0F)
            contentPath.close()

            val pointb = BazierUtils.getBezierPoint(pointA, pointB, pointC, 0.5F)
            val pointf = BazierUtils.getBezierPoint(pointE, pointF, pointG, 0.5F)

            dogEaredPath.reset()
            dogEaredPath.moveTo(pointb.x, pointb.y)
            dogEaredPath.lineTo(pointD.x, pointD.y)
            dogEaredPath.lineTo(pointf.x, pointf.y)
            dogEaredPath.lineTo(pointb.x, pointb.y)
            dogEaredPath.close()

            unionPath.fillType = Path.FillType.WINDING
            unionPath.reset()
            unionPath.moveTo(0F, 0F)
            unionPath.lineTo(pointA.x, pointA.y)
            unionPath.quadTo(pointB.x, pointB.y, pointC.x, pointC.y)
            unionPath.lineTo(pointD.x, pointD.y)
            unionPath.lineTo(pointE.x, pointE.y)
            unionPath.quadTo(pointF.x, pointF.y, pointG.x, pointG.y)
            unionPath.lineTo(paperWidth, paperHeight)
            unionPath.lineTo(paperWidth, 0F)
            unionPath.lineTo(0F, 0F)
            unionPath.lineTo(pointb.x, pointb.y)
            unionPath.lineTo(pointf.x, pointf.y)
            unionPath.lineTo(paperWidth, paperHeight)
            unionPath.lineTo(paperWidth, 0F)
            unionPath.lineTo(0F, 0F)

            dogEaredPath.op(contentPath, Path.Op.DIFFERENCE)

            childContentPath.reset()
            childContentPath.op(contentPath, Path.Op.UNION)
            childContentPath.offset((width - paperWidth) / 2F, (height - paperHeight) / 2F)

        } else if (drawState == DRAW_STATE_OUTER) {

            val pointb = BazierUtils.getBezierPoint(pointA, pointB, pointC, 0.5F)
            val pointf = BazierUtils.getBezierPoint(pointE, pointF, pointG, 0.5F)

            unionPath.fillType = Path.FillType.WINDING
            unionPath.reset()
            unionPath.moveTo(pointD.x, pointD.y)
            unionPath.lineTo(pointH.x, pointH.y)
            unionPath.lineTo(pointA.x, pointA.y)
            unionPath.quadTo(pointB.x, pointB.y, pointC.x, pointC.y)
            unionPath.lineTo(pointE.x, pointE.y)
            unionPath.quadTo(pointF.x, pointF.y, pointG.x, pointG.y)
            unionPath.lineTo(pointI.x, pointI.y)
            unionPath.lineTo(pointD.x, pointD.y)
            unionPath.lineTo(pointb.x, pointb.y)
            unionPath.lineTo(pointf.x, pointf.y)
            unionPath.lineTo(pointD.x, pointD.y)

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        canvas?.let {
            drawPaper(it)
        }
    }

    private fun drawPaper(canvas: Canvas) {
        with(canvas) {
            when (drawState) {
                DRAW_STATE_INNER -> {
                    save()
                    translate((width - paperWidth) / 2F, (height - paperHeight) / 2F)

                    mPaint.shader = null
//                    mPaint.color = Color.RED
                    mPaint.setShadowLayer(50F, -10F, 10F, shadowColor)
                    drawPath(unionPath, mPaint)

                    mPaint.setShadowLayer(20F, 10F, -10F, shadowColor)
//                    mPaint.color = Color.BLUE
                    drawPath(dogEaredPath, mPaint)

                    mPaint.shader = LinearGradient(pointD.x / 2F, pointD.y + pointD.x / 2F, pointD.x * 3 / 4F, pointD.y + pointD.x / 4F, shadowColor, Color.WHITE, Shader.TileMode.CLAMP)
                    drawPath(dogEaredPath, mPaint)

                    restore()
                }
                DRAW_STATE_OUTER -> {
                    save()
                    translate((width - paperWidth) / 2F, (height - paperHeight) / 2F)
//                    mPaint.color = Color.BLUE
                    mPaint.shader = LinearGradient(pointD.x / 2F, pointD.y + pointD.x / 2F, pointD.x * 3 / 5F, pointD.y + pointD.x * 2 / 5F, shadowColor, Color.WHITE, Shader.TileMode.CLAMP)
                    drawPath(unionPath, mPaint)
                    mPaint.setShadowLayer(50F, -10F, 10F, shadowColor)
                    drawPath(unionPath, mPaint)
                    restore()
                }
                else -> {

                }
            }

        }
    }

    /**
     * 子View画布待裁减path
     */
    var childContentPath: Path = Path()

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        if (drawState == DRAW_STATE_INNER) {
            canvas?.save()
            canvas?.clipPath(childContentPath)

            val flag = super.drawChild(canvas, child, drawingTime)
            canvas?.restore()
            return flag
        } else {
            return true
        }
    }

    private fun initFunc() {
        crimpSizeFunc = CrimpSizeFunc()
        with(crimpSizeFunc!!) {
            outParamMax = 160F
            outParamMin = 0F

            inParamMax = paperWidth * 2
            inParamMin = 0F

            initValue = 0F
        }
    }

    private fun executeCrimpSizeFunc(offset: Float) {
        crimpSizeFunc?.let {
            crimpSize = it.execute(offset)
        }
    }

    var downX: Float = 0F
    var downY: Float = 0F
    var offset: Float = 0F
    var dStartX: Float = 0F
    var dStartY: Float = 0F

    /**
     * 是否可交互
     */
    var isCanTouch: Boolean = true

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isCanTouch == false) {
            return super.onTouchEvent(event)
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                dStartX = pointD.x
                dStartY = pointD.y
            }
            MotionEvent.ACTION_MOVE -> {
                offset = event.x - downX
                pointD.x = dStartX + offset
                pointD.y = dStartY - offset
                state = STATE_TEARING
                //边界控制
                if (pointD.x <= 0F) {
                    pointD.x = 0F
                    state = STATE_NORMAL
                }
                if (pointD.y >= paperHeight) {
                    pointD.y = paperHeight
                    state = STATE_NORMAL
                }
                executeCrimpSizeFunc(dStartX + offset)
                configPoint(pointD.x, pointD.y)
                combinePath()
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                downX = 0F
                downY = 0F
                offset = 0F
                if (pointD.x <= 0F) {
                    pointD.x = 0F
                    state = STATE_NORMAL
                }
                if (pointD.y >= paperHeight) {
                    pointD.y = paperHeight
                    state = STATE_NORMAL
                }
            }
            else -> {

            }
        }
        onTearStateChangeListener?.onTearStateChanged(state)
        return true
    }

    /**
     * 撕页动画
     */
    var tearAnim: ValueAnimator? = null

    /**
     * 开始便利贴撕页动画
     */
    fun startTearAnim() {
        tearAnim?.cancel()
        tearAnim = ValueAnimator.ofFloat(0F, paperWidth * 2.5F)
        with(tearAnim!!) {
            // 图个吉利
            duration = 888L
            interpolator = AccelerateInterpolator()
            dStartX = pointD.x
            dStartY = pointD.y
            addUpdateListener {
                offset = it.animatedValue as Float
                pointD.x = dStartX + offset
                pointD.y = dStartY - offset
                //边界控制
                if (pointD.x <= 0F) {
                    pointD.x = 0F
                }
                if (pointD.y >= paperHeight) {
                    pointD.y = paperHeight
                }
                executeCrimpSizeFunc(dStartX + offset)
                configPoint(pointD.x, pointD.y)
                combinePath()
                postInvalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    state = STATE_TEARED
                    onTearStateChangeListener?.onTearStateChanged(state)
                }
            })
            start()
        }
    }


    var state = STATE_NORMAL

    /**
     * 对外暴露三种状态：正常显示、正在撕、撕完了
     */
    var onTearStateChangeListener: OnTearStateChangeListener? = null

    open interface OnTearStateChangeListener {
        fun onTearStateChanged(tearState: Int)
    }
}