package no.ntnu.ihb.acco.physics

sealed class ConstraintComponent

class LockComponent : ConstraintComponent()

class HingeComponent : ConstraintComponent()
