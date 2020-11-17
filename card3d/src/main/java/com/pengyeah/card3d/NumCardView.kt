package com.pengyeah.card3d

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntRange
import com.pengyeah.card3d.func.CardRotateFunc
import com.pengyeah.card3d.func.CardShadowDistanceFunc
import com.pengyeah.card3d.func.CardShadowSizeFunc
import com.pengyeah.flowview.func.IFunc
import kotlin.math.abs

/**
 *  Created by pengyeah on 2020/10/9
 *  佛祖开光，永无bug
 *  God bless U
 */
class NumCardView : View {

    /**
     * 3D摄像头
     */
    var mCamera: Camera = Camera()

    var mPaint: Paint = Paint()
    var mMatrix: Matrix = Matrix()

    /**
     * 3D位置参数
     */
    private var depthZ: Float = 0F
    private var rotateX: Float = 0F
    private var rotateY: Float = 0F

    /**
     * 数字图片列表，0～9
     */
    private var numBms = ArrayList<Bitmap>()
    private var numBmIds = arrayOf(
            R.drawable.num0, R.drawable.num1, R.drawable.num2,
            R.drawable.num3, R.drawable.num4, R.drawable.num5,
            R.drawable.num6, R.drawable.num7, R.drawable.num8,
            R.drawable.num9
    )

    /*
    * 当前显示数字
     */
    @IntRange(from = 0, to = 9)
    var curShowNum: Int = 0

    /**
     * padding大小，为阴影绘制留出空间
     */
    var paddingSize: Float = 250F

    /**
     * 每片card的宽高
     */
    private var cardWidth: Float = 0F
    private var cardHeight: Float = 0F

    /**
     *  活动卡片的阴影大小和距离
     */
    private var cardShadowSize: Float = 10F
    private var cardShadowDistance: Float = 10F

    /**
     * 是否需要绘制上中下卡片
     */
    private var isNeedDrawUpCard = true
    private var isNeedDrawMidCard = true
    private var isNeedDrawDownCard = true

    /**
     * Card翻转函数
     */
    var cardRotateFunc: IFunc? = null

    /**
     * 阴影大小变化函数
     */
    var cardShadowSizeFunc: IFunc? = null

    /**
     * 阴影距离变化函数
     */
    var cardShadowDistanceFunc: IFunc? = null

    /**
     * 控件状态定义
     */
    //上翻中
    val STATE_UP_ING = 0x02
    //下翻中
    val STATE_DOWN_ING = 0x03
    //常规显示状态
    val STATE_NORMAL = 0x04

    var curState: Int = STATE_NORMAL

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED

