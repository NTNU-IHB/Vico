package no.ntnu.ihb.vico.util

interface ElementObserver<E> {

    fun onElementAdded(element: E)
    fun onElementRemoved(element: E)

}

class ObservableList<E>(
    private val list: MutableList<E>
) : MutableList<E> by list {

    private val observers: MutableList<ElementObserver<E>> = mutableListOf()

    override fun add(element: E): Boolean {
        return list.add(element).also { status ->
            if (status) observers.forEach { it.onElementAdded(element) }
        }
    }

    override fun remove(element: E): Boolean {
        return list.remove(element).also { status ->
            if (status) observers.forEach { it.onElementRemoved(element) }

        }
    }

    fun addObserver(observer: ElementObserver<E>) {
        observers.add(observer)
    }

    fun removeObserver(observer: ElementObserver<E>) {
        observers.remove(observer)
    }

}

class ObservableSet<E>(
    private val set: MutableSet<E>
) : MutableSet<E> by set {

    private val observers: MutableList<ElementObserver<E>> = mutableListOf()

    override fun add(element: E): Boolean {
        return set.add(element).also { status ->
            if (status) observers.forEach { it.onElementAdded(element) }
        }
    }

    override fun remove(element: E): Boolean {
        return set.remove(element).also { status ->
            if (status) observers.forEach { it.onElementRemoved(element) }

        }
    }

    fun addObserver(observer: ElementObserver<E>) {
        observers.add(observer)
    }

    fun removeObserver(observer: ElementObserver<E>) {
        observers.remove(observer)
    }

}
