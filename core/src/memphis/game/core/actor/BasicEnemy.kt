package memphis.game.core.actor

import memphis.game.core.Environment
import memphis.game.core.NamedAnimation


class BasicEnemy(animations: List<NamedAnimation>, environment: Environment) : OrientedActor(animations, environment), Destructible  {
    override fun destroy() {
        dispose()
    }
}