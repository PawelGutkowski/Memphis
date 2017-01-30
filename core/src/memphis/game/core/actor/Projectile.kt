package memphis.game.core.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.NamedAnimation
import memphis.game.core.event.ShotEvent
import memphis.game.not


class Projectile(animations: List<NamedAnimation>, environment: Environment, event: ShotEvent) : UniOrientedActor(animations, environment), Destructible {

    override fun destroy() {
        updateAction(Action(ActionType.DISPOSE))
    }

    init {
        this.position.set(event.origin)
        this.orientation = event.orientation
    }

    override val baseType: BaseType = BaseType.NONE

    var shooter : Actor = event.shooter

    var explosionTime = 0f

    override fun startRender() {
        super.startRender()
        if(action.type == ActionType.IDLE){
            translate()
        } else if(action.type == ActionType.DISPOSE) {
            explosionTime += Gdx.graphics.deltaTime
            if(currentAnimation.isAnimationFinished(explosionTime)){
                dispose()
            }
        }
    }

    override fun canCollide(other: Item): Boolean {
        return super.canCollide(other) && other != shooter && other !is Projectile
    }

    override fun updateBase(size: Vector2) {
        base.x = size.x/6
        base.y = size.y/3
    }

    override fun hitbox() = Rectangle (
            position.x-(base.x/2), position.y + (size.y*0.2f), base.x, (size.y * 0.6f)
    )

    override fun speed(): Float = GameCamera.cameraSpeed * 1.5f
}