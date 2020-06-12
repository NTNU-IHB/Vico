package no.ntnu.ihb.acco.util

import java.util.function.Predicate


interface NodeTraverser<E> {

    enum class TraverseOption {
        DEPTH_FIRST, BREADTH_FIRST
    }

    fun accept(data: E)

}

class Node<E>(
    val data: E
) {

    var parent: Node<E>? = null

    val level: Int
        get() {
            return if (isRoot()) 0 else parent!!.level + 1
        }


    private val children: MutableList<Node<E>> = mutableListOf()
    private val decendants: MutableList<Node<E>> = mutableListOf()

    fun hasParent() = parent != null

    fun isRoot(): Boolean {
        return parent == null
    }

    fun isLeaf(): Boolean {
        return children.isEmpty()
    }

    fun childCount(): Int {
        return children.size
    }

    fun add(child: Node<E>) = apply {

        if (child == this) {
            throw IllegalArgumentException("Adding self as child!")
        }
        if (isAncestor(child)) {
            throw IllegalArgumentException("Adding ancestor as child!")
        }

        if (isDecendant(child)) {
            throw IllegalArgumentException("Adding decendant as child!")
        }


        children.add(child)
        child.parent = this
    }

    fun remove(child: Node<E>) = apply {
        if (children.remove(child)) {
            child.parent = null
        }
    }

    fun isAncestor(node: Node<E>): Boolean {
        var current = parent
        while (current != null) {
            if (node === current) {
                return true
            }
            current = current.parent
        }
        return false
    }

    fun isDecendant(node: Node<E>): Boolean {
        for (child in children) {
            return if (child === node) {
                true
            } else child.isDecendant(node)
        }
        return false
    }

    /*fun traverseDecendants(traverser: NodeTraverser<E>, option: TraverseOption?) {
        when (option) {
            TraverseOption.BREADTH_FIRST -> breadthFirstTraversal(traverser)
            TraverseOption.DEPTH_FIRST -> depthFirstTraversal(traverser)
        }
    }*/

    /*fun depthFirstTraversal(traverser: NodeTraverser<E>) {
        traverser.accept(data)
        for (child in children) {
            child.depthFirstTraversal(traverser)
        }
    }

    fun breadthFirstTraversal(traverser: NodeTraverser<E>) {
        traverser.accept(data)
        val queue = ArrayDeque(children)
        while (!queue.isEmpty()) {
            val child: Node<E> = queue.remove() as Node<E>
            traverser.accept(child.data)
            child.children.forEach(queue::add)
        }
    }*/

    /*fun findInAncestors(predicate: Predicate<E>): E? {
        var current = this
        do {
            if (predicate.test(current.getData())) {
                return current.getData()
            }
        } while (current.getParent().also({ current = it }) != null)
        return null
    }

    fun findAllInAncestors(predicate: Predicate<E>): List<E>? {
        val result: MutableList<E> = ArrayList()
        var current = this
        do {
            if (predicate.test(current.getData())) {
                result.add(current.getData())
            }
        } while (current.getParent().also({ current = it }) != null)
        return result
    }*/

    private fun findInCollection(collection: List<Node<E>>, predicate: Predicate<E>): E? {
        return collection.map { it.data }.find { predicate.test(it) }
    }

    fun findInChildren(predicate: Predicate<E>): E? {
        return findInCollection(children, predicate)
    }

    fun findInDecendants(predicate: Predicate<E>): E? {
        return findInCollection(decendants, predicate)
    }

    /*fun findAllInChildren(predicate: Predicate<E>): List<E> {
        val collect = mutableListOf<E>()
        if (predicate.test(data)) {
            collect.add(data)
        }
        children.forEach { child ->
            if (predicate.test(child.data)) {
                collect.add(child.data)
            }
        }
        return collect
    }

    fun findAllInDecendants(predicate: Predicate<E>): List<E>? {
        val collect = mutableListOf<E>()
        if (predicate.test(data)) {
            collect.add(data)
        }
        decendants.forEach { child ->
            if (predicate.test(child.data)) {
                collect.add(child.data)
            }
        }
        return collect
    }*/

}
