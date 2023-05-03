(function () {

    var _frm = $('#addProjects'),
        _btn = _frm.find('button[type="submit"]');

	var _frm2 = $('#addProjectsClass'),
		_btn = _frm2.find('button[type="submit"]');

	var _frm3 = $('#bidOnProject'),
		_btn = _frm3.find('button[type="submit"]');
		
    var _doc = $(document);

    function _init() {
        _validate.init();
		_submit.init();
		
		// _validate2.init();
		// _submit2.init();

		_validate3.init();
		_submit3.init();

		_doc.on('click', '#leaveClass', function (e) {
			leaveClass();
		});

        //On click li load projects
        _doc.on('click', 'li.channel', function (e) {
            let id = $(this).attr('id')

            let className = $(this).data('ing-value');
            let projectClassId = $(this).data('ing-id');
            let classId = $(this).data('ing-classid');

            changeClassName(className);
            buildAddProjectButton(projectClassId, classId);

            let data = {
                projectClassId: id,
            };

            $.ajax({
                dataType: "json",
                type: "POST",
                data: data,
                success: function (d) {
                    buildProjects(d);
                },
                error: function () {
                    console.log('error')
                }
            });

		});

		

		_doc.on('click', '#bidProject', function (e) {
			//Here we need to get the values and replace them in the form
			let elem = document.getElementById("askingPrice");
			let askingPrice = $(this).data('askingprice-value');
			elem.value = askingPrice;

			// let elem = document.getElementById("askingPrice");
			let elem2 = document.getElementById("projectName");
			let name = $(this).data('name-value');
			elem2.innerHTML = "Project from " + name;

			// let elem = document.getElementById("askingPrice");
			let elem3 = document.getElementById("projectid");
			let projectID = $(this).data('projectid-value');
			elem3.value = projectID;
			
			$('#bidOnProjectModal').modal('toggle');
		})

		// _doc.on('click', '#bidProjectButton', function (e) {
			
			
		// })

		_doc.on('click', '#addClassRoomProject', function (e) {
			console.log("hhe")
			$('#createProjectClass').modal('toggle');
		})
    }

	function leaveClass(){
		let classId = $(this).data('ing-classid');
		let data = {
			leaveClass: "1"
		};
		
		$.ajax({
            dataType: "json",
            type: "POST",
            data: data,
            success: function (d) {
				window.location = "/home"
				window.reload()
            },
            error: function () {
                console.log('error')
            }
        });
	}
    //After a project is added on the success call this to take this
    //back to that project page and load projetcs
    function onAddProject(id){
        let data = {
            projectClassId: id,
        };

        $.ajax({
            dataType: "json",
            type: "POST",
            data: data,
            success: function (d) {
                buildProjects(d);
            },
            error: function () {
                console.log('error')
            }
        });

        $('#exampleModal').modal('toggle');
        clearModal();
	}
	
	//Bid on Project
	function onBidProject(){
		
	}

    //Chnage the class name on the op nav bar
    function changeClassName(className) {
        document.getElementById("menu-name").innerHTML = className;
    }

    //Build the html of the projects using the data from the ajax call
    function buildProjects(data) {
        let elem = document.getElementById("projectPosts");
        elem.innerHTML = "";
        data.forEach(
            element => putIntoHTML(element)
        );

    }

    //Build the html from the function aobve
    function buildAddProjectButton(projectClassId, classId) {
        let id = document.getElementById("addProject");
        id.innerHTML = "";
        id.innerHTML = '<button class="btn btn-primary" " id="addProjectButton" data-id="' + projectClassId + '" data-classid="' + classId + '" data-toggle="modal" data-target="#exampleModal"">Add Project</button>';

        let id2 = document.getElementById("projectclassid");
        id2.value = projectClassId;
        let id3 = document.getElementById("classid");
        id3.value = classId;
    }

    //Build the html from the function aobve
    function putIntoHTML(element) {
        let elem = document.getElementById("projectPosts");
        let html = "";

		if(element.profilePicture == ""){
			if(element.singedinUserID == element.UserID){
				html = '<div class="message-2qnXI6 cozyMessage-3V1Y8y groupStart-23k01U wrapper-2a6GCs cozy-3raOZG zalgo-jN1Ica" id="chat-messages-755907437651034133" role="group" tabindex="-1"><div class="contents-2mQqc9" role="document"><img src="../../../assets/img/profilePictures/default.png" aria-hidden="true" class="threads-avatar-hack avatar-1BDn8e clickable-1bVtEA" alt=" "><h2 class="header-23xsNx threads-header-hack"><span class="username-1A8OIy clickable-1bVtEA focusable-1YV_-H" aria-controls="popout_76" aria-expanded="false" role="button" tabindex="0" style="color: rgb(52, 152, 219);">' + element.name + '</span><span class="timestamp-3ZCmNB"><span aria-label="Today at 5:46 PM">' + element.date + '</span><span class="rating">   '+ element.user_ranking + '/5 Stars</span></span></h2><div class="markup-2BOw-j messageContent-2qWWxC">' + element.projectname + '</div>	</div><div class="container-1ov-mD"></div><div class="buttonContainer-DHceWr"><div class="buttons-cl5qTG container-3npvBV isHeader-2dII4U" aria-label="Message Actions"><div class="wrapper-2aW0bm"><div class="button-1ZiXG9" aria-label="Add Reaction" aria-controls="popout_111" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M12.2512 2.00309C12.1677 2.00104 12.084 2 12 2C6.477 2 2 6.477 2 12C2 17.522 6.477 22 12 22C17.523 22 22 17.522 22 12C22 11.916 21.999 11.8323 21.9969 11.7488C21.3586 11.9128 20.6895 12 20 12C15.5817 12 12 8.41828 12 4C12 3.31052 12.0872 2.6414 12.2512 2.00309ZM10 8C10 6.896 9.104 6 8 6C6.896 6 6 6.896 6 8C6 9.105 6.896 10 8 10C9.104 10 10 9.105 10 8ZM12 19C15.14 19 18 16.617 18 14V13H6V14C6 16.617 8.86 19 12 19Z"></path><path d="M21 3V0H19V3H16V5H19V8H21V5H24V3H21Z" fill="currentColor"></path></svg></div><div class="button-1ZiXG9" aria-label="More" aria-controls="popout_112" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M7 12.001C7 10.8964 6.10457 10.001 5 10.001C3.89543 10.001 3 10.8964 3 12.001C3 13.1055 3.89543 14.001 5 14.001C6.10457 14.001 7 13.1055 7 12.001ZM14 12.001C14 10.8964 13.1046 10.001 12 10.001C10.8954 10.001 10 10.8964 10 12.001C10 13.1055 10.8954 14.001 12 14.001C13.1046 14.001 14 13.1055 14 12.001ZM19 10.001C20.1046 10.001 21 10.8964 21 12.001C21 13.1055 20.1046 14.001 19 14.001C17.8954 14.001 17 13.1055 17 12.001C17 10.8964 17.8954 10.001 19 10.001Z"></path></svg></div></div></div></div></div>';
			}else{
			html = '<div class="message-2qnXI6 cozyMessage-3V1Y8y groupStart-23k01U wrapper-2a6GCs cozy-3raOZG zalgo-jN1Ica" id="chat-messages-755907437651034133" role="group" tabindex="-1"><div class="contents-2mQqc9" role="document"><img src="../../../assets/img/profilePictures/default.png" aria-hidden="true" class="threads-avatar-hack avatar-1BDn8e clickable-1bVtEA" alt=" "><h2 class="header-23xsNx threads-header-hack"><span class="username-1A8OIy clickable-1bVtEA focusable-1YV_-H" aria-controls="popout_76" aria-expanded="false" role="button" tabindex="0" style="color: rgb(52, 152, 219);">' + element.name + '</span><span class="timestamp-3ZCmNB"><span aria-label="Today at 5:46 PM">' + element.date + '</span><span class="rating">   '+ element.user_ranking + '/5 Stars</span></span></h2><div class="markup-2BOw-j messageContent-2qWWxC">' + element.projectname + '</div>	<a style="float: right;" id="bidProject" data-name-value="' + element.name +'" data-projectID-value="' + element.projectID +'" data-askingPrice-value="' + element.askingprice +'"   href="#">Bid</a></div><div class="container-1ov-mD"></div><div class="buttonContainer-DHceWr"><div class="buttons-cl5qTG container-3npvBV isHeader-2dII4U" aria-label="Message Actions"><div class="wrapper-2aW0bm"><div class="button-1ZiXG9" aria-label="Add Reaction" aria-controls="popout_111" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M12.2512 2.00309C12.1677 2.00104 12.084 2 12 2C6.477 2 2 6.477 2 12C2 17.522 6.477 22 12 22C17.523 22 22 17.522 22 12C22 11.916 21.999 11.8323 21.9969 11.7488C21.3586 11.9128 20.6895 12 20 12C15.5817 12 12 8.41828 12 4C12 3.31052 12.0872 2.6414 12.2512 2.00309ZM10 8C10 6.896 9.104 6 8 6C6.896 6 6 6.896 6 8C6 9.105 6.896 10 8 10C9.104 10 10 9.105 10 8ZM12 19C15.14 19 18 16.617 18 14V13H6V14C6 16.617 8.86 19 12 19Z"></path><path d="M21 3V0H19V3H16V5H19V8H21V5H24V3H21Z" fill="currentColor"></path></svg></div><div class="button-1ZiXG9" aria-label="More" aria-controls="popout_112" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M7 12.001C7 10.8964 6.10457 10.001 5 10.001C3.89543 10.001 3 10.8964 3 12.001C3 13.1055 3.89543 14.001 5 14.001C6.10457 14.001 7 13.1055 7 12.001ZM14 12.001C14 10.8964 13.1046 10.001 12 10.001C10.8954 10.001 10 10.8964 10 12.001C10 13.1055 10.8954 14.001 12 14.001C13.1046 14.001 14 13.1055 14 12.001ZM19 10.001C20.1046 10.001 21 10.8964 21 12.001C21 13.1055 20.1046 14.001 19 14.001C17.8954 14.001 17 13.1055 17 12.001C17 10.8964 17.8954 10.001 19 10.001Z"></path></svg></div></div></div></div></div>';
			}
		}
		else{
			if(element.singedinUserID == element.UserID){
				html = '<div class="message-2qnXI6 cozyMessage-3V1Y8y groupStart-23k01U wrapper-2a6GCs cozy-3raOZG zalgo-jN1Ica" id="chat-messages-755907437651034133" role="group" tabindex="-1"><div class="contents-2mQqc9" role="document"><img src="../../../assets/img/profilePictures/' + element.profilePicture + '" aria-hidden="true" class="threads-avatar-hack avatar-1BDn8e clickable-1bVtEA" alt=" "><h2 class="header-23xsNx threads-header-hack"><span class="username-1A8OIy clickable-1bVtEA focusable-1YV_-H" aria-controls="popout_76" aria-expanded="false" role="button" tabindex="0" style="color: rgb(52, 152, 219);">' + element.name + '</span><span class="timestamp-3ZCmNB"><span aria-label="Today at 5:46 PM">' + element.date + '</span><span class="rating">   '+ element.user_ranking + '/5 Stars</span></span></h2><div class="markup-2BOw-j messageContent-2qWWxC">' + element.projectname + '</div></div><div class="container-1ov-mD"></div><div class="buttonContainer-DHceWr"><div class="buttons-cl5qTG container-3npvBV isHeader-2dII4U" aria-label="Message Actions"><div class="wrapper-2aW0bm"><div class="button-1ZiXG9" aria-label="Add Reaction" aria-controls="popout_111" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M12.2512 2.00309C12.1677 2.00104 12.084 2 12 2C6.477 2 2 6.477 2 12C2 17.522 6.477 22 12 22C17.523 22 22 17.522 22 12C22 11.916 21.999 11.8323 21.9969 11.7488C21.3586 11.9128 20.6895 12 20 12C15.5817 12 12 8.41828 12 4C12 3.31052 12.0872 2.6414 12.2512 2.00309ZM10 8C10 6.896 9.104 6 8 6C6.896 6 6 6.896 6 8C6 9.105 6.896 10 8 10C9.104 10 10 9.105 10 8ZM12 19C15.14 19 18 16.617 18 14V13H6V14C6 16.617 8.86 19 12 19Z"></path><path d="M21 3V0H19V3H16V5H19V8H21V5H24V3H21Z" fill="currentColor"></path></svg></div><div class="button-1ZiXG9" aria-label="More" aria-controls="popout_112" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M7 12.001C7 10.8964 6.10457 10.001 5 10.001C3.89543 10.001 3 10.8964 3 12.001C3 13.1055 3.89543 14.001 5 14.001C6.10457 14.001 7 13.1055 7 12.001ZM14 12.001C14 10.8964 13.1046 10.001 12 10.001C10.8954 10.001 10 10.8964 10 12.001C10 13.1055 10.8954 14.001 12 14.001C13.1046 14.001 14 13.1055 14 12.001ZM19 10.001C20.1046 10.001 21 10.8964 21 12.001C21 13.1055 20.1046 14.001 19 14.001C17.8954 14.001 17 13.1055 17 12.001C17 10.8964 17.8954 10.001 19 10.001Z"></path></svg></div></div></div></div></div>';
			}else{
				html = '<div class="message-2qnXI6 cozyMessage-3V1Y8y groupStart-23k01U wrapper-2a6GCs cozy-3raOZG zalgo-jN1Ica" id="chat-messages-755907437651034133" role="group" tabindex="-1"><div class="contents-2mQqc9" role="document"><img src="../../../assets/img/profilePictures/' + element.profilePicture + '" aria-hidden="true" class="threads-avatar-hack avatar-1BDn8e clickable-1bVtEA" alt=" "><h2 class="header-23xsNx threads-header-hack"><span class="username-1A8OIy clickable-1bVtEA focusable-1YV_-H" aria-controls="popout_76" aria-expanded="false" role="button" tabindex="0" style="color: rgb(52, 152, 219);">' + element.name + '</span><span class="timestamp-3ZCmNB"><span aria-label="Today at 5:46 PM">' + element.date + '</span><span class="rating">   '+ element.user_ranking + '/5 Stars</span></span></h2><div class="markup-2BOw-j messageContent-2qWWxC">' + element.projectname + '</div>	<a style="float: right;" id="bidProject" data-name-value="' + element.name +'" data-projectID-value="' + element.projectID +'" data-askingPrice-value="' + element.askingprice +'"   href="#">Bid</a></div><div class="container-1ov-mD"></div><div class="buttonContainer-DHceWr"><div class="buttons-cl5qTG container-3npvBV isHeader-2dII4U" aria-label="Message Actions"><div class="wrapper-2aW0bm"><div class="button-1ZiXG9" aria-label="Add Reaction" aria-controls="popout_111" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M12.2512 2.00309C12.1677 2.00104 12.084 2 12 2C6.477 2 2 6.477 2 12C2 17.522 6.477 22 12 22C17.523 22 22 17.522 22 12C22 11.916 21.999 11.8323 21.9969 11.7488C21.3586 11.9128 20.6895 12 20 12C15.5817 12 12 8.41828 12 4C12 3.31052 12.0872 2.6414 12.2512 2.00309ZM10 8C10 6.896 9.104 6 8 6C6.896 6 6 6.896 6 8C6 9.105 6.896 10 8 10C9.104 10 10 9.105 10 8ZM12 19C15.14 19 18 16.617 18 14V13H6V14C6 16.617 8.86 19 12 19Z"></path><path d="M21 3V0H19V3H16V5H19V8H21V5H24V3H21Z" fill="currentColor"></path></svg></div><div class="button-1ZiXG9" aria-label="More" aria-controls="popout_112" aria-expanded="false" role="button" tabindex="0"><svg class="icon-3Gkjwa" aria-hidden="false" width="24" height="24" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" clip-rule="evenodd" d="M7 12.001C7 10.8964 6.10457 10.001 5 10.001C3.89543 10.001 3 10.8964 3 12.001C3 13.1055 3.89543 14.001 5 14.001C6.10457 14.001 7 13.1055 7 12.001ZM14 12.001C14 10.8964 13.1046 10.001 12 10.001C10.8954 10.001 10 10.8964 10 12.001C10 13.1055 10.8954 14.001 12 14.001C13.1046 14.001 14 13.1055 14 12.001ZM19 10.001C20.1046 10.001 21 10.8964 21 12.001C21 13.1055 20.1046 14.001 19 14.001C17.8954 14.001 17 13.1055 17 12.001C17 10.8964 17.8954 10.001 19 10.001Z"></path></svg></div></div></div></div></div>';
			}
        	
		}
        $(elem).append(html);
    }

    //Function to clear the information out of the modals
    function clearModal(){
        let projectclassid = document.getElementById("projectclassid");
        projectclassid.value = "";
        let classid = document.getElementById("classid");
        classid.value = "";
        let projectname = document.getElementById("projectname");
        projectname.value = "";
        let content = document.getElementById("content");
        content.value = "";
        let price = document.getElementById("price");
        price.value = "";
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
                projectname: "required",
                content: "required",
                price: "required"
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
                    //window.location = d.redirect;

                    onAddProject(d.s)
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
	
	var _validate2 = {
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
                projectname: "classprojectname"
            }
        },
        init: function () {
            _frm2.validate(this.options);
        }
    };
	var _submit2 = {
        options: {
            url: location.href,
            type: 'post',
            dataType: 'json',
            beforeSubmit: function (arr, form, options) {

                $('.pgn-wrapper').remove();
                return _frm2.valid(); //call the validate plugin
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
            _frm2.ajaxForm(this.options);
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

	var _validate3 = {
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
                projectname: "bidAmount"
            }
        },
        init: function () {
            _frm3.validate(this.options);
        }
    };
	var _submit3 = {
        options: {
            url: location.href,
            type: 'post',
            dataType: 'json',
            beforeSubmit: function (arr, form, options) {

                $('.pgn-wrapper').remove();
                return _frm3.valid(); //call the validate plugin
            },
            success: function (d) {
                if (d.r == "fail") {
                    alert('fail')
                } else {
					$('#bidOnProjectModal').modal('toggle');
					_submit3.action.notify(d.type, d.message);
                }

            },
            error: function (d) {

            }
        },
        init: function () {
            _frm3.ajaxForm(this.options);
        },
        action: {
          notify: function (type, message) {
            $('.notify').pgNotification({
              style: 'flip',
              message: message,
              position: 'top-right',
              timeout: 2000,
              type: type
            }).show();
          }
        }
	};
	
	

    return {
        init: _init()
    };
})();