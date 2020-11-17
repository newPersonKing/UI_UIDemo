package com.pengyeah.flowview.func

/**
 *  Created by pengyeah on 2020/9/2
 *  佛祖开光，永无bug
 *  God bless U
 */
class Func6 : BaseFuncImpl {

    constructor(initValue: Float, inParamMax: Float) : super(initValue, inParamMax)

    override fun execute(offset: Float): Float {
        super.execute(offset)
        if (initValue + offset > inParamMax) {
            return inParamMax
        } else if (initValue + offset <= 0) {
            return 0F
        } else {
            return initValue + offset * 2
        }
    }
}