package com.pengyeah.card3d.func

import com.pengyeah.flowview.func.BaseFuncImpl

/**
 *  Created by pengyeah on 2020/10/9
 *  佛祖开光，永无bug
 *  God bless U
 */
class ShadowSizeFunc : BaseFuncImpl {

    constructor()

    override fun execute(inParam: Float): Float {
        val rate = (outParamMax - outParamMin) / (inParamMax / 2 - inParamMin)
        val result = rate * inParam * 2 + initValue
        if (result in outParamMin..outParamMax) {
            return result
        } else if (result < outParamMin) {
            return outParamMin
        } else {
            return outParamMax
        }
    }
}