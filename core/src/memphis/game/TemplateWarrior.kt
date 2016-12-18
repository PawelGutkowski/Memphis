package memphis.game

import memphis.game.core.Actor
import memphis.game.core.NamedAnimation
import memphis.game.input.InGameAction

class TemplateWarrior(animations : List<NamedAnimation>) : Actor(animations){
    override val actions: MutableSet<InGameAction> = mutableSetOf(InGameAction.STAND)
}