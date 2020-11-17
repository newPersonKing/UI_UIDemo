package com.pengyeah.card3d.func

import com.pengyeah.flowview.func.BaseFuncImpl

/**
 *  Created by pengyeah on 2020/10/10
 *  佛祖开光，永无bug
 *  God bless U
 */
class CardRotateFunc : BaseFuncImpl {

    constructor()

    override fun execute(inParam: Float): Float {
        if (inParam > inParamMax) {
            return outParamMin
        } else if (inParam < inParamMin) {
            return outParamMax
        } else {
            //斜率
            val rate = (outParamMin - outParamMax) / (inParamMax - inParamMin)
            return outParamMax + inParam * rate
        }
    }
}