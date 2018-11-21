$(document).ready(function () {
    $("#find").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: $(this).attr('action'),
            data: {"phone": $("#phoneNumber").val()},
            success: function (result) {
                $("#message").empty();
                $("#message1").empty();
                var data = JSON.parse(result);
                if (data.status == "error") {
                    $("#message").html(data.message);
                } else {
                    $("#result").html("<h3>Search result</h3>").append(buildClientSearchResult(data.client));
                }
            }
        });

    });

    $("#find1").submit(function (event) {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        $.ajax({
            url: url,
            data: {"passport": $("#passport").val()},
            success: function (result) {
                $("#message").empty();
                $("#message1").empty();
                var data = JSON.parse(result);
                if (data.status == "error") {
                    $("#message1").html(data.message);
                } else {
                    $("#result").html("<h3>Search result</h3>").append(buildClientSearchResult(data.client));
                }
            }
        });
    });

});

function buildClientSearchResult(client) {
    let table = document.createElement("table");
    table.setAttribute("class", "table table-striped");
    table.innerHTML = "<thead>" +
        "<tr>" +
        "<th>id</th>" +
        "<th>Name</th>" +
        "<th>Surname</th>" +
        "<th>Passport</th>" +
        "<th>e-mail</th>" +
        "</tr>" +
        "</thead>";

    let body = document.createElement("tbody");
    body.innerHTML = "<tr>" +
        "<td>" + client.id + "</td>" +
        "<td>" + client.name + "</td>" +
        "<td>" + client.surname + "</td>" +
        "<td>" + client.passportId + "</td>" +
        "<td>" + client.email + "</td>" +
        "<td>" + "<form action=\"editClient\" method=\"get\">" +
        "<input type=\"hidden\" name=\"id\" value=" + client.id + ">" +
        "<input type=\"submit\" value=\"Edit\" class=\"btn btn-warning\"></form>" + "</td>" +
        "<td>" + "<form action=\"showClient\" method=\"get\">" +
        "<input type=\"hidden\" name=\"id\" value=" + client.id + " >" +
        "<input type=\"submit\" value=\"Show details\" class=\"btn btn-info\"></form>" + "</td>" +
        "</tr><br>";
    table.appendChild(body);
    return table;
}

