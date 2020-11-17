package com.pengyeah.tear.func

import BaseFuncImpl

class CrimpSizeFunc : BaseFuncImpl {
    constructor()

    override fun execute(offset: Float): Float {
        var rate = (outParamMax - outParamMin) / (inParamMax / 4 - inParamMin)
        if (offset >= inParamMin && offset <= inParamMax / 4) {
            return offset * rate
        } else if (offset < inParamMin) {
            return outParamMin
        } else if (offset > inParamMax / 4 && offset <= inParamMax / 2) {
            return outParamMax - (offset - inParamMax / 4) * rate
        } else if (offset > inParamMax / 2 && offset <= inParamMax * 3 / 4) {
            return (offset - inParamMax / 2) * rate
        } else {
            var result = outParamMax - (offset - inParamMax * 3 / 4) * rate
            if (result <= 0F) {
                result = 0F
            }
            return result
        }
        return super.execute(offset)
    }
}