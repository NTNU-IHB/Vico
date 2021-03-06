function send(socket, action, data) {

    if (socket.readyState === WebSocket.OPEN) {

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
        return true
    } else {
        return false
    }

}

