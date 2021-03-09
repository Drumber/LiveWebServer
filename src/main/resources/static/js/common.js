
interface.onReady(() => {
    var source = interface.source;

    // add event listeners

    source.addEventListener("log", (event) => {
        var msg = event.data;
        if(msg) console.log("[LOG]", msg);
    });

    source.addEventListener("page_reload", (event) => {
        var reason = event.data;
        if(reason) console.log("Received page reload event:", reason);
        else console.log("Reloading page...");
        // reload the page
        window.location.reload();
    });

    source.addEventListener("page_open", (event) => {
        var url = event.data;
        if(url) {
            console.log("Open web page: " + url);
            window.open(url, "_self");
        }
    });

    source.addEventListener("page_title", (event) => {
        var title = event.data;
        if(title) {
            document.title = title;
        }
    });

    // only for test purpose
    source.addEventListener("time", function(event) {
        var json = JSON.parse(event.data);
        document.getElementById("clock").innerText = json.time;
        document.getElementById("date").innerText = json.date;
    });

});
