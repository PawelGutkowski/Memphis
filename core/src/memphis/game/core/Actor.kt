package memphis.game.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import memphis.game.input.ActorAction

abstract class Actor(val animations : List<NamedAnimation>) {

    enum class Orientation(val scaleX: Float) { LEFT(-1f), RIGHT(1f) }

    open val position: Vector2 = Vector2(100f, 10f)

    open val size: Vector2 = Vector2.Zero

    open val actions : MutableSet<ActorAction> = mutableSetOf(ActorAction.STAND)

    var currentAnimation : NamedAnimation = getAnimation("stand")

    var currentFrame : TextureRegion? = null

    var orientation = Orientation.RIGHT

    private var stateTime = 0f

    fun render(batch: SpriteBatch){
        stateTime += Gdx.graphics.deltaTime

        if(currentAnimation.isAnimationFinished(stateTime)){
            if(actions.contains(ActorAction.GO_RIGHT) && actions.contains(ActorAction.GO_LEFT)){
                actions.remove(ActorAction.GO_RIGHT)
                actions.remove(ActorAction.GO_LEFT)
            } else if(actions.contains(ActorAction.GO_LEFT)){
                orientation = Orientation.LEFT
                currentAnimation = getAnimation("walk")
            } else if(actions.contains(ActorAction.GO_RIGHT)){
                currentAnimation = getAnimation("walk")
                orientation = Orientation.RIGHT
            } else {
                currentAnimation = getAnimation("stand")
            }
        }

        if(currentAnimation.name == "walk"){
            position.add(orientation.scaleX * 7f, 0f)
        }
        if(actions.contains(ActorAction.HIT)){
            currentAnimation = getAnimation("hit")
        }
        currentFrame = renderFrame(batch, currentAnimation.getKeyFrame(stateTime, true))
    }

    private fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        if (orientation == Orientation.LEFT) {
            currentFrame.flip(true, false)
        }
        batch.draw(
                currentFrame,
                //position, bottom left corner
                position.x - (size.x / 2),
                position.y
        )
        if (currentFrame.isFlipX ) {
            currentFrame.flip(true, false)
        }
        return currentFrame
    }

    private fun getAnimation(name : String) = animations.find { it.name == name } ?: throw Exception("No animation $name for Actor $this")
}