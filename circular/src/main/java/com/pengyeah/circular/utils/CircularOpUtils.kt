package com.pengyeah.circular.utils

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.sqrt

/**
 *  Created by pengyeah on 2020/9/16
 *  佛祖开光，永无bug
 *  God bless U
 */
class CircularOpUtils {

    constructor()

    /**
     * 操作之前的角度
     */
    var preDegree: Float = 0F

    /**
     * 当前角度
     */
    var curDegree: Float = 0F

    /**
     * 起始角度,startAngle<endAngle
     */
    var startAngle: Float = 0F

    /**
     * 结束角度,startAngle<endAngle
     */
    var endAngle: Float = 360F

    /**
     *  圆心坐标
     */
    var centerX: Float = 0F
    var centerY: Float = 0F

    /**
     * 计算当前角度
     */
    fun computeCurAngle(x1: Float, y1: Float, x2: Float, y2: Float) {
        curDegree = preDegree + calculateAngle(x1, y1, x2, y2)
    }

    /**
     * 重置操作之前的角度
     */
    fun resetPreDegree() {
        preDegree = curDegree
    }


    /**
     * 计算两点的夹角
     */
    fun calculateAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val angle1 = calculateAngle(x1, y1)
        val angle2 = calculateAngle(x2, y2)
        return angle2 - angle1
    }

    /**
     * 计算坐标点与x轴的夹角
     */
    fun calculateAngle(x: Float, y: Float): Float {
        val distance = sqrt(((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)))
        if (distance == 0F) {
            return 0F
        }
        var degree = acos((x - centerX) / distance) * 180 / PI.toFloat()
        if (y < centerY) {
            degree = 360 - degree
        }
        return degree
    }
}