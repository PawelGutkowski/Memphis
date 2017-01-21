package memphis.game.core

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import memphis.game.core.actor.Item.Orientation
import memphis.game.core.actor.OrientedActor


class GameCamera() : OrthographicCamera() {

    companion object {
        public val cameraSpeed = 4f
        private val thresholdFactor = 0.2f
        private val movements = listOf(
                CameraMovement(
                        { target, orientation, x, y -> target.x < x * threshold(Orientation.LEFT, orientation) },
                        { cam -> cam.translate(-cameraSpeed, 0f) }
                ),
                CameraMovement(
                        { target, orientation, x, y -> target.x > x * (1f - threshold(Orientation.RIGHT, orientation))},
                        { cam -> cam.translate(cameraSpeed, 0f) }
                ),
                CameraMovement(
                        { target, orientation, x, y -> target.y < y * 2 * threshold(Orientation.DOWN, orientation) },
                        { cam -> cam.translate(0f, -cameraSpeed) }
                ),
                CameraMovement(
                        { target, orientation, x, y -> target.y > y * (1f - (2 * threshold(Orientation.UP, orientation)))},
                        { cam -> cam.translate(0f, cameraSpeed) }
                )
        )

        fun threshold(orientation : Orientation, actorOrientation: Orientation) : Float {
            return thresholdFactor * if(orientation == actorOrientation) 2f else 1f
        }
    }

    fun follow(viewport: Viewport?, actor: OrientedActor?, screenWidth: Int, screenHeight: Int) {
        if(actor != null && viewport!= null){
            val target = viewport.project(actor.origin())
            if(target != null){
                movements.filter { it.condition.invoke(target, actor.orientation, screenWidth, screenHeight) }
                        .forEach { it.execution.invoke(this) }
            }
        }
    }

    private class CameraMovement(val condition: (Vector2, Orientation, Int, Int)->Boolean, val execution: (GameCamera)-> Unit)
}


