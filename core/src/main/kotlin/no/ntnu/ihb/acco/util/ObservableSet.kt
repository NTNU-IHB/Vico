package no.ntnu.ihb.acco.util

interface SetObserver<E> {

    fun onElementAdded(element: E)

    fun onElementRemoved(element: E)

}

class ObservableSet<E>(
    private val set: MutableSet<E>
) : MutableSet<E> by set {

    var observer: SetObserver<E>? = null

    override fun add(element: E): Boolean {
        return set.add(element).also { added ->
            if (added) {
                observer?.onElementAdded(element)
            }
        }
    }

    override fun remove(element: E): Boolean {
        return set.remove(element).also { added ->
            if (added) {
                observer?.onElementRemoved(element)
            }
        }
    }

}
