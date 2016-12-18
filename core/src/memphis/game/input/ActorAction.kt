package memphis.game.input

enum class ActorAction(continuous : Boolean, private val order : Int) {
    STAND(true, 0), GO_LEFT(true, 10), GO_DOWN(true, 10), GO_UP(true, 10), GO_RIGHT(true, 10), JUMP(false, 20), HIT(false, 20), BLOCK(false, 15)
}