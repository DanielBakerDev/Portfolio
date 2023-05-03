(function () {

    function _init() {
        _validate.init();
        _submit.init();
    }

    var _submit = {
        options: {
            url: location.href,
            type: 'post',
            dataType: 'json',
            beforeSubmit: function (arr, form, options) {
                _submit.action.buttonOff(),
                    $('.pgn-wrapper').remove();
                return _frm.valid();
            },
            success: function (d) {
                if (d.r == "fail") {
                    _submit.action.notify(d.type, d.message);
                    _submit.action.buttonOn();
                } else {
                    window.location = d.redirect;
                }
                _submit.action.buttonOn();
            },
            error: function (d) {
                _submit.action.buttonOn();
            }
        },
        init: function () {
            _frm.ajaxForm(this.options);
        },
        action: {
            buttonOn: function () {
                _btn.removeClass('disabled').removeAttr('disabled');
            },
            buttonOff: function () {
                _btn.addClass('disabled').attr('disabled', 'disabled');
            },
            notify: function (type, message) {
                $('body').pgNotification({
                    style: 'flip',
                    message: message,
                    position: 'top-right',
                    timeout: 0,
                    type: type
                }).show(),
                    setTimeout(function () {
                        $('.pgn-wrapper').remove();
                    }, 3000);
            }
        }
    };

    return {
        init: _init()
    };
})();