
// namespace
this.interface = this.interface || {};

(function() {

    interface.source = undefined;
    var callbacks = [];

    document.addEventListener("DOMContentLoaded", function() {
        if(!!window.EventSource) {
            interface.source = new EventSource("http://localhost:8080/subscribe");
    
            if(callbacks.length > 0) {
                // call pending callback functions
                callbacks.forEach((func, index, array) => {
                    func();
                    array.splice(index, 1); // remove element from array
                });
            }
        } else {
            console.log("The browser does not support Server Sent Events.");
        }
    }, false);

    interface.onReady = function(callback) {
        if(interface.source != undefined) {
            callback();
        } else {
            callbacks.push(callback);
        }
    }

}());
