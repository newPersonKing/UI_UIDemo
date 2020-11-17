package com.pengyeah.tear.coordinate

import com.pengyeah.tear.func.IFunc

open class Coordinate {

    constructor() {

    }

    var x: Float = 0F

    var y: Float = 0F

    var xFunc: IFunc? = null

    var yFunc: IFunc? = null

    override fun toString(): String {
        return "Coordinate(x=$x, y=$y, xFunc=$xFunc, yFunc=$yFunc)"
    }


}