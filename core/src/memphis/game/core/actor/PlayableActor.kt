
package memphis.game.core.actor

import com.badlogic.gdx.Input
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.GameInputProcessor
import memphis.game.core.NamedAnimation


open class PlayableActor(animations : List<NamedAnimation>, environment: Environment) : OrientedActor(animations, environment), GameInputProcessor {

    init {
        currentAnimation = getAnimation(action)
    }

    val polledKeys : MutableMap<Int, Int> = mutableMapOf()

    override fun keyUp(keycode: Int): Boolean {
        polledKeys[keycode] = -1
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        polledKeys[keycode] = 0
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        when(character){
            'q' -> environment.shoot(this.origin(), this.orientation)
        }
        return true
    }

    override fun startRender() {
        polledKeys.entries.filter { it.value >= 0 }.forEach { polledKeys[it.key] = it.value+1 }
        if (action.type == ActionType.RUN || action.type == ActionType.IDLE) {
            handleMovement(polledKeys)
        }
    }

    override fun finishRender() {
        polledKeys.entries.removeAll { it.value == -1 }
    }

    private fun handleMovement(polledKeys: MutableMap<Int, Int>) {
        val key = polledKeys
                .filter { setOf(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.UP).contains(it.key) }
                .filter { it.value >= 0 }
                .minBy { it.value }?.key
        when(key){
            Input.Keys.LEFT -> handleMovement(Orientation.LEFT)
            Input.Keys.RIGHT -> handleMovement(Orientation.RIGHT)
            Input.Keys.UP -> handleMovement(Orientation.UP)
            Input.Keys.DOWN -> handleMovement(Orientation.DOWN)
            else -> {
                if(action.type == ActionType.RUN) updateAction(Action(ActionType.IDLE))
            }
        }
    }

    private fun handleMovement(orientation: Orientation){
        if(action.type != ActionType.RUN || this.orientation != orientation){
            updateAction(Action(ActionType.RUN, orientation))
        }
        translate(this.orientation.x * GameCamera.cameraSpeed, this.orientation.y*GameCamera.cameraSpeed)
    }
}