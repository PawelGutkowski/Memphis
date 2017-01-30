package memphis.game.core.actor

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import memphis.game.core.Environment
import memphis.game.core.NamedAnimation


open class UniOrientedActor(animations : List<NamedAnimation>, environment: Environment) : OrientedActor(animations, environment) {

    override fun updateAction(action: Actor.Action) {
        if(action.orientation != null) {
            this.orientation = action.orientation
        }
        super.updateAction(action)
    }

    override fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        updateBase(size)
        val rotation = when(orientation){
            Orientation.LEFT -> 180f
            Orientation.DOWN -> 270f
            Orientation.UP -> 90f
            else -> 0f
        }
        batch.draw(
                currentFrame,
                //position, bottom left corner
                position.x - (size.x / 2),
                position.y,
                size.x/2,
                size.y/2,
                size.x,
                size.y,
                1f, 1f,
                rotation
        )
        return currentFrame
    }

    override fun getAnimation(action : Actor.Action) : NamedAnimation {
        return animations.find { it.name == action.type.name.toLowerCase() } ?: throw Exception("No animation ${action.type.name} for Actor $this")
    }
}