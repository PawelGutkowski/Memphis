package memphis.game.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import memphis.game.core.Actor

class InGameInputProcessor(val actor: Actor) : InputAdapter() {

    val bindings: Map<Int, ActorAction> = mapOf(
            Input.Keys.LEFT to ActorAction.GO_LEFT,
            Input.Keys.RIGHT to ActorAction.GO_RIGHT,
            Input.Keys.DOWN to ActorAction.GO_DOWN,
            Input.Keys.UP to ActorAction.GO_UP,
            Input.Keys.Z to ActorAction.BLOCK,
            Input.Keys.X to ActorAction.HIT
    )

    fun processAction(keycode: Int, handler: (ActorAction)-> Boolean) : Boolean {
        val action = bindings[keycode]
        if (action != null) {
            handler.invoke(action)
            return true
        } else {
            return false
        }
    }

    override fun keyUp(keycode: Int): Boolean = processAction(keycode, { actor.actions.remove(it) })

    override fun keyDown(keycode: Int): Boolean = processAction(keycode, { actor.actions.add(it) })
}