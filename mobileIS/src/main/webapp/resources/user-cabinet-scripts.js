const SUCCESS = 'success';
const ERROR = 'error';
const DISABLED = 'disabled';
const MORE_TARIFFS_SELECTOR = '#moreTariffs';
const MORE_OPTIONS_SELECTOR = '#moreOptions';
const TARIFFS_SELECTOR = '#tariffs';

$(document).ready(function () {
    $(".delete-option").submit(function (event) {
        event.preventDefault();
        let id = $(this).children('input:hidden[name="id"]').val();
        let csrfValue = $(this).children('input:hidden[class="csrf"]').attr('value');

        $.ajax({
            url: 'deleteOption',
            data: {
                "id": id,
                '_csrf': csrfValue
            },
            type: "post",
            success: function (result) {
                var data = JSON.parse(result);
                if (data.status == ERROR) {
                    $(".modal-body").text(data.message);
                    $("#cart-message").modal();
                }
                if (data.status == SUCCESS) {
                    location.reload(false);
                }
            }
        });
    });
});

$(document).ready(function () {
    $('#buy-button').submit(function (event) {
        event.preventDefault();
        let csrfValue = $(this).children('input:hidden').attr('value');
        $.ajax({
            url: 'buy',
            data: {
                '_csrf': csrfValue
            },
            type: "post",
            success: function (result) {
                var data = JSON.parse(result);
                if (data.status == ERROR) {
                    $(".modal-body").text(data.message);
                    $("#cart-message").modal();
                }
                if (data.status == SUCCESS) {
                    location.reload(false);
                }

            }
        })
    });
});

function addToCart(id) {
    $.ajax({
        url: 'addToCart',
        data: {'id': id},
        success: function (result) {
            buildOptionInCart(JSON.parse(result));
        }
    })
}

function deleteFromCart(id) {
    $.ajax({
        url: 'deleteFromCart',
        data: {'id': id},
        success: function (result) {
            let data = JSON.parse(result);
            $("#optionInCart" + data.id).remove();
            $('#newOption' + data.id).find('button').removeAttr(DISABLED); //remove from available
            $("#cart-sum").text('Total: $' + data.totalSum);
            if ($('#cart-body').children().length < 1) {
                $("#buy-button").attr(DISABLED, DISABLED);
            }
        }
    })
}

function getMoreOptions() {
    let page = $(MORE_OPTIONS_SELECTOR).attr('data-page');
    $.ajax({
        url: 'getMoreOptions',
        data: {'page': page},
        success: function (result) {
            let data = JSON.parse(result);
            let page = parseInt($(MORE_OPTIONS_SELECTOR).attr('data-page'));
            if (page < data.total) {
                $(MORE_OPTIONS_SELECTOR).attr('data-page', 1 + page);
            } else {
                $(MORE_OPTIONS_SELECTOR).attr(DISABLED, DISABLED);
            }
            showOptions(data.items);
        }
    })
}


function showOptions(data) {
    for (let k = 0; k < data.length; k++) {
        let option = data[k];
        let id = option.id;
        if ($('#option' + id).length > 0) continue; //skip option in contract
        if ($('#tariffOption' + id).length > 0) continue; //skip option in tariff
        let card = $($('#available-options').find($('.card'))[0]).clone().appendTo($('#available-options').find($('.card-columns')));
        card.attr('id', 'newOption' + option.id);
        card.find($('.card-title')).text(option.name);
        card.find('p')[0].innerText = option.description;
        card.find('p')[1].innerText = '$' + option.price + ' per month';
        card.find('p')[2].innerText = '$' + option.subscribeCost + ' for subscribe';
        card.find('button').attr('onclick', 'addToCart(' + option.id + ')');
    }
}

function getMoreTariffs() {
    let page = $(MORE_TARIFFS_SELECTOR).attr('data-page');
    $.ajax({
        url: 'getMoreTariffs',
        data: {'page': page},
        success: function (result) {
            let data = JSON.parse(result);
            let page = parseInt($(MORE_TARIFFS_SELECTOR).attr('data-page'));
            if (page < data.total) {
                $(MORE_TARIFFS_SELECTOR).attr('data-page', 1 + page);
            } else {
                $(MORE_TARIFFS_SELECTOR).attr(DISABLED, DISABLED);
            }
            showTariffs(data.items);
        }
    })
}

function showTariffs(data) {
    let currentTariffId = $('#tariffInfo').attr('data-tariffId');
    for (let k = 0; k < data.length; k++) {
        let tariff = data[k];
        if (tariff.id == currentTariffId) continue; //skip tariff in contract
        let card = $($(TARIFFS_SELECTOR).find($('.card'))[0]).clone().appendTo($(TARIFFS_SELECTOR).find($('.card-columns')));
        card.attr('id', 'tariff' + tariff.id);
        card.find($('.card-title')).text(tariff.name);
        card.find('p')[0].innerText = tariff.description;
        card.find('p')[1].innerText = '$' + tariff.price + ' per month';
        //get tariff or current tariff
        let $button = $('<a/>', {
            class: "btn btn-success",
            text: 'Get tariff',
            href: "getTariff/" + tariff.id,
            role: "button"
        });
        card.find($(".card-footer")).empty().append($button);
    }

}

function buildOptionInCart(data) {
    let option = data.option;
    if (option.hasOwnProperty('name')) {
        let name = option['name'];
        let id = option['id'];
        let cost = option['cost'];
        let $button = $('<button/>', {
            class: "btn btn-sm btn-outline-danger",
            text: '  X  ',
        });
        $button.click(function () {
            deleteFromCart(id);
        });
        let $p = $('<p/>', {
            class: "card-text d-flex justify-content-between align-items-center",
            id: 'optionInCart' + id,
            text: name + ': $' + cost,
        });

        $p.append($button);
        $('#cart-body').append($p);
        $("#cart-sum").text("Total: $" + data.totalSum);
        $("#buy-button").removeAttr(DISABLED);
        $('#newOption' + id).find('button').attr(DISABLED, DISABLED); //remove from available
    }
}

function filterTariff() {
    let currentTariffId = $('#tariffInfo').attr('data-tariffId');
    let card = $(TARIFFS_SELECTOR).find('#' + currentTariffId);
    if (card.length > 0) {
        $(card).find('a').replaceWith('Current tariff');
    }
}