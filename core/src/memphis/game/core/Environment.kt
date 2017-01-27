package memphis.game.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
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

    fun translate(item: Item, dX: Float, dY: Float): Boolean {
        val newBase = Rectangle(item.base().x +dX, item.base().y + dY, item.base().width, item.base().height)
        val colliding = items.find { item.canCollide(it) && it.base().overlaps(newBase) }

        if (colliding != null) {
            return translateTo(item, colliding, dX/2, dY/2)
        } else {
            item.position.add(dX, dY)
            return true
        }
    }

    private fun translateTo(translated: Item, colliding: Item, dX : Float, dY: Float): Boolean {
        if(not(dX + dY in -1f..1f)){
            val newBase = Rectangle(translated.base().x +dX, translated.base().y + dY, translated.base().width, translated.base().height)
            if(translated == colliding || not(colliding.base().overlaps(newBase))){
                translated.position.add(dX, dY)
                return true
            } else {
                return translateTo(translated, colliding, dX/2, dY/2)
            }
        } else return false
    }

    fun render(spriteBatch: SpriteBatch){
        items.removeAll(Item::disposed)
        items.sortedByDescending { it.position.y }.forEach { it.render(spriteBatch) }
    }
}