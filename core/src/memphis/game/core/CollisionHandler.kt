package memphis.game.core

import com.badlogic.gdx.math.Rectangle
import memphis.game.core.actor.Item

class CollisionHandler(val hasBase: Boolean, val newHitbox: Rectangle, val newBase : Rectangle) {

    val isColliding : (Item)->Boolean = if(hasBase) {
        { other ->
            if (other.hasBase()) {
                other.base().overlaps(newBase)
            } else {
                other.hitbox().overlaps(newHitbox)
            }
        }
    } else {
        { other -> other.hitbox().overlaps(newHitbox) }
    }

    val getRectangles : (Item, Item)->Pair<Rectangle, Rectangle> = if (hasBase) {
        { item, other ->
            if (other.hasBase()) {
                item.base() to other.base()
            } else {
                item.hitbox() to other.hitbox()
            }
        }
    } else {
        { item, other -> item.hitbox() to other.hitbox() }
    }
}