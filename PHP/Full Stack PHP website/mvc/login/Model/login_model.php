<?php


class Login_Model extends Model
{
    
	const MEMBERTABLE = 'users';
	const MEMBERRECORDS = 'id,username,password,name,profilePicture,isProf,isStud';
    public function __construct()
    {
        parent::__construct();
		$this->cookieHelper = new CookieHelper();
    }

    public function process()
	{
		$loginHelper = new LoginHelper();
		
		//check if username is in a cookie
		if($this->checkCookie()) {
			//there is a cookie
			$_POST['username'] = $this->getUserName();
		}

		if (empty($_POST['username']) && empty($_POST['password'])) {
			return [
				"r"	      => "fail",
				"type"    => "danger",
				"message" => "Sorry internal error (21222)"
			];
		}

		$username = $this->eqDb->escape($_POST['username']);
		$password = $this->eqDb->escape($_POST['password']);
		//$password = md5($password);
		$this->eqDb->where("(username = ?)", [$username]);
		$this->eqDb->where('password', $password);
		$members = $this->eqDb->get(
			self::MEMBERTABLE,
			null,
			self::MEMBERRECORDS
		);

		$this->unsetSessionVariables();

		if (count($members) == 1) {

			$user = $members[0];

			if($user['isStud'] == 1){
				$_SESSION['automatic_user_id']         = $user['id'];
				$_SESSION['automatic_username']        = $user['username'];
				$_SESSION['automatic_name']            = $user['name'];
				$_SESSION['automatic_profile_pic']     = $user['profilePicture'];
	
				if($_SESSION['automatic_profile_pic'] == "" || $_SESSION['automatic_profile_pic'] == NULL){
					$_SESSION['automatic_profile_pic'] = "default.png";
				}
	
				// check if remember me was checked.
				if(isset($_POST['remember-me'])) {
					$this->setRememberMeCookie($user);
				}
	
				return  [
					"r" 	   => "success",
					"redirect" => _SITEROOT_ . "home"
				];
			}
			else if($user['isProf'] == 1){

				$_SESSION['automatic_teacher_id']         = $user['id'];
				$_SESSION['automatic_teacher_username']   = $user['username'];
				$_SESSION['automatic_teacher_name']       = $user['name'];
				$_SESSION['automatic_profile_pic']     	  = $user['profilePicture'];

					// check if remember me was checked.
					if(isset($_POST['remember-me'])) {
						$this->setRememberMeCookie($user);
					}
		
					return  [
						"r" 	   => "success",
						"redirect" => _SITEROOT_ . "teacher"
					];
			}

		}

		return [
			"r"       => "fail",
			"type"    => "danger",
			"message" => "<strong>Error: </strong> Wrong Email or Password"
		];
    }
    
	public function setRememberMeCookie($user) {		
		$name = $user['name'];
		$arr = json_encode(array('name'=>$name,'username'=>$user['username']));
		$this->cookieHelper->setCookie($arr, time() + (86400 * 30), "/");
	}

	public function unsetSessionVariables(){
		$_SESSION['automatic_user_id']         	  = "";
		$_SESSION['automatic_username']        	  = "";
		$_SESSION['automatic_name']            	  = "";
		$_SESSION['automatic_profile_pic']     	  = "";
		$_SESSION['automatic_teacher_id']         = "";
		$_SESSION['automatic_teacher_username']   = "";
		$_SESSION['automatic_teacher_name']       = "";
		$_SESSION['automatic_profile_pic']     	  = "";
	}

	public function getUser() {
		return $this->cookieHelper->decodeCookie()->{'name'};
	}

	public function getUserName() {
		return $this->cookieHelper->decodeCookie()->{'username'};
	}

	public function checkCookie() {
		return $this->cookieHelper->getCookie();
	}

	public function forgetMe() {
		// unset it
		$this->cookieHelper->setCookie("", time() - 3600, "/");
	}
}