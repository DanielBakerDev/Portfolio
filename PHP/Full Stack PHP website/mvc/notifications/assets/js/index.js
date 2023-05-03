(function () {

    var _frm = $('#createMsg'),
		_btn = _frm.find('button[type="submit"]');
		
	var _doc = $(document);

    function _init() {
		_validate.init();
		_submit.init();
	   
		$(document).on('click', '#markNotificationAsRead', function (e) {
			let notificationId = $(this).data('notification-id');
			console.log(notificationId)

            let data = {
                markNotifcationAsRead: notificationId,
            };

            $.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
			
					location.reload();
					console.log(d.r)
					// console.log('success')
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
				},
                error: function () {
                    console.log('error')
                }
            });
		});

		$(document).on('click', '#markMessageAsRead', function (e) {
			let messageId = $(this).data('message-id');
			console.log(messageId)

			let data = {
                markMessageAsRead: messageId,
			};

			$.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
					console.log(d.r)
					console.log(d.message)
					location.reload();
					// console.log('success')
                },
                error: function () {
                    console.log('error')
                }
            });
		});

		$(document).on('click', '#acceptBid', function (e) {
			let bidID = $(this).data('project-bid-id');
			let data = {
                acceptBID: bidID,
			};

			$.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
					// console.log(d.r)
					// console.log(d.message)
					location.reload();
					// console.log('success')
                },
                error: function () {
                    console.log('error')
                }
            });
		});

		$(document).on('click', '#rejectBid', function (e) {
			let bidID = $(this).data('project-bid-id');
			let data = {
                rejectBID: bidID,
			};

			$.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
					// console.log(d.r)
					// console.log(d.message)
					location.reload();
					// console.log('success')
                },
                error: function () {
                    console.log('error')
                }
            });
		});
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
                userid: "required",
                type: "required",
                message: "required"
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
                return _frm.valid(); 
            },
            success: function (d) {
                if (d.r == "fail") {
                    //_submit.action.notify(d.type, d.message);
					alert("Failed but sent!")
                } else {
					// window.location = d.redirect;
					alert("success")
					// _submit.action.notify(d.type, d.message);
					location.reload();
                }
            },
            error: function (d) {
				alert("fail")
               
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
    };

    return {
        init: _init()
    };
})();