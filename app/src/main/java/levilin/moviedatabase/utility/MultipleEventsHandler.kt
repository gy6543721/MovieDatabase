package levilin.moviedatabase.utility


internal interface MultipleEventsHandler {
    fun processEvent(event: () -> Unit)
    companion object
}

internal fun MultipleEventsHandler.Companion.get(): MultipleEventsHandler =
    MultipleEventsHandlerImplement()

private class MultipleEventsHandlerImplement : MultipleEventsHandler {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 100L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}