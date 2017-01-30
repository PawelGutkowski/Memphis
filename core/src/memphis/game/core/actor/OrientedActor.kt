package memphis.game.core.actor

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.NamedAnimation


abstract class OrientedActor(animations : List<NamedAnimation>, environment: Environment) : Actor(animations, environment) {

    companion object {
        val animationOrientationMap = mapOf(
                Item.Orientation.RIGHT to "side",
                Item.Orientation.LEFT to "side",
                Item.Orientation.DOWN to "front",
                Item.Orientation.UP to "back"
        )
    }

    override val baseType: BaseType = BaseType.CENTRIC

    var orientation = Item.Orientation.RIGHT

    override fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        updateBase(size)
        //TODO: should be replaced with render method parameters
        if (orientation == Orientation.LEFT) {
            currentFrame.flip(true, false)
        }
        batch.draw(
                currentFrame,
                //position, bottom left corner
                position.x - (size.x / 2),
                position.y,
                size.x,
                size.y
        )
        if (currentFrame.isFlipX ) {
            currentFrame.flip(true, false)
        }
        return currentFrame
    }

    override fun updateBase(size: Vector2) {
        base.y = size.x/3f
        base.x = size.x/2f
    }

    override fun updateAction(action: Action) {
        if(action.orientation != null) {
            this.orientation = action.orientation
        }
        super.updateAction(action)
    }

    override fun getAnimation(action : Action) : NamedAnimation {
        return animations.find { it.name == action.type.name.toLowerCase() }
                ?: animations.find { it.name == action.type.name.toLowerCase()+"-"+ animationOrientationMap[this.orientation]}
                ?: throw Exception("No animation ${action.type.name} for Actor $this")
    }

    open fun translate() = translate(this.orientation.x * speed(), this.orientation.y* speed())

    open protected fun speed() = GameCamera.cameraSpeed
}