        initNumBms()
    }

    private fun initNumBms() {
        for (i in 0..9) {
            numBms.add(BitmapFactory.decodeResource(resources, numBmIds[i]))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //设置card 大小
        cardWidth = width - paddingSize * 2
        cardHeight = height / 2 - paddingSize

        configFunc()
    }

    /**
     * 配置各个函数
     */
    private fun configFunc() {
        cardRotateFunc = CardRotateFunc()
        with(cardRotateFunc!!) {
            inParamMin = 0F
            inParamMax = cardHeight * 2

            outParamMin = 0F
            outParamMax = 180F

            initValue = 45F
        }

        cardShadowSizeFunc = CardShadowSizeFunc()
        with(cardShadowSizeFunc!!) {
            inParamMin = 0F
            inParamMax = 180F

            outParamMax = 50F
            outParamMin = 0F

            initValue = 10F
        }

        cardShadowDistanceFunc = CardShadowDistanceFunc()
        with(cardShadowDistanceFunc!!) {
            inParamMin = 0F
            inParamMax = 180F

            outParamMax = 50F
            outParamMin = 0F

            initValue = 10F
        }
    }

    /**
     * 计算交互过程中Card对应的角度
     */
    private fun executeFunc(offset: Float) {
        cardRotateFunc?.let {
            val rate = (it.outParamMin - it.outParamMax) / (it.inParamMax - it.inParamMin)
            val initH = ((it.outParamMin - it.outParamMax) + it.initValue) / rate
            rotateX = it.execute(initH + offset)
        }

        executeShadowFunc(rotateX)
    }

    /**
     * 根据旋转角度计算阴影大小、距离
     */
    private fun executeShadowFunc(rotate: Float) {
        cardShadowSizeFunc?.let {
            cardShadowSize = it.execute(rotate)
        }

        cardShadowDistanceFunc?.let {
            cardShadowDistance = it.execute(rotate)
        }
    }

    /**
     * 重置各个初始值
     */
    private fun resetInitValue() {
        cardRotateFunc?.let {
            it.initValue = rotateX
        }

        cardShadowSizeFunc?.let {
            it.initValue = cardShadowSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //判断状态，不同状态绘制不同内容
        judgeState(curState)

        canvas?.let {
            drawUpCard(it)
            drawDownCard(it)

            // 中间活动card最后绘制
            drawMidCard(it)
        }
    }

    private fun judgeState(state: Int) {
        when (state) {
            STATE_NORMAL -> {
                isNeedDrawMidCard = false
                isNeedDrawUpCard = true
                isNeedDrawDownCard = true
            }
            STATE_UP_ING -> {
                isNeedDrawMidCard = true
                if (curShowNum + 1 > 9) {
                    isNeedDrawDownCard = false
                }
            }
            STATE_DOWN_ING -> {
                isNeedDrawMidCard = true
                if (curShowNum - 1 < 0) {
                    isNeedDrawUpCard = false
                }
            }
        }
    }

    /**
     * 上页片
     */
    private fun drawUpCard(canvas: Canvas) {
        if (!isNeedDrawUpCard) return

        with(canvas) {
            mPaint.setShadowLayer(10F, 0F, 10F, Color.GRAY)
            mPaint.color = Color.WHITE
            val rectF = RectF(paddingSize, paddingSize, paddingSize + cardWidth, paddingSize + cardHeight)
            drawRoundRect(
                    rectF,
                    20F,
                    20F,
                    mPaint
            )

            //绘制数字
            mPaint.clearShadowLayer()
            val curNumBm = numBms[curShowNum]
            // 根据状态绘制不同的数字
            if (curState == STATE_DOWN_ING) {
                //往下翻，显示前一个数字
                var tempBm: Bitmap? = null
                if (curShowNum - 1 >= 0) {
                    tempBm = numBms[curShowNum - 1]
                    drawBitmap(tempBm, Rect(0, 0, tempBm.width, tempBm.height / 2), rectF, mPaint)
                }
            } else {
                //绘制当前数字
                drawBitmap(curNumBm, Rect(0, 0, curNumBm.width, curNumBm.height / 2), rectF, mPaint)
            }


        }
    }

    /**
     * 中页片（活动页片）
     */
    private fun drawMidCard(canvas: Canvas) {
        if (!isNeedDrawMidCard) return

        with(canvas) {
            save()
            mMatrix.reset()
            mCamera.save()
            mCamera.translate(0F, 0F, depthZ)
            mCamera.rotateX(rotateX)
            mCamera.rotateY(rotateY)
            mCamera.getMatrix(mMatrix)
            mCamera.restore()

            val scale = resources.displayMetrics.density
            val mValues = FloatArray(9)
            mMatrix.getValues(mValues)
            mValues[6] = mValues[6] / scale
            mValues[7] = mValues[7] / scale
            mMatrix.setValues(mValues)

            mMatrix.preTranslate(-width / 2F, -height / 2F)
            mMatrix.postTranslate(width / 2F, height / 2F)
            concat(mMatrix)
            mPaint.color = Color.WHITE
            mPaint.setShadowLayer(cardShadowSize, 0F, cardShadowDistance, Color.GRAY)

            val rectF = RectF(paddingSize, paddingSize + cardHeight, paddingSize + cardWidth, paddingSize + cardHeight * 2)
            drawRoundRect(
                    rectF,
                    20F,
                    20F,
                    mPaint
            )
            mPaint.clearShadowLayer()
            val curNumBm = numBms[curShowNum]
            if (rotateX >= 90F) {
                val matrix = Matrix()
                matrix.postRotate(180F)
                matrix.postScale(-1F, 1F)
                var tempBm: Bitmap? = null
                if (curState == STATE_UP_ING) {
                    //绘制下一个倒置翻转的数字图片
                    if (curShowNum + 1 <= 9) {
                        tempBm = Bitmap.createBitmap(numBms[curShowNum + 1], 0, 0, curNumBm.width, curNumBm.height, matrix, false)
                    }
                } else if (curState == STATE_DOWN_ING) {
                    //往下翻
                    if (abs(cardRotateFunc!!.initValue - rotateX) >= 90F) {
                        //绘制前一个数字
                        if (curShowNum - 1 >= 0) {
                            tempBm = Bitmap.createBitmap(numBms[curShowNum - 1], 0, 0, curNumBm.width, curNumBm.height, matrix, false)
                        } else {
                            tempBm = Bitmap.createBitmap(numBms[0], 0, 0, curNumBm.width, curNumBm.height, matrix, false)
                        }
                    } else {
                        tempBm = Bitmap.createBitmap(numBms[curShowNum], 0, 0, curNumBm.width, curNumBm.height, matrix, false)
                    }
                }
                tempBm?.let {
                    drawBitmap(it, Rect(0, it.height / 2, it.width, it.height), rectF, mPaint)
                }
            } else {
                if (abs(cardRotateFunc!!.initValue - rotateX) >= 90F) {
                    //绘制前一个数字
                    var tempBm: Bitmap? = null
                    if (curShowNum - 1 >= 0) {
                        tempBm = numBms[curShowNum - 1]
                    }
                    tempBm?.let {
                        drawBitmap(it, Rect(0, it.height / 2, it.width, it.height), rectF, mPaint)
                    }
                } else {
                    drawBitmap(curNumBm, Rect(0, curNumBm.height / 2, curNumBm.width, curNumBm.height), rectF, mPaint)
                }
            }

            restore()
        }
    }

    /**
     * 下页片
     */
    private fun drawDownCard(canvas: Canvas) {
        if (!isNeedDrawDownCard) return

        with(canvas) {
            mPaint.setShadowLayer(10F, 0F, 10F, Color.GRAY)
            mPaint.color = Color.WHITE
            val rectF = RectF(paddingSize, paddingSize + cardHeight, paddingSize + cardWidth, paddingSize + cardHeight * 2)
            drawRoundRect(
                    rectF,
                    20F,
                    20F,
                    mPaint
            )

            //绘制数字
            mPaint.clearShadowLayer()
            val curNumBm = numBms[curShowNum]
            //往上翻，显示下一个数字
            if (curState == STATE_UP_ING) {
                var tempBm = numBms[curShowNum + 1]
                if (curShowNum + 1 <= 9) {
                    drawBitmap(tempBm, Rect(0, tempBm.height / 2, tempBm.width, tempBm.height), rectF, mPaint)
                }
            } else {
                drawBitmap(curNumBm, Rect(0, curNumBm.height / 2, curNumBm.width, curNumBm.height), rectF, mPaint)
            }
        }
    }

    /**
     * 卡片翻转动画
     */
    private var cardRotateAnim: ValueAnimator? = null

    /**
     * 卡片上翻动画
     */
    private fun startCardUpAnim(curNum: Int) {
        cardRotateAnim?.cancel()
        cardRotateAnim = ValueAnimator.ofFloat(rotateX, 180F)
        with(cardRotateAnim!!) {
            duration = 400L
            addUpdateListener {
                rotateX = it.animatedValue as Float
                executeShadowFunc(rotateX)
                postInvalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    resetInitValue()
                    curState = STATE_NORMAL
                    curShowNum = curNum
                }
            })
            start()
        }
    }

    /**
     * 卡片下翻动画
     */
    private fun startCardDownAnim(curNum: Int) {
        cardRotateAnim?.cancel()
        cardRotateAnim = ValueAnimator.ofFloat(rotateX, 0F)
        with(cardRotateAnim!!) {
            duration = 400L
            addUpdateListener {
                rotateX = it.animatedValue as Float
                executeShadowFunc(rotateX)
                postInvalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    resetInitValue()
                    curState = STATE_NORMAL
                    curShowNum = curNum
                }
            })
            start()
        }
    }

    /**
     * 手指按下的初始坐标
     */
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var offsetY: Float = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                if (downY >= height / 2) {
                    //绘制下方的mid card
                    rotateX = 0F
                    curState = STATE_UP_ING
                } else {
                    rotateX = 180F
                    curState = STATE_DOWN_ING
                }
                resetInitValue()
                postInvalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                offsetY = event.y - downY
                executeFunc(offsetY)
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                //判断是上翻还是下翻
                if (rotateX >= 90F) {
                    if (abs(cardRotateFunc!!.initValue - rotateX) >= 90F) {
                        if (curShowNum + 1 <= 9) {
                            startCardUpAnim(curShowNum + 1)
                        } else {
                            curShowNum = 9
                            startCardDownAnim(9)
                        }
                    } else {
                        startCardUpAnim(curShowNum)
                    }
                } else {
                    if (abs(cardRotateFunc!!.initValue - rotateX) >= 90F) {
                        if (curShowNum - 1 >= 0) {
                            startCardDownAnim(curShowNum - 1)
                        } else {
                            curShowNum = 0
                            startCardUpAnim(0)
                        }
                    } else {
                        startCardDownAnim(curShowNum)
                    }
                }
                downX = 0F
                downY = 0F
            }
            else -> {

            }
        }
        return true
    }
}