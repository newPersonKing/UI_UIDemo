package com.pengyeah.flowview.func

class Func14 : BaseFuncImpl {


    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 2) {
            return outParamMax
        } else if (inParam >= (inParamMax - inParamMin) / 2 && inParam < inParamMax) {
            val k = (outParamMin - outParamMax) / ((inParamMax - inParamMin) / 2)
            return outParamMax + (inParam - (inParamMax - inParamMin) / 2) * k
        } else {
            return outParamMin
        }
    }
}