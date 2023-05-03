(function () {

    var _frm = $('#editUser'),
        _btn = _frm.find('button[type="submit"]');

    var _doc = $(document);

    function _init() {
        _validate.init();
        _submit.init();
    }

    var _validate = {
        options: {
            ignore: [],
            errorElement: 'span',
            errorPlacement: function (error, element) {
                if (error.length > 0) {

                    error.insertBefore(element);

                }
                return false;
            },
            invalidHandler: function (form, validator) {
                if (!validator.numberOfInvalids()) {
                    return;
                }
            },
            rules: {
                name: "required",
                email: "required",
                username: "required",
                password: "required",
                username: "required"
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

                $('.pgn-wrapper').remove();
                return _frm.valid(); //call the validate plugin
            },
            success: function (d) {
                if (d.r == "fail") {
					_submit.action.notify(d.type, d.message);
                    //alert('fail')
                } else {
					_submit.action.notify(d.type, d.message);
                    //alert("success")
                    //window.location = d.redirect;

                    //I will add the notifications to here aftwerwards
                }

            },
            error: function (d) {

            }
        },
        init: function () {
            _frm.ajaxForm(this.options);
		},
		action: {
            notify: function(type, message) {
                $('.notify').pgNotification({
                    style: 'flip',
                    message: message,
                    position: 'top-right',
                    timeout: 0,
                    type: type
                }).show();
            }
        }
        // action: {
        //   buttonOn: function () {
        //     _btn.removeClass('disabled').removeAttr('disabled');
        //   },
        //   buttonOff: function () {
        //     _btn.addClass('disabled').attr('disabled', 'disabled');
        //   },
        //   notify: function (type, message) {
        //     $('.page-content-wrapper').pgNotification({
        //       style: 'flip',
        //       message: message,
        //       position: 'top-right',
        //       timeout: 0,
        //       type: type
        //     }).show();
        //   }
        // }
    };

    return {
        init: _init()
    };
})();