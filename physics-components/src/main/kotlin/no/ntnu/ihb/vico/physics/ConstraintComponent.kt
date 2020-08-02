package no.ntnu.ihb.vico.physics

sealed class ConstraintComponent

class LockComponent : ConstraintComponent()

class HingeComponent : ConstraintComponent()
