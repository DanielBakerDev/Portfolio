(function () {

    var _frm = $('#testCode'),
        _btn = _frm.find('button[type="submit"]');

		
    var _doc = $(document);

    function _init() {
        _validate.init();
		_submit.init();
	}
	
	function setScore(score){
		let scoreID = document.getElementById("score");
		scoreID.innerHTML = "Score: " +  score.toFixed(2);

		if(score < 30){
			scoreID.style.color = 'green'
		}else if(score > 30 && score < 60){
			scoreID.style.color = 'yellow'
		}else{
			scoreID.style.color = 'red'
		}
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
                codeSet1: "required",
                codeSet2: "required"
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
                    
                } else {
					//window.location = d.redirect;
					setScore(d.score)
					console.log(d.score)
                }

            },
            error: function (d) {

            }
        },
        init: function () {
            _frm.ajaxForm(this.options);
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