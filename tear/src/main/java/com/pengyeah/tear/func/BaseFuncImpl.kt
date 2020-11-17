import com.pengyeah.tear.func.IFunc

/**
 *  Created by pengyeah on 2020/9/1
 *  佛祖开光，永无bug
 *  God bless U
 */
open class BaseFuncImpl : IFunc {


    override var outParamMax: Float = 0F
    override var outParamMin: Float = 0F

    override var inParamMin: Float = 0F
    override var initValue: Float = 0F
    override var inParamMax: Float = 0F

    constructor()

    override fun execute(inParam: Float): Float {
        return 0F
    }

    override fun toString(): String {
        return "BaseFuncImpl(initValue=$initValue, inParamMax=$inParamMax, inParamMin=$inParamMin)"
    }
}