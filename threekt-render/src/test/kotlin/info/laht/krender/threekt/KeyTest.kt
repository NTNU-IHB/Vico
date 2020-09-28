package info.laht.krender.threekt

fun main() {

    ThreektRenderer().apply {

        registerKeyListener {
            println(it)
        }

    }

}
