package no.ntnu.ihb.vico

import com.google.gson.GsonBuilder

class JsonFrame(
    val action: String,
    val data: Any? = null
) {

    fun toJson() = gson.toJson(this)

    private companion object {

        private val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

    }

}
