$(document).ready(function () {
    $("#telegram").submit(function (event) {
        event.preventDefault();
        $.ajax({
            url: 'sendToTelegram',
            data: {
                "message": $("#message").val()
            },
            success: function (result) {
                if (JSON.parse(result) == "200")
                    $("#result").html("Successful!").fadeIn('fast').delay(4000).fadeOut('slow');
                else
                    $("#result").html("Failed!").fadeIn('fast').delay(4000).fadeOut('slow');
            }
        });
    });
});


