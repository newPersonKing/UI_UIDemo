package com.pengyeah.flowview.func

/**
 *  Created by pengyeah on 2020/9/1
 *  佛祖开光，永无bug
 *  God bless U
 */
interface IFunc {

    /**
     * 初始值
     */
    var initValue: Float

    /**
     * 入参的阈值
     */
    var inParamMax: Float

    /**
     * 入参的阈值
     */
    var inParamMin: Float

    /**
     * 出参的阈值
     */
    var outParamMax:Float


    /**
     * 出参的阈值
     */
    var outParamMin:Float

    fun execute(inParam: Float): Float
}