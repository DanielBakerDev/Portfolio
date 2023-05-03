(function () {

  var _frm = $('#joinClassRoom'),
    _btn = _frm.find('button[type="submit"]');

  function _init() {
    _validate.init();
    _submit.init();

    //On click show modal for join
    $(document).on('click', 'a.roomListA', function () {
      //On click lets set the id of the hidden input to the id of the class that was clicked on
      let input = document.getElementById('classId');
      input.value = this.id;
      //Set the name of the modal to the name of whatever id was clicked on (this is a shit way to do it but idec)
      let name = document.getElementById('classRoom').innerHTML
      document.getElementById('exampleModalLabel').innerHTML = "Join " + name;
    });

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
        classPassword: "required"
      },
      messages: {
        classPassword: {
          required: "Please enter a password"
        }
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
          alert('fail')
        } else {
          window.location = d.redirect;
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
  }
})();