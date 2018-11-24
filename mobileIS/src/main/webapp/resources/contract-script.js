$(document).ready(function () {
    $("#find").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: 'findContract',
            data: {
                "phone": $("#phoneNumber").val()
            },
            success: function (result) {
                var data = JSON.parse(result);
                if (data.status == "error") {
                    $("#message").html(data.message);
                } else {
                    $("#message").empty();
                    $('#result').html("<h3>Search result</h3>").append(buildContractSearchResult(data.contract));
                }
            }
        });
    });
});

function buildContractSearchResult(contract) {

    let td1;
    let td3;

    if (contract.isBlocked) td1 = "<td><span class=\"label label-danger\">Blocked</span></td>";
    else td1 = "<td></td>";
    if (contract.isBlockedByAdmin) td3 = "<td><span class=\"label label-danger\">Blocked</span></td>";
    else td3 = "<td></td>";

    let table = document.createElement("table");
    table.setAttribute("class", "table table-striped");
    table.innerHTML = "<thead>" +
        "<tr>" +
        "<th>id</th>" +
        "<th>Phone</th>" +
        "<th>Tariff</th>" +
        "<th>Owner</th>" +
        "<th>Client block</th>" +
        "<th>Blocked</th>" +
        "</tr>" +
        "</thead>";

    let body = document.createElement("tbody");
    body.innerHTML = "<tr>" +
        "<td>" + contract.id + "</td>" +
        "<td>" + contract.number + "</td>" +
        "<td>" + contract.tariffName + "</td>" +
        "<td>" + contract.ownerName + "</td>" + td1 + td3 +
        "<td>" +
        "<form action=\"editContract\" method=\"get\">" +
        "<input type=\"hidden\" name=\"id\" value=" + contract.id + ">" +
        "<input type=\"submit\" value=\"Edit\" class=\"btn btn-warning\"></form>" +
        "</td>" +
        "<td>" +
        "<form action=\"showContract\" method=\"get\">" +
        "<input type=\"hidden\" name=\"id\" value=" + contract.id + " >" +
        "<input type=\"submit\" value=\"Show details\" class=\"btn btn-info\"></form>" +
        "</td>" +
        "</tr><br>";
    table.appendChild(body);
    return table;
}

