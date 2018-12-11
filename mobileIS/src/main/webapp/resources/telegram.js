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
                    $("#result").removeClass('btn-danger').addClass("btn-success").html("Successful!").fadeIn('slow').delay(3000).fadeOut('slow');
                }

                else {
                    $("#result").removeClass('btn-success').addClass("btn-danger").html("Failed!").fadeIn('slow').delay(3000).fadeOut('slow');
                }

            }
        });
    });
});


