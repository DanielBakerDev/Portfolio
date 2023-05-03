(function () {

    var _frm = $('#updateUser'),
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
                username: "required",
                profilePicture: "required",
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
                console.log("here");
                if (d.r == "fail") {
                    alert('fail')
                } else {
                    alert("Profile Successfully Updated");
                    window.location = d.redirect;
                    //I will add the notifications to here aftwerwards
                }

            },
            error: function (d) {
                console.log(d);
            }
        },
        init: function () {
            _frm.ajaxForm(this.options);
        }

    };

    return {
        init: _init()
    };
})();