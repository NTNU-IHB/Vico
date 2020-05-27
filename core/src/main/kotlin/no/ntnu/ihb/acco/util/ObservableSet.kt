package no.ntnu.ihb.acco.util

class ObservableSet<E>(
    private val set: MutableSet<E>
) : MutableSet<E> by set {

    var addObserver: ((E) -> Unit)? = null
    var removeObserver: ((E) -> Unit)? = null

    override fun add(element: E): Boolean {
        return set.add(element).also {
            if (it) addObserver?.invoke(element)
        }
    }

    override fun remove(element: E): Boolean {
        return set.remove(element).also {
            if (it) removeObserver?.invoke(element)
        }
    }

}
