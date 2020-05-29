package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.math.Euler
import no.ntnu.ihb.acco.math.Matrix4
import no.ntnu.ihb.acco.math.Quaternion
import no.ntnu.ihb.acco.math.Vector3


val Entity.transform
    get() = getComponent(TransformComponent::class.java)


class TransformComponent : Component {

    var parent: TransformComponent? = null
    val children: MutableList<TransformComponent> = mutableListOf<TransformComponent>()

    var up = defaultUp.clone()

    val position: Vector3 = Vector3()
    val rotation: Euler = Euler().also {
        it.onChangeCallback = { onRotationChange() }
    }
    val quaternion: Quaternion = Quaternion().also {
        it.onChangeCallback = { onQuaternionChange() }
    }
    val scale: Vector3 = Vector3(1.0, 1.0, 1.0)

    val matrix: Matrix4 = Matrix4()
    val matrixWorld: Matrix4 = Matrix4()

    var matrixAutoUpdate: Boolean = true
    var matrixWorldNeedsUpdate: Boolean = true

    private fun onRotationChange() {
        quaternion.setFromEuler(rotation, false)
    }

    private fun onQuaternionChange() {
        rotation.setFromQuaternion(quaternion, null, false)
    }

    /**
     * This updates the position, rotation and scale with the matrix.
     */
    fun applyMatrix4(matrix: Matrix4) {
        if (this.matrixAutoUpdate) {
            this.updateMatrix()
        }

        this.matrix.premultiply(matrix)

        this.matrix.decompose(this.position, this.quaternion, this.scale)
    }

    fun applyQuaternion(q: Quaternion): TransformComponent {
        this.quaternion.premultiply(q)

        return this
    }

    fun setRotationFromAxisAngle(axis: Vector3, angle: Double) {
        // assumes axis is normalized
        this.quaternion.setFromAxisAngle(axis, angle)
    }

    fun setRotationFromEuler(euler: Euler) {
        this.quaternion.setFromEuler(euler, true)
    }

    fun setRotationFromMatrix(m: Matrix4) {
        // assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)
        this.quaternion.setFromRotationMatrix(m)
    }

    fun setRotationFromQuaternion(q: Quaternion) {
        // assumes q is normalized
        this.quaternion.copy(q)
    }

    /**
     * Rotate an object along an axis in object space. The axis is assumed to be normalized.
     * @param axis    A normalized vector in object space.
     * @param angle    The angle in radians.
     */
    fun rotateOnAxis(axis: Vector3, angle: Double): TransformComponent {
        // rotate object on axis in object space
        // axis is assumed to be normalized
        val q1 = Quaternion()
        q1.setFromAxisAngle(axis, angle)
        this.quaternion.multiply(q1)

        return this
    }

    /**
     * Rotate an object along an axis in world space. The axis is assumed to be normalized. Method Assumes no rotated parent.
     * @param axis    A normalized vector in object space.
     * @param angle    The angle in radians.
     */
    fun rotateOnWorldAxis(axis: Vector3, angle: Double): TransformComponent {
        // rotate object on axis in world space
        // axis is assumed to be normalized
        // method assumes no rotated parent
        val q1 = Quaternion()
        q1.setFromAxisAngle(axis, angle)
        this.quaternion.premultiply(q1)

        return this
    }

    /**
     *
     * @param angle
     */
    fun rotateX(angle: Double): TransformComponent {
        return this.rotateOnAxis(Vector3.X, angle)
    }

    /**
     *
     * @param angle
     */
    fun rotateY(angle: Double): TransformComponent {
        return this.rotateOnAxis(Vector3.Y, angle)
    }

    /**
     *
     * @param angle
     */
    fun rotateZ(angle: Double): TransformComponent {
        return this.rotateOnAxis(Vector3.Z, angle)
    }

    /**
     * @param axis    A normalized vector in object space.
     * @param distance    The distance to translate.
     */
    fun translateOnAxis(axis: Vector3, distance: Double): TransformComponent {
        val v1 = Vector3()
        v1.copy(axis).applyQuaternion(this.quaternion)
        this.position.add(v1.multiplyScalar(distance))

        return this
    }

    /**
     * Translates object along x axis by distance.
     * @param distance Distance.
     */
    fun translateX(distance: Double): TransformComponent {
        return this.translateOnAxis(Vector3.X, distance)
    }

    /**
     * Translates object along y axis by distance.
     * @param distance Distance.
     */
    fun translateY(distance: Double): TransformComponent {
        return this.translateOnAxis(Vector3.Y, distance)
    }

    /**
     * Translates object along z axis by distance.
     * @param distance Distance.
     */
    fun translateZ(distance: Double): TransformComponent {
        return this.translateOnAxis(Vector3.Z, distance)
    }

    /**
     * Updates the vector from local space to world space.
     * @param vector A local vector.
     */
    fun localToWorld(vector: Vector3): Vector3 {
        return vector.applyMatrix4(this.matrixWorld)
    }

    /**
     * Updates the vector from world space to local space.
     * @param vector A world vector.
     */
    fun worldToLocal(vector: Vector3): Vector3 {
        if (parent == null) return vector
        return vector.applyMatrix4(Matrix4().getInverse(this.matrixWorld))
    }

    /**
     * Rotates object to face point in space.
     * @param v A world vector to look at.
     */
    fun lookAt(v: Vector3) {
        lookAt(v.x, v.y, v.z)
    }

