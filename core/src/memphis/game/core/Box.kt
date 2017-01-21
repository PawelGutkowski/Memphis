package memphis.game.core

import com.badlogic.gdx.math.Vector2

data class Box(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float
) {
    operator fun contains(vector : Vector2) : Boolean {
       return (vector.x in fromX..toX) && (vector.y in fromY..toY)
    }
}
