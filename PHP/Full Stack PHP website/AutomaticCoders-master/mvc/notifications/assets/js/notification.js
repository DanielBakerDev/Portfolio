(function () {


	var _doc = $(document);

    function _init() {
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
	}

    return {
        init: _init()
    };
})();