package info.laht.krender.util

import java.util.*

open class RenderContext {

    private val tasks: Queue<() -> Unit> = ArrayDeque()

    @Synchronized
    fun invokeLater(task: () -> Unit) {
        tasks.add(task)
    }

    @Synchronized
    fun invokePendingTasks() {
        while (!tasks.isEmpty()) {
            tasks.poll().invoke()
        }
    }

}
