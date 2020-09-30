package info.laht.krender.jme.extra

import com.jme3.input.RawInputListener
import com.jme3.input.event.*

internal abstract class RawInputAdapter : RawInputListener {
    override fun beginInput() {}
    override fun endInput() {}
    override fun onJoyAxisEvent(evt: JoyAxisEvent) {}
    override fun onJoyButtonEvent(evt: JoyButtonEvent) {}
    override fun onMouseMotionEvent(evt: MouseMotionEvent) {}
    override fun onMouseButtonEvent(evt: MouseButtonEvent) {}
    override fun onKeyEvent(evt: KeyInputEvent) {}
    override fun onTouchEvent(evt: TouchEvent) {}
}
