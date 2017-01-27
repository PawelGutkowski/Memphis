package memphis.game.core.actor

import memphis.game.core.Environment
import memphis.game.core.NamedAnimation
import memphis.game.core.event.ShotEvent
import memphis.game.not


class Projectile(animations: List<NamedAnimation>, environment: Environment, event: ShotEvent) : OrientedActor(animations, environment) {

    init {
        this.position.set(event.origin)
        this.orientation = event.orientation
    }

    var shooter : Actor = event.shooter

    override fun startRender() {
        super.startRender()
        val translated = translate(orientation.x * 10f, orientation.y * 10f)
        if(not(translated)){
            dispose()
        }
    }

    override fun canCollide(other: Item): Boolean {
        return super.canCollide(other) && other != shooter
    }
}