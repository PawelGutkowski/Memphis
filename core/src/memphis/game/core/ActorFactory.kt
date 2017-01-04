package memphis.game.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import memphis.game.PixelMan
import memphis.game.TemplateWarrior
import memphis.game.assets.GameAssets
import memphis.game.assets.MissingAssetException


class ActorFactory(gameAssets: GameAssets) {

    val animations : MutableMap<String, List<NamedAnimation>> = mutableMapOf()

    init {
        gameAssets.animations.forEach {
            val sources : List<NamedAnimation> = it.value.map { animation ->
                val size = Vector2(animation.width, animation.height)
                val texture = Texture(Gdx.files.internal("${it.key}/${animation.name}.png"))
                val splitTextureMatrix = TextureRegion.split(texture, size.x.toInt(), size.y.toInt())
                val animationFrames : MutableList<TextureRegion> = mutableListOf()
                splitTextureMatrix.forEach { it.forEach { animationFrames.add(it) } }
                NamedAnimation(animation.name, animation.frameDuration, animationFrames.toTypedArray())
            }
            animations.put(it.key, sources)
        }
    }

    fun createPixel() : Actor {
        return PixelMan(animations["pixel"] ?: throw MissingAssetException("Missing asset: animations from group template"))
    }
}