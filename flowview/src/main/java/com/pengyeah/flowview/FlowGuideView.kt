package com.pengyeah.flowview

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout

class FlowGuideView : RelativeLayout {

    val views = ArrayList<FlowView>()

    var curShowPosition: Int = -1

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }

    fun addGuides(vararg resIds: Int) {
        resIds.iterator().forEach {
            val flowView = FlowView(context)
            views.add(flowView)
//            flowSurfaceView.setBackgroundColor(Color.WHITE)
            flowView.setImageResource(it)
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            flowView.layoutParams = layoutParams
            flowView.visibility = View.VISIBLE
//            flowView.scaleType = ImageView.ScaleType.FIT_XY
            flowView.setOnStateChangedListener(object : FlowView.OnStateChangedListener {
                override fun onStateChanged(state: Int) {
                    if (state == STATE_EXPANDED) {
                        showNext()
                    } else if (state == STATE_MOVING) {
//                        hideNext()
//                        showNext()
                    } else {
                        hideNext()
                    }
                }
            })
            addView(flowView)
        }
        Handler().postDelayed({
            views.iterator().withIndex().forEach {
                if (it.index == 0) {
                    it.value.showWithAnim()
                } else {
                    it.value.hideWithAnim()
                }
            }
        }, 500L)
    }

    private fun showNext() {
        if (curShowPosition + 1 < views.size) {
            curShowPosition += 1
            if (curShowPosition + 1 < views.size) {
                views[curShowPosition + 1].showWithAnim()
            }
        }
        Log.i("pengyeah", "curShowPosition==>$curShowPosition")
    }

    private fun hideNext() {
        if (curShowPosition + 1 < views.size) {
            if (views[curShowPosition + 1].visibility == View.VISIBLE) {
                views[curShowPosition + 1].hideWithAnim()
            }
        }
        curShowPosition -= 1
        Log.i("pengyeah", "curShowPosition==>$curShowPosition")
    }
}