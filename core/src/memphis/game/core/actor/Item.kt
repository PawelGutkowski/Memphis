package memphis.game.core.actor

import com.badlogic.gdx.math.Vector2
import memphis.game.core.Environment
import memphis.game.core.Box


abstract class Item(val environment: Environment) {

    constructor(environment: Environment, position : Vector2) : this(environment) {
        this.position.set(position)
    }

    enum class Orientation(val x: Float, val y: Float) {
        LEFT(-1f, 0f),
        RIGHT(1f, 0f),
        UP(0f, 1f),
        DOWN(0f, -1f)
    }

    enum class BaseType {
        CENTRIC, BEHIND
    }

    //fields
    val position: Vector2 = Vector2(0f, 0f)

    val size: Vector2 = Vector2(0f, 0f)

    open var baseSize : Float = 0f

    open val baseType : BaseType = BaseType.BEHIND

    open fun base() : Box {
        return if(baseType == BaseType.BEHIND){
            Box(
                    position.x-(size.x/2), position.y,
                    position.x+(size.x/2), position.y + baseSize
            )
        } else {
            Box(
                    position.x-(size.x/2), position.y - baseSize/2f,
                    position.x+(size.x/2), position.y + baseSize/2f
            )
        }
    }

    open fun origin() = Vector2(position.x, position.y + (size.y /2))

    open fun hitbox() = Box(
            position.x-(size.x/2), position.y,
            position.x+(size.x/2), position.y+size.y
    )

    fun translate(dx : Float, dY: Float) = environment.translate(this, dx, dY)
}