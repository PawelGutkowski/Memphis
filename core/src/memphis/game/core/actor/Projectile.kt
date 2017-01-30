package memphis.game.core.actor

import com.badlogic.gdx.Gdx
import memphis.game.core.Environment
import memphis.game.core.NamedAnimation
import memphis.game.core.event.ShotEvent
import memphis.game.not


class Projectile(animations: List<NamedAnimation>, environment: Environment, event: ShotEvent) : OrientedActor(animations, environment) {

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
            val translated = translate(orientation.x * 10f, orientation.y * 10f)
            if(not(translated)){
                updateAction(Action(ActionType.EXPLODE))
            }
        } else {
            explosionTime += Gdx.graphics.deltaTime
            if(currentAnimation.isAnimationFinished(explosionTime)){
                dispose()
            }
        }
    }

    override fun canCollide(other: Item): Boolean {
        return super.canCollide(other) && other != shooter && other !is Projectile
    }
}