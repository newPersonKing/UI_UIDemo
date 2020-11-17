package com.pengyeah.flowview.func

class Func13 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 4) {
            return outParamMax
        } else if (inParam >= (inParamMax - inParamMax) / 4 && inParam < (inParamMax - inParamMin) * 3 / 4) {
            val k = (outParamMin - outParamMax) / (inParamMax / 2)
            return outParamMax + (inParam - (inParamMax - inParamMin) / 4) * k
        } else {
            return outParamMin
        }
    }

}