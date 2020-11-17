package com.pengyeah.card3d.func

import com.pengyeah.flowview.func.BaseFuncImpl

/**
 *  Created by pengyeah on 2020/10/9
 *  佛祖开光，永无bug
 *  God bless U
 */
class PageRoateFunc : BaseFuncImpl {

    constructor()
    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(inParam: Float): Float {
        val rate = (outParamMax - outParamMin) / (inParamMax - inParamMin)
        val result = rate * inParam + initValue
        if (result in outParamMin..outParamMax) {
            return result
        } else if (result < outParamMin) {
            return outParamMin
        } else {
            return outParamMax
        }
    }

}