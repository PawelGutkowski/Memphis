package memphis.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import memphis.game.assets.AnimationAsset
import memphis.game.assets.GameAssets
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.Projectile
import memphis.game.core.actor.Actor
import memphis.game.core.actor.ActorFactory
import memphis.game.core.actor.PlayableActor

class MemphisGame() : ApplicationAdapter() {
    var spriteBatch : SpriteBatch? = null

    var player: PlayableActor? = null
    var viewport: Viewport? = null
    var camera = GameCamera()
    var font : BitmapFont? = null
    var potion : Texture? = null
    var background : Texture? = null
    var actorFactory : ActorFactory? = null
    var environment : Environment? = null

    val enemies : MutableList<Actor> = mutableListOf()
    val crates : MutableList<Actor> = mutableListOf()
    val potions : MutableList<Projectile>  = mutableListOf()
    var time = 0f

    companion object {
        //TODO: Will be extracted to json file
        val GAME_ASSETS = GameAssets(mapOf(
                "crate" to listOf(
                        AnimationAsset("idle", 24f, 23f, 10f)
                ),
                "pixel" to listOf (
                        AnimationAsset("run-side", 50f, 60f, 0.1f),
                        AnimationAsset("idle-side", 50f, 60f, 10f),
                        AnimationAsset("run-front", 50f, 60f, 0.1f),
                        AnimationAsset("idle-front", 50f, 60f, 10f),
                        AnimationAsset("run-back", 50f, 60f, 0.1f),
                        AnimationAsset("idle-back", 50f, 60f, 10f)
                ),
                "enemy" to listOf (
                        AnimationAsset("idle", 100f, 100f, 0.2f)
                )
        ))

        val ENEMY_DISTANCE = 300f
    }
    override fun create() {
        spriteBatch = SpriteBatch()
        val env = Environment()
        actorFactory = ActorFactory(GAME_ASSETS, env)
        val templateWarrior = actorFactory?.createPixel() ?: throw Exception("Actor not created")

        Gdx.input.inputProcessor = templateWarrior
        player = templateWarrior
        env.registerItem(templateWarrior)

        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.update()
        camera.zoom = 0.5f
        viewport = StretchViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)
        font = BitmapFont()
        font?.color = Color.WHITE
        potion = Texture(Gdx.files.internal("redpotion.png"))
        background = Texture(Gdx.files.internal("background.png"))

        this.environment = env
        addCrates(actorFactory)
    }

    private fun addCrates(crateFactory: ActorFactory?) {
        if (crateFactory != null) {
            for (i in 0..100) {
                val crate = crateFactory.createCrate()
                crates.add(crate)
            }
        }
        crates.forEach {
            it.position.set(MathUtils.random(0f, 1000f), MathUtils.random(0f, 1000f))
            environment?.registerItem(it)
        }
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.follow(viewport, player, Gdx.graphics.width, Gdx.graphics.height)
        camera.update()

        spriteBatch?.transformMatrix = camera.view
        spriteBatch?.projectionMatrix = camera.projection

        spriteBatch?.begin()
//        spriteBatch?.draw(background, 0f, 0f, 1000f, 1000f)
//        font?.draw(spriteBatch, viewport?.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).toString(), 400f, 100f)
        potions.forEach { it.render(getBatch(), potion) }
        enemies.forEach{ it.render(getBatch()) }
        crates.forEach { it.render(getBatch()) }
        player?.render(getBatch())
        spriteBatch?.end()
    }

    private fun getBatch() = spriteBatch ?: throw IllegalStateException("SpriteBatch should already be initialized")

    override fun dispose() {
        spriteBatch?.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport?.update(width, height)
    }
}

fun not(value : Boolean) = !value