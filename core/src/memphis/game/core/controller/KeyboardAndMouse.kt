package memphis.game.core.controller

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import memphis.game.core.actor.Actor
import memphis.game.core.actor.Item
import memphis.game.core.actor.PlayableActor
import java.util.*


class KeyboardAndMouse {
    class Listener(val actor: PlayableActor) : InputAdapter(), ControllerProcessor {

        companion object {
            val orientationMap = mapOf(
                    arrayOf(Input.Keys.UP) to Item.Orientation.UP,
                    arrayOf(Input.Keys.DOWN) to Item.Orientation.DOWN,
                    arrayOf(Input.Keys.LEFT) to Item.Orientation.LEFT,
                    arrayOf(Input.Keys.RIGHT) to Item.Orientation.RIGHT,
                    arrayOf(Input.Keys.UP, Input.Keys.LEFT) to Item.Orientation.UP_LEFT,
                    arrayOf(Input.Keys.UP, Input.Keys.RIGHT) to Item.Orientation.UP_RIGHT,
                    arrayOf(Input.Keys.DOWN, Input.Keys.LEFT) to Item.Orientation.DOWN_LEFT,
                    arrayOf(Input.Keys.DOWN, Input.Keys.RIGHT) to Item.Orientation.DOWN_RIGHT
            )
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
                'q' -> actor.shoot()
            }
            return true
        }

/*        fun handleMovementSingleKey(polledKeys: MutableMap<Int, Int>) {
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
        }*/

        fun handleMovement(polledKeys: MutableMap<Int, Int>) {
            val keys = polledKeys
                    .filterKeys { setOf(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.UP).contains(it) }
                    .filterValues { it >= 0 }
                    .toSortedMap(Comparator { l, r -> l - r })
                    .keys
                    .filterIndexed { i, entry -> i < 2 }

            val orientation = orientationMap[keys.toTypedArray()]
            if(orientation != null){
                actor.handleMovement(orientation)
            } else {
                if(actor.action.type == Actor.ActionType.RUN) actor.updateAction(Actor.Action(Actor.ActionType.IDLE))
            }
        }

        override fun poll() {
            handleMovement(polledKeys)
        }
    }
}