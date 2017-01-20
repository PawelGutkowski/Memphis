package memphis.game.core

import com.badlogic.gdx.math.Vector2
import memphis.game.core.actor.Item


class Environment {

    val items : MutableList<Item> = mutableListOf()

    open fun registerItem(item : Item) = items.add(item)

    fun shoot(position: Vector2, orientation: Item.Orientation){
        registerItem(Projectile(this, position, 20f, orientation))
    }

    fun translate(item: Item, dX: Float, dY: Float) {
        val newPosition = Vector2(item.position.x + dX, item.position.y + dY)
        var collision = false
        items.forEach {
            collision = collision || it != item && newPosition in it.hitbox()
        }
        if(!collision) item.position.set(newPosition)
    }
}