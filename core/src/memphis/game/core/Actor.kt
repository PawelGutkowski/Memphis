package memphis.game.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import memphis.game.input.InGameAction
import memphis.game.isFirst

abstract class Actor(val animations : List<NamedAnimation>) {

    enum class Orientation(val scaleX: Float) { LEFT(-1f), RIGHT(1f) }

    open val position: Vector2 = Vector2(100f, 10f)

    open val size: Vector2 = Vector2.Zero

    open val actions : MutableSet<InGameAction> = mutableSetOf()

    var currentAnimation : NamedAnimation? = null

    var currentFrame : TextureRegion? = null

    var orientation = Orientation.RIGHT

    private var stateTime = 0f

    fun render(batch: SpriteBatch){
        stateTime += Gdx.graphics.deltaTime
        if(currentAnimation == null){
            currentAnimation = animations.find { it.name == "stand" } ?: throw Exception("No animation for state stand")
        }
        if(currentAnimation?.breakable ?: false || currentFrame?.isFirst() ?: false){
            if(actions.contains(InGameAction.GO_RIGHT) && actions.contains(InGameAction.GO_LEFT)){
                actions.remove(InGameAction.GO_RIGHT)
                actions.remove(InGameAction.GO_LEFT)
            } else if(actions.contains(InGameAction.GO_LEFT)){
                orientation = Orientation.LEFT
                currentAnimation = animations.find { it.name == "walk" }
            } else if(actions.contains(InGameAction.GO_RIGHT)){
                currentAnimation = animations.find { it.name == "walk" }
                orientation = Orientation.RIGHT
            } else {
                currentAnimation = animations.find { it.name == "stand"}
            }
        }
        if(currentAnimation?.name == "walk"){
            position.add(orientation.scaleX * 7f, 0f)
        }
        if(actions.contains(InGameAction.HIT)){
            currentAnimation = animations.find {it.name == "hit"}
        }
        currentFrame = currentAnimation?.getKeyFrame(stateTime, true)
        if(currentFrame != null) {
            size.set (
                    currentFrame?.regionWidth?.toFloat() ?: 0f,
                    currentFrame?.regionHeight?.toFloat() ?: 0f
            )
            if(orientation == Orientation.LEFT){
                currentFrame?.flip(true, false)
            }
            batch.draw (
                    currentFrame,
                    //position, bottom left corner
                    position.x - (size.x/2),
                    position.y
            )
            if(currentFrame?.isFlipX ?: false){
                currentFrame?.flip(true, false)
            }
        }
    }
}