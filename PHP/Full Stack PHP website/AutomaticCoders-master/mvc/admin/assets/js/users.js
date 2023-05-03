(function () {


    var _doc = $(document);

    function _init() {

        $(document).on('click', '#deleteUser', function () {
            let userId = $(this).data('user-id');

            let data = {
                userId: userId,
            };

            $.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
                    //I will be putting actual notifications into this later
                    location.reload();
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