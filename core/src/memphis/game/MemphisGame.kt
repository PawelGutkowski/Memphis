package memphis.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import memphis.game.assets.AnimationAsset
import memphis.game.assets.GameAssets
import memphis.game.core.Environment
import memphis.game.core.GameCamera
import memphis.game.core.actor.Actor
import memphis.game.core.actor.ActorFactory
import memphis.game.core.actor.Item
import memphis.game.core.actor.PlayableActor

class MemphisGame() : ApplicationAdapter() {
    var spriteBatch : SpriteBatch? = null
    var shapeRenderer: ShapeRenderer? = null

    var player: PlayableActor? = null
    var viewport: Viewport? = null
    var camera = GameCamera()
    var font : BitmapFont? = null
    var background : Texture? = null
    var actorFactory : ActorFactory? = null
    var environment : Environment? = null

    val crates : MutableList<Actor> = mutableListOf()

    companion object {
        val dmitriWidth = 26f
        val dmitriHeight = 48f
        //TODO: Will be extracted to json file
        val GAME_ASSETS = GameAssets(mapOf(
                "crate" to listOf(
                        AnimationAsset("idle", 24f, 23f, 10f)
                ),
                "pixel" to listOf (
                        AnimationAsset("run-side", dmitriWidth, dmitriHeight, 0.1f),
                        AnimationAsset("idle-side", dmitriWidth, dmitriHeight, 10f),
                        AnimationAsset("run-front", dmitriWidth, dmitriHeight, 0.1f),
                        AnimationAsset("idle-front", dmitriWidth, dmitriHeight, 10f),
                        AnimationAsset("run-back", dmitriWidth, dmitriHeight, 0.1f),
                        AnimationAsset("idle-back", dmitriWidth, dmitriHeight, 10f)
                ),
                "enemy" to listOf (
                        AnimationAsset("idle", 100f, 100f, 0.2f)
                ),
                "projectile" to listOf(
                        AnimationAsset("idle", 18f, 15f, 0.1f),
                        AnimationAsset("boom", 18f, 15f, 0.1f)
                )
        ))
    }
    override fun create() {
        spriteBatch = SpriteBatch()
        actorFactory = ActorFactory(GAME_ASSETS)
        val env = Environment(actorFactory ?: throw Exception("Actor Factory not initialized"))
        val templateWarrior = actorFactory?.createPixel(env) ?: throw Exception("Actor not created")

        Gdx.input.inputProcessor = templateWarrior
        player = templateWarrior
        env.registerItem(templateWarrior)

        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.update()
        camera.zoom = 0.5f
        viewport = StretchViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), camera)
        font = BitmapFont()
        font?.color = Color.WHITE
        background = Texture(Gdx.files.internal("background.png"))

        shapeRenderer = ShapeRenderer()
        shapeRenderer?.setAutoShapeType(true)

        this.environment = env
        addCrates(actorFactory)
    }

    private fun addCrates(crateFactory: ActorFactory?) {
        if (crateFactory != null) {
            for (i in 0..100) {
                val crate = crateFactory.createCrate(environment ?: throw IllegalStateException("Environment not created"))
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
        environment?.render(getBatch())
        spriteBatch?.end()

//        renderBoxes()
    }

    private fun renderBoxes() {
        //TODO: Gets messed up when windows is resized, why?
        shapeRenderer?.begin()
        renderBox(shapeRenderer, player, viewport)
        environment?.items?.forEach { renderBox(shapeRenderer, it, viewport) }
        shapeRenderer?.end()
    }

    private fun renderBox(shapeRenderer: ShapeRenderer?, item: Item?, viewport: Viewport?) {
        if(shapeRenderer != null && item != null && viewport!= null){
            renderBox(item.base(), shapeRenderer, viewport)
            val (leftLower, rightUpper) = renderBox(item.hitbox(), shapeRenderer, viewport)
            shapeRenderer.x(leftLower.x + ((rightUpper.x - leftLower.x)/2), leftLower.y, 4f)
        }
    }

    private fun renderBox(hitbox : Rectangle, shapeRenderer: ShapeRenderer, viewport: Viewport): Pair<Vector2, Vector2> {
        val leftLower = viewport.project(Vector2(hitbox.x, hitbox.y))
        val rightUpper = viewport.project(Vector2(hitbox.x + hitbox.width, hitbox.y+hitbox.height))
        shapeRenderer.rect(leftLower.x, leftLower.y, rightUpper.x - leftLower.x, rightUpper.y - leftLower.y)
        return Pair(leftLower, rightUpper)
    }

    private fun getBatch() = spriteBatch ?: throw IllegalStateException("SpriteBatch should already be initialized")

    override fun dispose() {
        spriteBatch?.dispose()
    }

    override fun resize(width: Int, height: Int) {
        viewport?.update(width, height)
        camera.update()
    }
}

fun not(value : Boolean) = !value