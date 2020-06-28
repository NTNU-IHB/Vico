package no.ntnu.ihb.acco.math

import org.joml.*


interface IFrame {

    val hasParent: Boolean
    val children: MutableSet<IFrame>

    fun makeDirty()
    fun clearParent()
    fun setParent(parent: IFrame)

    fun getLocalMatrix(store: Matrix4d = Matrix4d()): Matrix4d
    fun getWorldMatrix(store: Matrix4d = Matrix4d()): Matrix4d
    fun updateWorldMatrix()

    fun getLocalTranslation(store: Vector3d = Vector3d()): Vector3d
    fun getLocalQuaternion(store: Quaterniond = Quaterniond()): Quaterniond?
    fun getTranslation(store: Vector3d = Vector3d()): Vector3d
    fun getQuaternion(store: Quaterniond = Quaterniond()): Quaterniond

    fun setLocalTranslation(x: Double, y: Double, z: Double)
    fun setLocalTranslation(v: Vector3dc)
    fun setLocalQuaternion(q: Quaterniondc)
    fun setTranslation(x: Double, y: Double, z: Double)
    fun setTranslation(v: Vector3dc)
    fun setQuaternion(q: Quaterniondc)
    fun applyMatrix(m: Matrix4dc)
    fun preApplyMatrix(m: Matrix4dc)
    fun setTransform(m: Matrix4dc)
    fun setLocalTransform(m: Matrix4dc)
    fun localToWorld(m: Matrix4dc): Matrix4d?
    fun localPointToWorld(x: Double, y: Double, z: Double): Vector3d
    fun localPointToWorld(v: Vector3dc): Vector3d
    fun localVectorToWorld(x: Double, y: Double, z: Double): Vector3d
    fun localVectorToWorld(v: Vector3dc?): Vector3d
    fun localToWorld(q: Quaterniondc): Quaterniond
    fun worldToLocal(m: Matrix4dc): Matrix4d
    fun worldPointToLocal(v: Vector3dc): Vector3d
    fun worldVectorToLocal(x: Double, y: Double, z: Double): Vector3d
    fun worldVectorToLocal(v: Vector3dc): Vector3d
    fun worldToLocal(q: Quaterniondc): Quaterniond
    fun localTranslate(x: Double, y: Double, z: Double)
    fun localTranslate(v: Vector3dc)
    fun localTranslateX(x: Double)
    fun localTranslateY(y: Double)
    fun localTranslateZ(z: Double)
    fun localRotate(q: Quaterniondc)
    fun localRotateX(angle: Angle)
    fun localRotateY(angle: Angle)
    fun localRotateZ(angle: Angle)
}

open class Frame : IFrame {

    private var parent: IFrame? = null
    override val children: MutableSet<IFrame> = mutableSetOf()

    private val localMatrix = Matrix4d()
    private val worldMatrix = Matrix4d()

    private var dirty = true

    override val hasParent: Boolean
        get() = parent != null


    override fun makeDirty() {
        dirty = true
        children.forEach { it.makeDirty() }
    }

    override fun clearParent() {
        parent?.also {
            it.children.remove(this)
        }
        this.parent = null
        makeDirty()
    }

    override fun setParent(parent: IFrame) {
        if (hasParent) {
            val w = getWorldMatrix(Matrix4d())
            this.parent = parent
            setLocalTransform(worldToLocal(w))
        } else {
            this.parent = parent
        }
        parent.children.add(this)
        makeDirty()
    }

    override fun getLocalMatrix(store: Matrix4d): Matrix4d {
        return store.set(localMatrix)
    }

    override fun getWorldMatrix(store: Matrix4d): Matrix4d {
        if (dirty) {
            updateWorldMatrix()
        }
        return store.set(worldMatrix)
    }

    override fun updateWorldMatrix() {
        if (hasParent) {
            parent!!.getWorldMatrix(worldMatrix).mul(localMatrix)
        } else {
            worldMatrix.set(localMatrix)
        }
        dirty = false
    }

    override fun getLocalTranslation(store: Vector3d): Vector3d {
        return localMatrix.getTranslation(store)
    }

    override fun getLocalQuaternion(store: Quaterniond): Quaterniond? {
        return localMatrix.getNormalizedRotation(store)
    }

    override fun getTranslation(store: Vector3d): Vector3d {
        return getWorldMatrix(Matrix4d()).getTranslation(store)
    }

    override fun getQuaternion(store: Quaterniond): Quaterniond {
        return getWorldMatrix(Matrix4d()).getNormalizedRotation(store)
    }

    override fun setLocalTranslation(x: Double, y: Double, z: Double) {
        localMatrix.setTranslation(x, y, z)
        makeDirty()
    }

    override fun setLocalTranslation(v: Vector3dc) {
        setLocalTranslation(v.x(), v.y(), v.z())
    }

