package memphis.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import memphis.game.assets.AnimationAsset
import memphis.game.assets.GameAssets
import memphis.game.core.Actor
import memphis.game.core.ActorFactory

class MemphisGame() : ApplicationAdapter() {
    var spriteBatch : SpriteBatch? = null
    var player: Actor? = null
    var viewport: Viewport? = null
    var camera = OrthographicCamera()
    var font : BitmapFont? = null

    var potion : Texture? = null

    companion object {
        //TODO: Will be extracted to json file
        val GAME_ASSETS = GameAssets(mapOf(
                "pixel" to listOf (
                        AnimationAsset("run", 50f, 60f, 0.1f),
                        AnimationAsset("idle", 50f, 60f, 10f)
                )
        ))
    }

    override fun create() {
        spriteBatch = SpriteBatch()
        val actorFactory = ActorFactory(GAME_ASSETS)
        val templateWarrior = actorFactory.createPixel()
        Gdx.input.inputProcessor = templateWarrior
        player = templateWarrior
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.update()
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)
        font = BitmapFont()
        font?.color = Color.WHITE
        potion = Texture(Gdx.files.internal("redpotion.png"))
    }

    override fun render() {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 0.3f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        spriteBatch?.transformMatrix = camera.view
        spriteBatch?.projectionMatrix = camera.projection

        spriteBatch?.begin()
        /*font?.draw(spriteBatch, viewport?.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).toString(), 400f, 100f)*/
        player?.render(spriteBatch ?: throw IllegalStateException("SpriteBatch should already be initialized"))
        spriteBatch?.draw(potion, 200f, 200f, 100f, 100f)
        spriteBatch?.end()
    }

    override fun dispose() {
        spriteBatch?.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport?.update(width, height)
    }
}