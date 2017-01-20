package memphis.game.core

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import memphis.game.core.actor.Actor
import memphis.game.core.actor.Item


class Projectile(environment: Environment, position : Vector2, val speed : Float, val orientation: Item.Orientation) : Item(environment, position){
    fun render(spriteBatch: SpriteBatch, texture : Texture?){
        position.add(speed*orientation.x, speed*orientation.y)
        spriteBatch.draw(texture, position.x, position.y)
    }
}