    override fun setLocalQuaternion(q: Quaterniondc) {
        localMatrix.set3x3(Matrix3d().set(q))
        makeDirty()
    }

    override fun setTranslation(x: Double, y: Double, z: Double) {
        if (!hasParent) {
            setLocalTranslation(x, y, z)
        } else {
            setLocalTranslation(worldToLocal(Matrix4d().setTranslation(x, y, z)).getTranslation(Vector3d()))
        }
    }

    override fun setTranslation(v: Vector3dc) {
        if (!hasParent) {
            setLocalTranslation(v)
        } else {
            setLocalTranslation(worldToLocal(Matrix4d().setTranslation(v)).getTranslation(Vector3d()))
        }
    }

    override fun setQuaternion(q: Quaterniondc) {
        if (!hasParent) {
            setLocalQuaternion(q)
        } else {
            setLocalQuaternion(worldToLocal(Matrix4d().set(q)).getNormalizedRotation(Quaterniond()))
        }
    }

    override fun applyMatrix(m: Matrix4dc) {
        localMatrix.mul(m)
        makeDirty()
    }

    override fun preApplyMatrix(m: Matrix4dc) {
        m.mul(localMatrix, localMatrix)
        makeDirty()
    }

    override fun setTransform(m: Matrix4dc) {
        if (!hasParent) {
            setLocalTransform(m)
        } else {
            setLocalTransform(worldToLocal(m))
        }
    }

    override fun setLocalTransform(m: Matrix4dc) {
        localMatrix.set(m)
        makeDirty()
    }

    override fun localToWorld(m: Matrix4dc): Matrix4d? {
        return if (!hasParent) {
            Matrix4d(m)
        } else {
            getWorldMatrix(Matrix4d()).mul(m)
        }
    }

    override fun localPointToWorld(x: Double, y: Double, z: Double): Vector3d {
        return localPointToWorld(Vector3d(x, y, z))
    }

    override fun localPointToWorld(v: Vector3dc): Vector3d {
        return if (!hasParent) {
            Vector3d(v)
        } else {
            Vector3d(v).mulPosition(getWorldMatrix(Matrix4d()))
        }
    }

    override fun localVectorToWorld(x: Double, y: Double, z: Double): Vector3d {
        return localVectorToWorld(Vector3d(x, y, z))
    }

    override fun localVectorToWorld(v: Vector3dc?): Vector3d {
        return getQuaternion(Quaterniond()).transform(Vector3d(v))
    }

    override fun localToWorld(q: Quaterniondc): Quaterniond {
        return if (!hasParent) {
            Quaterniond(q)
        } else {
            getWorldMatrix(Matrix4d()).mul(Matrix4d().set(q)).getNormalizedRotation(Quaterniond())
        }
    }

    override fun worldToLocal(m: Matrix4dc): Matrix4d {
        return if (!hasParent) {
            Matrix4d(m)
        } else {
            parent!!.getWorldMatrix(Matrix4d()).invert().mul(m)
        }
    }

    override fun worldPointToLocal(v: Vector3dc): Vector3d {
        return if (!hasParent) {
            Vector3d(v)
        } else {
            worldToLocal(Matrix4d().setTranslation(v)).getTranslation(Vector3d())
        }
    }

    override fun worldVectorToLocal(x: Double, y: Double, z: Double): Vector3d {
        return localVectorToWorld(Vector3d(x, y, z))
    }

    override fun worldVectorToLocal(v: Vector3dc): Vector3d {
        return getQuaternion(Quaterniond()).transform(Vector3d(v))
    }

    override fun worldToLocal(q: Quaterniondc): Quaterniond {
        return if (!hasParent) {
            Quaterniond(q)
        } else {
            worldToLocal(Matrix4d().set(q)).getNormalizedRotation(Quaterniond())
        }
    }

    override fun localTranslate(x: Double, y: Double, z: Double) {
        applyMatrix(Matrix4d().setTranslation(x, y, z))
    }

    override fun localTranslate(v: Vector3dc) {
        applyMatrix(Matrix4d().setTranslation(v))
    }

    override fun localTranslateX(x: Double) {
        applyMatrix(Matrix4d().setTranslation(x, 0.0, 0.0))
    }

    override fun localTranslateY(y: Double) {
        applyMatrix(Matrix4d().setTranslation(0.0, y, 0.0))
    }

    override fun localTranslateZ(z: Double) {
        applyMatrix(Matrix4d().setTranslation(0.0, 0.0, z))
    }

    override fun localRotate(q: Quaterniondc) {
        applyMatrix(Matrix4d().set(q))
    }

    override fun localRotateX(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d_X, angle.inRadians())))
    }

    override fun localRotateY(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d_Y, angle.inRadians())))
    }

    override fun localRotateZ(angle: Angle) {
        applyMatrix(Matrix4d().set(Quaterniond().fromAxisAngleRad(Vector3d_Z, angle.inRadians())))
    }

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
