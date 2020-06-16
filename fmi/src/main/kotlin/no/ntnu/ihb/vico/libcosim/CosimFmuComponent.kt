package no.ntnu.ihb.vico.libcosim

import no.ntnu.ihb.acco.core.Component
import java.io.File

class CosimFmuComponent(
    val source: File,
    val instanceName: String
) : Component
