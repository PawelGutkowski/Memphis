package memphis.game.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import memphis.game.core.Actor

class InGameInputProcessor(val actor: Actor) : InputAdapter() {

    val bindings: Map<Int, InGameAction> = mapOf(
            Input.Keys.LEFT to InGameAction.GO_LEFT,
            Input.Keys.RIGHT to InGameAction.GO_RIGHT,
            Input.Keys.DOWN to InGameAction.GO_DOWN,
            Input.Keys.UP to InGameAction.GO_UP,
            Input.Keys.Z to InGameAction.BLOCK,
            Input.Keys.X to InGameAction.HIT
    )

    fun processAction(keycode: Int, handler: (InGameAction)-> Boolean) : Boolean {
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