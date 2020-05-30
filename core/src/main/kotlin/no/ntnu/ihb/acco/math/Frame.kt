package no.ntnu.ihb.acco.math

import org.joml.*


open class Frame {

    private var parent: Frame? = null
    private val children = mutableListOf<Frame>()

    private val localMatrix = Matrix4d()
    private val worldMatrix = Matrix4d()

    private var dirty = true

    val hasParent: Boolean
        get() = parent != null


    fun makeDirty() {
        dirty = true
        children.forEach { it.makeDirty() }
    }

    private fun setParent(parent: Frame?) {
        if (hasParent) {
            val w = getWorldMatrix(Matrix4d())
            this.parent = parent
            setLocalTransform(worldToLocal(w))
        } else {
            this.parent = parent
            makeDirty()
        }
    }

    fun add(child: Frame) {
        children.add(child)
        child.setParent(this)
    }

    fun attach(child: Frame) {
        val worldTransform = child.getWorldMatrix()
        add(child)
        child.setTransform(worldTransform)
    }

    fun remove(child: Frame) {
        if (children.remove(child)) {
            child.setParent(null)
        }
    }

    @JvmOverloads
    fun getLocalMatrix(store: Matrix4d = Matrix4d()): Matrix4d {
        return store.set(localMatrix)
    }

    @JvmOverloads
    fun getWorldMatrix(store: Matrix4d = Matrix4d()): Matrix4d {
        if (dirty) {
            updateWorldMatrix()
        }
        return store.set(worldMatrix)
    }

    private fun updateWorldMatrix() {
        if (hasParent) {
            parent!!.getWorldMatrix(worldMatrix).mul(localMatrix)
        } else {
            worldMatrix.set(localMatrix)
        }
        dirty = false
    }

    @JvmOverloads
    fun getLocalTranslation(store: Vector3d = Vector3d()): Vector3d {
        return localMatrix.getTranslation(store)
    }

    @JvmOverloads
    fun getLocalQuaternion(store: Quaterniond = Quaterniond()): Quaterniond? {
        return localMatrix.getNormalizedRotation(store)
    }

    @JvmOverloads
    fun getTranslation(store: Vector3d = Vector3d()): Vector3d {
        return getWorldMatrix(Matrix4d()).getTranslation(store)
    }

    @JvmOverloads
    fun getQuaternion(store: Quaterniond = Quaterniond()): Quaterniond {
        return getWorldMatrix(Matrix4d()).getNormalizedRotation(store)
    }

    fun setLocalTranslation(x: Double, y: Double, z: Double) {
        localMatrix.setTranslation(x, y, z)
        makeDirty()
    }

    fun setLocalTranslation(v: Vector3dc) {
        setLocalTranslation(v.x(), v.y(), v.z())
    }

    fun setLocalQuaternion(q: Quaterniondc) {
        localMatrix.set3x3(Matrix3d().set(q))
        makeDirty()
    }

    fun setTranslation(x: Double, y: Double, z: Double) {
        if (!hasParent) {
            setLocalTranslation(x, y, z)
        } else {
            setLocalTranslation(worldToLocal(Matrix4d().setTranslation(x, y, z)).getTranslation(Vector3d()))
        }
    }

    fun setTranslation(v: Vector3dc) {
        if (!hasParent) {
            setLocalTranslation(v)
        } else {
            setLocalTranslation(worldToLocal(Matrix4d().setTranslation(v)).getTranslation(Vector3d()))
        }
    }

    fun setQuaternion(q: Quaterniondc) {
        if (!hasParent) {
            setLocalQuaternion(q)
        } else {
            setLocalQuaternion(worldToLocal(Matrix4d().set(q)).getNormalizedRotation(Quaterniond()))
        }
    }

    fun applyMatrix(m: Matrix4dc) {
        localMatrix.mul(m)
        makeDirty()
    }

    fun preApplyMatrix(m: Matrix4dc) {
        m.mul(localMatrix, localMatrix)
        makeDirty()
    }

