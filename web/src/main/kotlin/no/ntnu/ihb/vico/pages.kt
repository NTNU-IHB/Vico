package no.ntnu.ihb.vico

import kotlinx.html.*

fun HTML.makeIndex() {
    head {
        title("Vico")
    }
    body {

        h1 {
            +"Vico"
        }
        a {
            href = "visual"
            +"vico"
        }

    }
}

fun HTML.makeVisual() {
    head {
        title("Vico")
    }
    body {

        script {
            src = "https://cdnjs.cloudflare.com/ajax/libs/three.js/r126/three.min.js"
            +"""console.log('hei')"""
        }

    }
}
