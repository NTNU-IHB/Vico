package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.math.Frame


val Entity.transform
    get() = getComponent(TransformComponent::class.java)


class TransformComponent : Frame(), Component
