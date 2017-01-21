package memphis.game.core.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import memphis.game.core.Environment
import memphis.game.core.NamedAnimation

abstract class Actor(val animations : List<NamedAnimation>, environment: Environment) : Item(environment) {

    //Inner classes
    data class Action(val type : ActionType, val orientation : Orientation? = null, var time : Float = 0f)

    enum class ActionType(){
        IDLE, RUN
    }

    open var action : Action = Action(ActionType.IDLE)

    var currentFrame : TextureRegion? = null

    var currentAnimation : NamedAnimation = animations[0]

    fun render(batch: SpriteBatch){
        action.time += Gdx.graphics.deltaTime
        startRender()
        currentFrame = renderFrame(batch, currentAnimation.getKeyFrame(action.time, true))
        finishRender()
    }

    open fun startRender() : Unit {}

    open fun finishRender() : Unit {}

    protected open fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        baseSize = size.y/2f

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

    protected open fun updateAction(action: Action) {
        this.action = action
        this.currentAnimation = getAnimation(this.action)
    }

    open fun getAnimation(action : Action) : NamedAnimation {
        return animations.find { it.name == action.type.name.toLowerCase()} ?: throw Exception("No animation ${action.type.name} for Actor $this")
    }
}