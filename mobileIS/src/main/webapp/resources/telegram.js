$(document).ready(function () {
    $("#telegram").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: 'sendToTelegram',
            data: {
                "message": $("#message").val()
            },
            success: function (result) {
                if (JSON.parse(result) == "200") {
                    $("result").addClass("btn-success").html("Successful!").fadeIn('slow').delay(4000).fadeOut('slow');
                }

                else {
                    $("result").addClass("btn-danger").html("Failed!").fadeIn('slow').delay(4000).fadeOut('slow');
                }

            }
        });
    });
});


