package no.ntnu.ihb.acco.core

fun interface EntityTraverser {

    fun traverse(entity: Entity)

}

enum class TraverseOption {
    DEPTH_FIRST, BREADTH_FIRST
}
