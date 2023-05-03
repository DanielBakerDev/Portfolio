(function () {

    var _frm = $('#createClass'),
        _btn = _frm.find('button[type="submit"]');

    function _init() {
        _validate.init();
        _submit.init();
    }

    var _validate = {
        options: {
            ignore: [],
            errorElement: 'span',
            errorPlacement: function (error, element) {
                error.insertBefore(element);
                return false;
            },
            invalidHandler: function (form, validator) {
                if (!validator.numberOfInvalids()) {
                    return;
                }
            },
            rules: {
                className: "required",
                institution: "required",
                description: "required",
				password: "required",
				userfile: "required"
            }
        },
        init: function () {
            _frm.validate(this.options);
        }
    };

    var _submit = {
        options: {
            url: location.href,
            type: 'post',
            dataType: 'json',
            beforeSubmit: function (arr, form, options) {
                _submit.action.buttonOff(),
                    $('.pgn-wrapper').remove();
                return _frm.valid(); //call the validate plugin
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
                alert("Failed!")
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