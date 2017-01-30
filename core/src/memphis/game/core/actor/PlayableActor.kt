
package memphis.game.core.actor

import com.badlogic.gdx.math.MathUtils
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.NamedAnimation
import memphis.game.core.controller.ControllerProcessor
import memphis.game.core.event.ShotEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


open class PlayableActor(animations : List<NamedAnimation>, environment: Environment) : OrientedActor(animations, environment) {

    init {
        currentAnimation = getAnimation(action)
    }

    val polledKeys : MutableMap<Int, Int> = mutableMapOf()

    val executor : ExecutorService = Executors.newSingleThreadExecutor()

    var handleInputFuture : Future<*>? = null

    var controllerProcessor : ControllerProcessor? = null

    fun shoot() {
        environment.shoot(ShotEvent(this.origin().cpy().add(MathUtils.random(-4f, 4f), MathUtils.random(-4f, 4f)), this.orientation, this))
    }

    override fun startRender() {
        polledKeys.entries.filter { it.value >= 0 }.forEach { polledKeys[it.key] = it.value + 1 }
        if (action.type == ActionType.RUN || action.type == ActionType.IDLE) {
            controllerProcessor?.poll()
        }
        polledKeys.entries.removeAll { it.value == -1 }
    }

    fun handleMovement(orientation: Orientation){
        if(action.type != ActionType.RUN || this.orientation != orientation){
            updateAction(Action(ActionType.RUN, orientation))
        }
        translate(this.orientation.x * GameCamera.cameraSpeed, this.orientation.y*GameCamera.cameraSpeed)
    }
}