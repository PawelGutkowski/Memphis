package memphis.game.core.actor

import com.badlogic.gdx.math.Vector2
import memphis.game.core.Environment
import memphis.game.core.Hitbox


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

    //fields
    val position: Vector2 = Vector2(100f, 100f)

    open val size: Vector2 = Vector2.Zero

    fun origin() = Vector2(position.x, position.y + (size.y /2))

    fun hitbox() = Hitbox(position.x-(size.x/2), position.y-size.y/4, position.x+(size.x/2), position.y+size.y/4)

    fun translate(dx : Float, dY: Float) = environment.translate(this, dx, dY)
}