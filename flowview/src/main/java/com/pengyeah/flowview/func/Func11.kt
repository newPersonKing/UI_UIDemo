package com.pengyeah.flowview.func

class Func11 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        super.execute(inParam)
        if (inParam < inParamMax / 2) {
            val k = (outParamMax - outParamMin) / (inParamMax / 2)
            return outParamMin + inParam * k
        } else {
            return outParamMax
        }
    }
}