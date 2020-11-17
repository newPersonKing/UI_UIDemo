package com.pengyeah.card3d

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.pengyeah.card3d.func.PageRoateFunc
import com.pengyeah.card3d.func.ShadowDistanceFunc
import com.pengyeah.card3d.func.ShadowSizeFunc
import com.pengyeah.flowview.func.IFunc


/**
 *  Created by pengyeah on 2020/9/11
 *  佛祖开光，永无bug
 *  God bless U
 */
class Card3DView : View {

    var mCamera: Camera = Camera()
    var mPaint: Paint = Paint()
    var mMatrix: Matrix = Matrix()

    var mSrcBm: Bitmap? = null

    private var depthZ: Float = 0F
    private var rotateX: Float = 80F
    private var rotateY: Float = 0F

    /**
     * 翻页时的阴影变化
     */
    private var shadowSize: Float = 50F
    private var shadowDistance: Float = 30F


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED

        mSrcBm = BitmapFactory.decodeResource(resources, R.drawable.img_sample)
    }

    var pageRotate: IFunc? = null
    var shadowSizeFunc: IFunc? = null
    var shadowDistanceFunc: IFunc? = null

    fun configFunc() {
        pageRotate = PageRoateFunc()
        with(pageRotate!!) {
            //手指移动距离的范围
            inParamMin = -height / 4F
            inParamMax = height / 4F
            //卡片旋转角度范围
            outParamMin = 0F
            outParamMax = 180F
            //初始角度
            initValue = 0F
        }

        shadowSizeFunc = ShadowSizeFunc()
        with(shadowSizeFunc!!) {
            //手指移动距离的范围
            inParamMin = -height / 4F
            inParamMax = height / 4F
            //阴影大小范围
            outParamMin = 0F
            outParamMax = 50F
            //初始阴影大小
            initValue = 10F
        }

        shadowDistanceFunc = ShadowDistanceFunc()
        with(shadowDistanceFunc!!) {
            //手指移动距离的范围
            inParamMin = -height / 4F
            inParamMax = height / 4F
            //阴影距离范围
            outParamMin = 0F
            outParamMax = 30F
            //初始阴影距离
            initValue = 10F
        }
    }

    fun executeFunc(offset: Float) {
        pageRotate?.let {
            rotateX = it.execute(offset)
        }

        shadowSizeFunc?.let {
            shadowSize = it.execute(offset)
        }

        shadowDistanceFunc?.let {
            shadowDistance = it.execute(offset)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        canvas?.let {
            mPaint.setShadowLayer(10F, 0F, 10F, Color.GRAY)
            mPaint.color = Color.WHITE
            it.drawRoundRect(RectF(width / 4F, height / 4F, width * 3 / 4F, height / 2F), 20F, 20F, mPaint)

            mPaint.color = Color.WHITE
            it.drawRoundRect(RectF(width / 4F, height / 2F, width * 3 / 4F, height * 3 / 4F), 20F, 20F, mPaint)

            it.save()
            mMatrix.reset()
            mCamera.save()
            mCamera.translate(0F, 0F, depthZ)
            mCamera.rotateX(rotateX)
            mCamera.rotateY(rotateY)
            mCamera.getMatrix(mMatrix)
            mCamera.restore()

            // 修正失真，主要修改 MPERSP_0 和 MPERSP_1
            // 修正失真，主要修改 MPERSP_0 和 MPERSP_1
            val scale = resources.displayMetrics.density
            val mValues = FloatArray(9)
            mMatrix.getValues(mValues) //获取数值
            mValues[6] = mValues[6] / scale //数值修正
            mValues[7] = mValues[7] / scale //数值修正
            mMatrix.setValues(mValues)

            mMatrix.preTranslate(-width / 2F, -height / 2F)
            mMatrix.postTranslate(width / 2F, height / 2F)
            it.concat(mMatrix)
            mPaint.color = Color.WHITE
            mPaint.setShadowLayer(shadowSize, 0F, shadowDistance, Color.GRAY)

            it.drawRoundRect(RectF(width / 4F, height / 2F, width * 3 / 4F, height * 3 / 4F), 20F, 20F, mPaint)
            it.restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        configFunc()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    var pageAnim: ValueAnimator? = null
    var shadowSizeAnim: ValueAnimator? = null
    var shadowDistanceAnim: ValueAnimator? = null

    fun startPageUpAnim() {
        pageAnim?.cancel()
        pageAnim = ValueAnimator.ofFloat(rotateX, 180F)
        with(pageAnim!!) {
            duration = 800L
            addUpdateListener {
                rotateX = it.animatedValue as Float
            }
            start()
        }

        shadowSizeAnim?.cancel()
        shadowSizeAnim = ValueAnimator.ofFloat(shadowSize, 60F, -10F, 0F)
        with(shadowSizeAnim!!) {
            duration = 800L
            addUpdateListener {
                shadowSize = it.animatedValue as Float
            }
            start()
        }

        shadowDistanceAnim?.cancel()
        shadowDistanceAnim = ValueAnimator.ofFloat(shadowDistance, 30F, 0F)
        with(shadowDistanceAnim!!) {
            duration = 800L
            addUpdateListener {
                shadowDistance = it.animatedValue as Float
            }
            start()
        }
    }

    fun startPageDownAnim() {
        pageAnim?.cancel()
        pageAnim = ValueAnimator.ofFloat(rotateX, 0F)
        with(pageAnim!!) {
            duration = 800L
            addUpdateListener {
                rotateX = it.animatedValue as Float
            }
            start()
        }

        shadowSizeAnim?.cancel()
        shadowSizeAnim = ValueAnimator.ofFloat(shadowSize, -10F, 60F, 10F)
        with(shadowSizeAnim!!) {
            duration = 800L
            addUpdateListener {
                shadowSize = it.animatedValue as Float
            }
            start()
        }

        shadowDistanceAnim?.cancel()
        shadowDistanceAnim = ValueAnimator.ofFloat(shadowDistance, 0F, 30F)
        with(shadowDistanceAnim!!) {
            duration = 800L
            addUpdateListener {
                shadowDistance = it.animatedValue as Float
            }
            start()
        }
    }

    private var downX: Float = 0F
    private var downY: Float = 0F

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val offset = -event.y + downY
                executeFunc(offset)
                Log.i("pengyeah", "rotateX==>$rotateX")
                Log.i("pengyeah", "offsetY==>" + (event.y - downY))
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                downX = 0F
                downY = 0F
            }
            else -> {

            }
        }
        return true
    }
}