package com.pengyeah.flowview.func

class Func12 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 2) {
            val k = (outParamMin - outParamMax) / ((inParamMax - inParamMin) / 2)
            return outParamMax + inParam * k
        } else {
            return outParamMin
        }
    }
}