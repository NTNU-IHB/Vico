package info.laht.krender.threekt

import info.laht.threekt.math.Color

fun main() {

    ThreektRenderer().apply {

        setBackGroundColor(Color.black)

        createArrow(1f).apply {
            setColor(Color.green)
        }

    }

}
