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

    open var baseY: Float = 0f

    open var baseX: Float = 0f

    var disposed = false

    open val baseType : BaseType = BaseType.BEHIND

    open fun base() : Rectangle {
        return when(baseType){
            BaseType.BEHIND -> Rectangle(position.x-(baseX/2), position.y, baseX, baseY)
            BaseType.CENTRIC -> Rectangle (position.x-(baseX/2), position.y - baseY /2f, baseX, baseY)
            else -> Rectangle(0f, 0f, 0f, 0f)
        }
    }

    fun hasBase() = baseType != BaseType.NONE

    open fun origin() = Vector2(position.x, position.y + (size.y /2))

    open fun hitbox() = Rectangle (
            position.x-(baseX/2), position.y, baseX, size.y
    )

    open fun startRender() : Unit {}

    open fun finishRender() : Unit {}

    abstract fun render(spriteBatch: SpriteBatch)

    protected open fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        baseX = size.x
        baseY = size.y/2f

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

    override fun dispose() {
        disposed = true
    }

    open fun canCollide(other: Item) : Boolean = other != this

    fun translate(dx : Float, dY: Float) = environment.translate(this, dx, dY)
}