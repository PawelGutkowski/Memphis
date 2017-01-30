package memphis.game.core.controller

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import memphis.game.core.actor.Actor
import memphis.game.core.actor.Item
import memphis.game.core.actor.PlayableActor


class KeyboardAndMouse {
    class Listener(val actor: PlayableActor) : InputAdapter(), ControllerProcessor {
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
                'q' -> actor.shoot()
            }
            return true
        }

        private fun handleMovement(polledKeys: MutableMap<Int, Int>) {
            val key = polledKeys
                    .filter { setOf(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.UP).contains(it.key) }
                    .filter { it.value >= 0 }
                    .minBy { it.value }?.key
            when(key){
                Input.Keys.LEFT -> actor.handleMovement(Item.Orientation.LEFT)
                Input.Keys.RIGHT -> actor.handleMovement(Item.Orientation.RIGHT)
                Input.Keys.UP -> actor.handleMovement(Item.Orientation.UP)
                Input.Keys.DOWN -> actor.handleMovement(Item.Orientation.DOWN)
                else -> {
                    if(actor.action.type == Actor.ActionType.RUN) actor.updateAction(Actor.Action(Actor.ActionType.IDLE))
                }
            }
        }

        override fun poll() {
            handleMovement(polledKeys)
        }
    }
}