package no.ntnu.ihb.vico.render.util

import java.util.*

open class RenderContext {

    private val tasks: Queue<() -> Unit> = ArrayDeque()

    @Synchronized
    fun invokeLater(task: () -> Unit) {
        tasks.add(task)
    }

    @Synchronized
    fun invokePendingTasks() {
        val t0 = System.currentTimeMillis()
        while (!tasks.isEmpty()) {
            tasks.poll()?.invoke()
            val t = System.currentTimeMillis() - t0
            if (t > 25) {
                tasks.clear()
                break
            }
        }
    }

}