    fun setTransform(m: Matrix4dc) {
        if (!hasParent) {
            setLocalTransform(m)
        } else {
            setLocalTransform(worldToLocal(m))
        }
    }

    fun setLocalTransform(m: Matrix4dc) {
        localMatrix.set(m)
        makeDirty()
    }

    fun localToWorld(m: Matrix4dc): Matrix4d? {
        return if (!hasParent) {
            Matrix4d(m)
        } else {
            getWorldMatrix(Matrix4d()).mul(m)
        }
    }

    fun localPointToWorld(x: Double, y: Double, z: Double): Vector3d {
        return localPointToWorld(Vector3d(x, y, z))
    }

    fun localPointToWorld(v: Vector3dc): Vector3d {
        return if (!hasParent) {
            Vector3d(v)
        } else {
            Vector3d(v).mulPosition(getWorldMatrix(Matrix4d()))
        }
    }

    fun localVectorToWorld(x: Double, y: Double, z: Double): Vector3d {
        return localVectorToWorld(Vector3d(x, y, z))
    }

    fun localVectorToWorld(v: Vector3dc?): Vector3d {
        return getQuaternion(Quaterniond()).transform(Vector3d(v))
    }

    fun localToWorld(q: Quaterniondc): Quaterniond {
        return if (!hasParent) {
            Quaterniond(q)
        } else {
            getWorldMatrix(Matrix4d()).mul(Matrix4d().set(q)).getNormalizedRotation(Quaterniond())
        }
    }

    fun worldToLocal(m: Matrix4dc): Matrix4d {
        return if (!hasParent) {
            Matrix4d(m)
        } else {
            parent!!.getWorldMatrix(Matrix4d()).invert().mul(m)
        }
    }

    fun worldPointToLocal(v: Vector3dc): Vector3d {
        return if (!hasParent) {
            Vector3d(v)
        } else {
            worldToLocal(Matrix4d().setTranslation(v)).getTranslation(Vector3d())
        }
    }

    fun worldVectorToLocal(x: Double, y: Double, z: Double): Vector3d {
        return localVectorToWorld(Vector3d(x, y, z))
    }

    fun worldVectorToLocal(v: Vector3dc?): Vector3d? {
        return getQuaternion(Quaterniond()).transform(Vector3d(v))
    }

    fun worldToLocal(q: Quaterniondc): Quaterniond {
        return if (!hasParent) {
            Quaterniond(q)
        } else {
            worldToLocal(Matrix4d().set(q)).getNormalizedRotation(Quaterniond())
        }
    }

    fun localTranslate(x: Double, y: Double, z: Double) {
        applyMatrix(Matrix4d().setTranslation(x, y, z))
    }

    fun localTranslate(v: Vector3dc) {
        applyMatrix(Matrix4d().setTranslation(v))
    }

    fun localTranslateX(x: Double) {
        applyMatrix(Matrix4d().setTranslation(x, 0.0, 0.0))
    }

    fun localTranslateY(y: Double) {
        applyMatrix(Matrix4d().setTranslation(0.0, y, 0.0))
    }

    fun localTranslateZ(z: Double) {
        applyMatrix(Matrix4d().setTranslation(0.0, 0.0, z))
    }

    fun localRotate(q: Quaterniondc) {
        applyMatrix(Matrix4d().set(q))
    }

    /*fun localRotateX(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d(1, 0, 0), angle.inRadians())))
    }

    fun localRotateY(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d(0, 1, 0), angle.inRadians())))
    }

    fun localRotateZ(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d(0, 0, 1), angle.inRadians())))
    }*/

    /*fun lookAt(target: Vector3dc?) {
        lookAt(target, Vector3d(0.0, 0.0, 1.0))
    }

    fun lookAt(target: Vector3dc?, up: Vector3dc?) {
        val eye = getTranslation(Vector3d())
        val lookAt: Matrix4d = Matrix4d().setLookAt(eye, target, up).inver
        localMatrix.set3x3(worldToLocal(lookAt))
        makeDirty()
    }*/


}
