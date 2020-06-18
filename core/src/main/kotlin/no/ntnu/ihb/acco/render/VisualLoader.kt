package no.ntnu.ihb.acco.render

import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.render.jaxb.TTransform
import no.ntnu.ihb.acco.render.jaxb.TTransforms
import java.io.File
import javax.xml.bind.JAXB

object VisualLoader {

    fun load(configFile: File): List<Entity> {
        require(configFile.exists())
        return load(JAXB.unmarshal(configFile, TTransforms::class.java))
    }

    fun load(config: TTransforms): List<Entity> {
        return config.transform.map { loadTransform(it) }
    }

    private fun loadTransform(t: TTransform): Entity {
        val entity = Entity(t.name)

        return entity
    }

}
