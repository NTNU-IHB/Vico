class KeyHandler {

    constructor(socket) {

        document.addEventListener('keydown', function (event) {
            let key = event.key
            send(socket, "keyPressed", key)
        })

    }

}
