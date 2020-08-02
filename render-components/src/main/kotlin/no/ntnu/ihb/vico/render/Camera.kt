package no.ntnu.ihb.vico.render

import no.ntnu.ihb.vico.core.Component

private const val DEFAULT_FOV = 50f
private const val DEFAULT_ASPECT = 1f
private const val DEFAULT_NEAR = 0.1f
private const val DEFAULT_FAR = 2000f

sealed class Camera : Component()

class PerspectiveCamera @JvmOverloads constructor(
    val fov: Float = DEFAULT_FOV,
    val aspect: Float = DEFAULT_ASPECT,
    val near: Float = DEFAULT_NEAR,
    val far: Float = DEFAULT_FAR
) : Camera()
