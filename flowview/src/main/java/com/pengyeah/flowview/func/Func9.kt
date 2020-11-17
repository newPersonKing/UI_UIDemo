package com.pengyeah.flowview.func

import android.util.Log

class Func9 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 2) {
            return inParamMin
        } else if (inParam >= inParamMax / 2) {
            val k = (outParamMax - outParamMin) / (inParamMax / 2)
            return inParamMin + (inParam - inParamMax / 2) * k
        } else {
            return outParamMax
        }
    }
}