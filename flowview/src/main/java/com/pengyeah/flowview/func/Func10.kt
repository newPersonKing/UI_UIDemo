package com.pengyeah.flowview.func

class Func10 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)


    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 4) {
            return outParamMin
        } else if (inParam >= inParamMax / 4 && inParam < inParamMax * 3 / 4) {
            val k = (outParamMax - outParamMin) / (inParamMax / 2)
            return outParamMin + (inParam - inParamMax / 4) * k
        } else {
            return outParamMax
        }
    }
}