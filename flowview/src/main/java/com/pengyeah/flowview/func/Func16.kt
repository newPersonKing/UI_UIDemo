package com.pengyeah.flowview.func

/**
 *  Created by pengyeah on 2020/9/9
 *  佛祖开光，永无bug
 *  God bless U
 */
class Func16 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)


    override fun execute(offset: Float): Float {
        super.execute(offset)
        if (offset > inParamMin && offset < inParamMax) {
            return initValue + offset
        } else if (offset < inParamMin) {
            return initValue
        } else {
            return outParamMax
        }
    }
}