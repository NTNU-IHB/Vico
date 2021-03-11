class ChartingApp {

    constructor() {

        const socket = new WebSocket('ws://' + window.location.hostname + ':8000/chart');

        socket.addEventListener('open', function (event) {
            console.log("Websocket connection for charting opened..")
        });

        socket.addEventListener('close', function (event) {
            console.log("Websocket connection for charting closed..")
        });
    }

}
