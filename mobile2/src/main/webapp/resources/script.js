//window.onload = init;
const tcp = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
const host = window.location.host;
let path="/mobile2/test";
const socket = new WebSocket(tcp + host + path);
socket.onmessage = onMessage;

function onMessage(event) {
    let data = JSON.parse(event.data);

    if (data.hasOwnProperty("tariffs")) {
        $("#tariffs").empty();
        for (let k=0;k<data.tariffs.length;k++){
            let tariff=data.tariffs[k];
            $("#tariffs").append(buildTariff(tariff));
        }
    }
}

function buildTariff(tariff){
    let col = document.createElement("div");
    col.setAttribute("class", "col-sm-4");

    let panel=document.createElement("div");
    panel.setAttribute("class","panel panel-info");
    col.appendChild(panel);

    let panelHead=document.createElement("div");
    panelHead.setAttribute("class","panel-heading");
    panelHead.innerText=tariff.name;
    panel.appendChild(panelHead);

    let panelBody=document.createElement("div");
    panelBody.setAttribute("class","panel-body");
    panelBody.innerText=tariff.description;
    panel.appendChild(panelBody);

    let panelFooter=document.createElement("div");
    panelFooter.setAttribute("class","panel-footer");
    panelFooter.innerText = "$" + tariff.price;
    panel.appendChild(panelFooter);
    return col;
}

