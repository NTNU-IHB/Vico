function send(socket, action, data) {

    if (data) {
        socket.send(JSON.stringify({
            "action": action,
            "data": data
        }))
    } else {
        socket.send(JSON.stringify({
            "action": action
        }))
    }

}

