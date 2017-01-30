package memphis.game.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import memphis.game.core.actor.ActorFactory
import memphis.game.core.actor.Item
import memphis.game.core.event.ShotEvent
import memphis.game.not


class Environment (val actorFactory: ActorFactory){

    val items : MutableList<Item> = mutableListOf()

    open fun registerItem(item : Item) = items.add(item)

    fun shoot(event: ShotEvent){
        registerItem(actorFactory.createProjectile(this, event))
    }

    /**
     * Based items collide by bases, if iteration include baseless object, it collides by hitbox
     */
    fun translate(item: Item, dX: Float, dY: Float): Boolean {

        val collisionHandler = CollisionHandler(
                item.hasBase(),
                translate(item.hitbox(), dX, dY),
                translate(item.base(), dX, dY)
        )

        val colliding = items.find { item.canCollide(it) && it.canCollide(item) && collisionHandler.isColliding(it)}
        if (colliding != null) {
            val (rectangle, rectangle2) = collisionHandler.getRectangles(item, colliding)
            val translation = translateTo(rectangle, rectangle2, dX, dY)
            if(translation != null){
                item.position.add(translation)
                return true
            } else {
                item.collide(colliding)
                colliding.collide(item)
                return false
            }
        } else {
            item.position.add(dX, dY)
            return true
        }
    }

    private fun translateTo(translated: Rectangle, colliding: Rectangle, dX : Float, dY: Float): Vector2? {
        if (dX + dY in -1f..1f){
            return null
        } else {
            val newRectangle = Rectangle(translated.x +dX, translated.y + dY, translated.width, translated.height)
            if(not(colliding.overlaps(newRectangle))){
                return Vector2(dX, dY)
            } else {
                return translateTo(translated, colliding, dX/2, dY/2)
            }
        }
    }

    private fun translate(rect: Rectangle, dX : Float, dY: Float) = Rectangle(rect.x +dX, rect.y + dY, rect.width, rect.height)

    fun render(spriteBatch: SpriteBatch){
        items.removeAll(Item::disposed)
        items.sortedByDescending { it.position.y }.forEach { it.render(spriteBatch) }
    }
}