package com.pengyeah.flowview.func

/**
 *  Created by pengyeah on 2020/9/1
 *  佛祖开光，永无bug
 *  God bless U
 */
class Func4 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(offset: Float): Float {
        if (offset < inParamMax / 2) {
            return initValue
        } else if (offset > inParamMax) {
            return initValue - inParamMax
        } else {
            return initValue - 2 * (offset - (inParamMax / 2))
        }
    }
}