    /**
     * Rotates object to face point in space.
     */
    fun lookAt(x: Double, y: Double, z: Double) {
        // This method does not support objects having non-uniformly-scaled parent(s)

        val q1 = Quaternion()
        val m1 = Matrix4()
        val target = Vector3()
        val position = Vector3()

        target.set(x, y, z)

        val parent = this.parent

        this.updateWorldMatrix(true, false)

        position.setFromMatrixPosition(this.matrixWorld)

        /* if (this is Camera || this is Light) {
             m1.lookAt(position, target, this.up)
         } else {*/
        m1.lookAt(target, position, this.up)
        //}

        this.quaternion.setFromRotationMatrix(m1)

        if (parent != null) {

            m1.extractRotation(parent.matrixWorld)
            q1.setFromRotationMatrix(m1)
            this.quaternion.premultiply(q1.inverse())

        }

    }

    /**
     * Adds object as child of TransformComponent object.
     */
    fun add(`object`: TransformComponent): TransformComponent {

        `object`.parent?.remove(`object`)
        `object`.parent = this
        children.add(`object`)

        return this
    }

    /**
     * Adds object as child of TransformComponent object.
     */
    fun add(vararg objects: TransformComponent): TransformComponent {

        objects.forEach { add(it) }

        return this
    }


    /**
     * Removes object as child of TransformComponent object.
     */
    fun remove(`object`: TransformComponent): TransformComponent {

        if (children.remove(`object`)) {
            `object`.parent = null
            //  `object`.dispatchEvent("removed", this)
        }

        return this
    }

    /**
     * Removes object as child of TransformComponent object.
     */
    fun remove(vararg objects: TransformComponent): TransformComponent {

        objects.forEach { remove(it) }

        return this
    }

    /**
     * Adds object as a child of TransformComponent, while maintaining the object's world transform.
     */
    fun attach(`object`: TransformComponent): TransformComponent {

        val m = Matrix4()

        this.updateWorldMatrix(updateParents = true, updateChildren = false)

        m.getInverse(this.matrixWorld)

        `object`.parent?.also { parent ->
            parent.updateWorldMatrix(updateParents = true, updateChildren = false)

            m.multiply(parent.matrixWorld)
        }

        `object`.applyMatrix4(m)
        `object`.updateWorldMatrix(updateParents = false, updateChildren = false)

        this.add(`object`)

        return this
    }

    fun getWorldPosition(target: Vector3 = Vector3()): Vector3 {
        this.updateMatrixWorld(true)
        return target.setFromMatrixPosition(this.matrixWorld)
    }

    fun getWorldQuaternion(target: Quaternion = Quaternion()): Quaternion {
        this.updateMatrixWorld(true)

        this.matrixWorld.decompose(position, target, scale)

        return target
    }

    fun getWorldScale(target: Vector3 = Vector3()): Vector3 {
        this.updateMatrixWorld(true)

        this.matrixWorld.decompose(position, quaternion, target)

        return target
    }

    fun getWorldDirection(target: Vector3): Vector3 {
        this.updateMatrixWorld(true)

        val e = this.matrixWorld.elements

        return target.set(e[8], e[9], e[10]).normalize()
    }

    fun traverse(callback: (TransformComponent) -> Unit) {
        callback(this)
        children.forEach { child ->
            child.traverse(callback)
        }
    }

    /*fun traverseVisible(callback: (TransformComponent) -> Unit) {
        if (!this.visible) {
            return
        }
        callback(this)
        children.forEach {
            it.traverseVisible(callback)
        }
    }*/

    fun traverseAncestors(callback: (TransformComponent) -> Unit) {
        parent?.also {
            callback(it)
            it.traverseAncestors(callback)
        }
    }


    /**
     * Updates local transform.
     */
    fun updateMatrix() {
        this.matrix.compose(this.position, this.quaternion, this.scale)
        this.matrixWorldNeedsUpdate = true
    }

    /**
     * Updates global transform of the object and its children.
     */
    fun updateMatrixWorld(force: Boolean = false) {

        if (this.matrixAutoUpdate) {
            this.updateMatrix()
        }

        @Suppress("NAME_SHADOWING")
        var force = force
        val parent = this.parent

        if (this.matrixWorldNeedsUpdate || force) {

            if (parent == null) {
                this.matrixWorld.copy(this.matrix)
            } else {
                this.matrixWorld.multiplyMatrices(parent.matrixWorld, this.matrix)
            }

            this.matrixWorldNeedsUpdate = false
            force = true

        }

        this.children.forEach {
            it.updateMatrixWorld(force)
        }

    }

    fun updateWorldMatrix(updateParents: Boolean = false, updateChildren: Boolean = false) {

        val parent = this.parent

        if (updateParents && parent != null) {
            parent.updateWorldMatrix(updateParents = true, updateChildren = false)
        }

        if (this.matrixAutoUpdate) {
            this.updateMatrix()
        }

        if (parent == null) {
            this.matrixWorld.copy(this.matrix)
        } else {
            this.matrixWorld.multiplyMatrices(parent.matrixWorld, this.matrix)
        }

        if (updateChildren) {
            this.children.forEach {
                it.updateWorldMatrix(updateParents = false, updateChildren = true)
            }
        }

    }

    companion object {
        var defaultUp = Vector3.Y.clone()
    }

}
