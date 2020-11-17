package com.pengyeah.flowview.func

class Func15 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    var rate: Float = 0F

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < (inParamMax - inParamMin) / 4) {
            return outParamMin + inParam
        } else if (inParam > (inParamMax - inParamMin) / 4 && inParam <= inParamMax) {
            val result = inParam * rate + outParamMin
            if (result >= outParamMax) {
                return outParamMax
            } else {
                return result
            }
        } else {
            return outParamMax
        }
    }
}