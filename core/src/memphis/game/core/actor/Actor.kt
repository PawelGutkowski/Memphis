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
        IDLE, RUN, EXPLODE
    }

    open var action : Action = Action(ActionType.IDLE)

    var currentFrame : TextureRegion? = null

    var currentAnimation : NamedAnimation = animations[0]

    override fun render(spriteBatch: SpriteBatch){
        action.time += Gdx.graphics.deltaTime
        startRender()
        currentFrame = renderFrame(spriteBatch, currentAnimation.getKeyFrame(action.time, true))
        finishRender()
    }

    open fun updateAction(action: Action) {
        this.action = action
        this.currentAnimation = getAnimation(this.action)
    }

    open fun getAnimation(action : Action) : NamedAnimation {
        return animations.find { it.name == action.type.name.toLowerCase()} ?: throw Exception("No animation ${action.type.name} for Actor $this")
    }
}