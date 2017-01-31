package memphis.game.core.controller

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import com.badlogic.gdx.controllers.PovDirection
import memphis.game.core.actor.Actor
import memphis.game.core.actor.Item
import memphis.game.core.actor.PlayableActor

class Xbox360Pad {
    companion object {
        val BUTTON_X = 2
        val BUTTON_Y = 3
        val BUTTON_A = 0
        val BUTTON_B = 1
        val BUTTON_BACK = 6
        val BUTTON_START = 7
        val BUTTON_DPAD_UP = PovDirection.north
        val BUTTON_DPAD_DOWN = PovDirection.south
        val BUTTON_DPAD_RIGHT = PovDirection.east
        val BUTTON_DPAD_LEFT = PovDirection.west
        val BUTTON_LB = 4
        val BUTTON_L3 = 8
        val BUTTON_RB = 5
        val BUTTON_R3 = 9
        val AXIS_LEFT_X = 1 //-1 is left | +1 is right
        val AXIS_LEFT_Y = 0 //-1 is up | +1 is down
        val AXIS_LEFT_TRIGGER = 4 //value 0 to 1f
        val AXIS_RIGHT_X = 3 //-1 is left | +1 is right
        val AXIS_RIGHT_Y = 2 //-1 is up | +1 is down
        val AXIS_RIGHT_TRIGGER = 4 //value 0 to -1f
    }

    class Listener(val actor: PlayableActor) : ControllerAdapter(), ControllerProcessor {

        val controllers : MutableSet<Controller> = mutableSetOf()

        override fun poll() {
            controllers.forEach {
                val orientation = Item.Orientation.of(it.getAxis(AXIS_LEFT_Y), -it.getAxis(AXIS_LEFT_X))
                if(orientation != Item.Orientation.NONE){
                    actor.handleMovement(orientation)
                } else {
                    if(actor.action.type == Actor.ActionType.RUN) actor.updateAction(Actor.Action(Actor.ActionType.IDLE))
                }
            }
        }

        override fun buttonDown(controller: Controller?, buttonCode: Int): Boolean {
            if(controller != null) controllers.add(controller)
            when(buttonCode){
                BUTTON_X -> actor.shoot()
            }
            return true
        }
    }
}