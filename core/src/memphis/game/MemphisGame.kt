package memphis.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import memphis.game.assets.AnimationAsset
import memphis.game.assets.GameAssets
import memphis.game.core.ActorFactory
import memphis.game.input.InGameInputProcessor

class MemphisGame() : ApplicationAdapter() {
    var spriteBatch : SpriteBatch? = null
    var player: TemplateWarrior? = null
    var viewport: Viewport? = null
    var camera = OrthographicCamera()
    var font : BitmapFont? = null

    companion object {
        val GAME_ASSETS = GameAssets(mapOf(
                "template" to listOf (
                        AnimationAsset("walk", "template/walk.png", "template/walk.json", 333f, 476f, 0.02f),
                        AnimationAsset("stand", "template/stand.png", "template/stand.png", 198f, 472f, 0.02f),
                        AnimationAsset("hit", "template/hit.png", "template/hit.png", 294f, 473f, 0.02f)
                )
        ))
    }

    override fun create() {
        spriteBatch = SpriteBatch()
        val actorFactory = ActorFactory(GAME_ASSETS)
        val templateWarrior = actorFactory.createTemplateWarrior()
        Gdx.input.inputProcessor = InGameInputProcessor(templateWarrior)
        player = templateWarrior
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.update()
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)
        font = BitmapFont()
        font?.color = Color.WHITE
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        spriteBatch?.transformMatrix = camera.view
        spriteBatch?.projectionMatrix = camera.projection

        spriteBatch?.begin()
        /*font?.draw(spriteBatch, viewport?.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).toString(), 400f, 100f)*/
        player?.render(spriteBatch ?: throw IllegalStateException("SpriteBatch should already be initialized"))
        spriteBatch?.end()
    }

    override fun dispose() {
        spriteBatch?.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport?.update(width, height)
    }
}
/*
fun TextureRegion.isFirst() : Boolean = MathUtils.isEqual(this.u, 0f) && MathUtils.isEqual(this.v, 0f)
*/
