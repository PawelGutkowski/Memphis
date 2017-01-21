package memphis.game.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import memphis.game.core.actor.Item
import memphis.game.not


class Environment {

    val items : MutableList<Item> = mutableListOf()

    open fun registerItem(item : Item) = items.add(item)

    fun shoot(position: Vector2, orientation: Item.Orientation){
        registerItem(Projectile(this, position, 20f, orientation))
    }

    fun translate(item: Item, dX: Float, dY: Float) {
        val newBase = Rectangle(item.base().x +dX, item.base().y + dY, item.base().width, item.base().height)
        val colliding = items.find { it != item && it.base().overlaps(newBase) }

        if (colliding != null) {
            translateTo(item, colliding, dX/2, dY/2)
        } else {
            item.position.add(dX, dY)
        }
    }

    private fun translateTo(translated: Item, colliding: Item, dX : Float, dY: Float){
        if(not(dX + dY in -1f..1f)){
            val newBase = Rectangle(translated.base().x +dX, translated.base().y + dY, translated.base().width, translated.base().height)
            if(translated == colliding || not(colliding.base().overlaps(newBase))){
                translated.position.add(dX, dY)
            } else {
                translateTo(translated, colliding, dX/2, dY/2)
            }
        }
    }

    fun render(spriteBatch: SpriteBatch){
        items.sortedByDescending { it.position.y }.forEach { it.render(spriteBatch) }
    }
}