package com.pengyeah.tear.utils

import android.graphics.PointF
import com.pengyeah.tear.coordinate.Coordinate


/**
 *  Created by pengyeah on 2020/10/21
 *  佛祖开光，永无bug
 *  God bless U
 */
class BazierUtils {
    private constructor()

    companion object {
        fun getBezierPoint(start: Coordinate, control: Coordinate, end: Coordinate, t: Float): Coordinate {
            val bezierPoint = Coordinate()
            bezierPoint.x = (1 - t) * (1 - t) * start.x + 2 * t * (1 - t) * control.x + t * t * end.x
            bezierPoint.y = (1 - t) * (1 - t) * start.y + 2 * t * (1 - t) * control.y + t * t * end.y
            return bezierPoint
        }
    }
}