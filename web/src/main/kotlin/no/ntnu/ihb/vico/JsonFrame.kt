package no.ntnu.ihb.vico

import com.google.gson.GsonBuilder

internal class JsonFrame(
    val action: String,
    val data: Any? = null
) {

    fun toJson() = gson.toJson(this)

    companion object {

        fun fromJson(str: String): JsonFrame {
            return gson.fromJson(str, JsonFrame::class.java)
        }

        private val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

    }

}
