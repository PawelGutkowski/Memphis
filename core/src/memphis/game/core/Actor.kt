package memphis.game.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

abstract class Actor(val animations : List<NamedAnimation>) : InputAdapter() {

    //Inner classes
    data class Action(val type : ActionType, val orientation : Orientation){
        fun execute(actor: Actor) = type.execution.invoke(actor, this)
    }

    enum class ActionType(val order : Int, val polled : Boolean = false, val execution : (Actor, Action)->Unit = {actor, action -> }){
        ROTATE(80, execution = {actor, action -> actor.orientation = action.orientation}),
        GO(60, true, {actor, action ->
            if (actor.state.name == "run" && !actor.currentAnimation.isAnimationFinished(actor.state.time)) {
                actor.position.add(8f*action.orientation.x, 10f*action.orientation.y)
            } else {
                actor.changeState("run")
            }
        }),
        JUMP(150),
        HIT(200),
        BLOCK(100, true)
    }

    data class State(val name: String, var time : Float = 0f)

    enum class Orientation(val x: Float, val y: Float) { LEFT(-1f, 0f), RIGHT(1f, 0f), UP(0f, 1f), DOWN(0f, -1f) }

    //fields
    open val position: Vector2 = Vector2(100f, 10f)

    open val size: Vector2 = Vector2.Zero

    open val polledKeys : MutableMap<Int, Int> = mutableMapOf()

    open val actions : MutableSet<Action> = mutableSetOf()

    var state: State = State("idle")

    var currentFrame : TextureRegion? = null

    var orientation = Orientation.RIGHT

    var currentAnimation : NamedAnimation = getAnimation(state)

    override fun keyUp(keycode: Int): Boolean {
        polledKeys[keycode] = -1
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        polledKeys[keycode] = 0
        return true
    }

    fun render(batch: SpriteBatch){
        state.time += Gdx.graphics.deltaTime
        polledKeys.entries.filter { it.value >= 0 }.forEach { polledKeys[it.key] = it.value+1 }

        polledKeys.forEach { handlePolledKey(it.key, it.value) }
        actions.forEach { it.execute(this) }
        actions.removeAll{ !it.type.polled }

        currentFrame = renderFrame(batch, currentAnimation.getKeyFrame(state.time, true))
        if(currentAnimation.isAnimationFinished(state.time)){
            changeState("idle")
            actions.clear()
        }
        polledKeys.entries.removeAll { it.value == -1 }
    }

    private fun handlePolledKey(keycode: Int, keyDuration : Int){
        when(keycode){
            Input.Keys.LEFT -> handleMovement(Orientation.LEFT, keyDuration)
            Input.Keys.RIGHT -> handleMovement(Orientation.RIGHT, keyDuration)
            Input.Keys.UP -> handleMovement(Orientation.UP, keyDuration)
            Input.Keys.DOWN -> handleMovement(Orientation.DOWN, keyDuration)
        }
    }

    private fun handleMovement(orientation: Orientation, keyDuration: Int) {
        when{
            keyDuration == -1 -> {
                actions.removeAll{ (it.type == ActionType.GO || it.type == ActionType.ROTATE) && it.orientation == orientation }
                changeState("idle")
            }
            keyDuration > 5 -> actions.add(Action(ActionType.GO, orientation))
            else -> actions.add(Action(ActionType.ROTATE, orientation))
        }
    }

    private fun renderFrame(batch: SpriteBatch, currentFrame : TextureRegion) : TextureRegion {
        size.set(
                currentFrame.regionWidth.toFloat(),
                currentFrame.regionHeight.toFloat()
        )
        //TODO: should be replaced with render method parameters
        if (orientation == Orientation.LEFT) {
            currentFrame.flip(true, false)
        }
        batch.draw(
                currentFrame,
                //position, bottom left corner
                position.x - (size.x / 2),
                position.y,
                currentFrame.regionWidth * 4f,
                currentFrame.regionHeight * 4f
        )
        if (currentFrame.isFlipX ) {
            currentFrame.flip(true, false)
        }
        return currentFrame
    }

    private fun changeState(stateName : String) {
        state = State(stateName)
        currentAnimation = getAnimation(state)
    }

    private fun getAnimation(name: State) = animations.find { it.name == state.name } ?: throw Exception("No animation $name for Actor $this")
}