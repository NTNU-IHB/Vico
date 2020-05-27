package no.ntnu.ihb.acco.util

class Clock(
    private var autoStart: Boolean = true
) {

    private var startTime = 0L
    private var oldTime = 0L
    var elapsedTime_ = 0.0
        private set

    var running = false
        private set

    fun start() {
        startTime = System.currentTimeMillis()
        oldTime = startTime
        elapsedTime_ = 0.0
        running = true
    }

    fun stop() {
        getElapsedTime()
        running = false
        autoStart = false
    }

    fun getElapsedTime(): Double {
        getDelta()
        return elapsedTime_
    }

    fun getDelta(): Double {
        var diff = 0.0

        if (this.autoStart && !this.running) {
            start()
            return 0.0
        }

        if (this.running) {

            val newTime = System.currentTimeMillis()
            diff = (newTime - this.oldTime).toDouble() / 1000.0
            this.oldTime = newTime

            elapsedTime_ += diff

        }

        return diff
    }

}
