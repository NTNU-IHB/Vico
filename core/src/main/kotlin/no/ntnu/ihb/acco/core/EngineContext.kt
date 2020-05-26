package no.ntnu.ihb.acco.core

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

internal class EngineContext internal constructor(
    private val initialized: AtomicBoolean
) {

    private val queue: Queue<() -> Unit> = ArrayDeque()

    fun safeContext(task: () -> Unit) {
        if (initialized.get()) {
            queue.add(task)
        } else {
            task.invoke()
        }
    }

    internal fun emptyQueue() {
        while (!queue.isEmpty()) {
            queue.poll().invoke()
        }
    }

}
