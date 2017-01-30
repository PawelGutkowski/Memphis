package memphis.game.core.actor

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import memphis.game.core.Environment


abstract class Item(val environment: Environment) : Disposable {

    enum class Orientation(val x: Float, val y: Float) {
        LEFT(-1f, 0f),
        RIGHT(1f, 0f),
        UP(0f, 1f),
        DOWN(0f, -1f),
        UP_LEFT(-0.5f, 0.5f),
        UP_RIGHT(0.5f, 0.5f),
        DOWN_LEFT(-0.5f, -0.5f),
        DOWN_RIGHT(0.5f, -0.5f);

        companion object {
            fun of(x : Float, y: Float) : Orientation {
                return Orientation.values().sortedBy { Math.abs(it.x - x) + Math.abs(it.y - y) }.first()
            }
        }
    }

    enum class BaseType {
        CENTRIC, BEHIND, NONE
    }

    //fields
    val position: Vector2 = Vector2(0f, 0f)

    val size: Vector2 = Vector2(0f, 0f)

    open var base: Vector2 = Vector2(0f, 0f)

    var disposed = false

    open val baseType : BaseType = BaseType.BEHIND

    open fun base() : Rectangle {
        return when(baseType){
            BaseType.BEHIND -> Rectangle(position.x-(base.x/2), position.y, base.x, base.y)
            BaseType.CENTRIC -> Rectangle (position.x-(base.x/2), position.y - base.y /2f, base.x, base.y)
            else -> Rectangle(0f, 0f, 0f, 0f)
        }
    }

    fun hasBase() = baseType != BaseType.NONE

    open fun origin() = Vector2(position.x, position.y + (size.y /2))

    open fun hitbox() = Rectangle (
            position.x-(base.x/2), position.y, base.x, size.y
    )

    open fun startRender() : Unit {}

    open fun finishRender() : Unit {}

    abstract fun render(spriteBatch: SpriteBatch)

    protected open fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        updateBase(size)

        batch.draw(
                currentFrame,
                //position, bottom left corner
                position.x - (size.x / 2),
                position.y,
                size.x,
                size.y
        )
        return currentFrame
    }

    protected open fun updateBase(size: Vector2) {
        base.x = size.x
        base.y = size.y / 2f
    }

    override fun dispose() {
        disposed = true
    }

    open fun canCollide(other: Item) : Boolean = other != this

    open fun collide(other: Item) {
        if(this is Destructible){
            this.destroy()
        }
    }

    fun translate(dx : Float, dY: Float) = environment.translate(this, dx, dY)
}