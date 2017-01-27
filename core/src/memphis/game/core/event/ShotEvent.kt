package memphis.game.core.event

import com.badlogic.gdx.math.Vector2
import memphis.game.core.actor.Actor
import memphis.game.core.actor.Item


data class ShotEvent(val origin: Vector2, val orientation: Item.Orientation, val shooter: Actor) : Event {
}