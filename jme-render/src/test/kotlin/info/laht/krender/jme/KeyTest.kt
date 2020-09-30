package info.laht.krender.jme

fun main() {

    JmeRenderEngine().apply {

        registerKeyListener {
            println(it)
        }

    }

}
