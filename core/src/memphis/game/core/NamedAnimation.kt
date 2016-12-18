package memphis.game.core

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion


class NamedAnimation(val name : String, frameDuration : Float, keyFrames : Array<out TextureRegion>) : Animation(frameDuration, *keyFrames) {}