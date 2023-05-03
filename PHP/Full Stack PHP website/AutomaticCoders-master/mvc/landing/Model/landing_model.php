<?php


class Landing_Model extends Model
{

    const MEMBERTABLE = 'users';
    const MEMBERRECORDS = 'id,username,password,name, profilePicture';
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
        parent::__construct();
        $this->cookieHelper = new CookieHelper();
    }

    public function createAccount()
    {
        $name           = $this->eqDb->escape($_POST['name']);
        $email          = $this->eqDb->escape($_POST['email']);
        $username       = $this->eqDb->escape($_POST['username']);
        $password       = $this->eqDb->escape($_POST['password']);
        $accountType    = $this->eqDb->escape($_POST['accountType']);
        $isProf         = 0;
        $isStud         = 0;

        if ($accountType == "Student") {
            $isStud = 1;
        } else {
            $isProf = 1;
        }

        $image = $_FILES["userfile"]["name"];
        $uploaddir =  'assets/img/profilePictures/';
        $uploadfile = $uploaddir . basename($_FILES['userfile']['name']);
        //Lets get a random number for the id
        // $_FILES['userfile']['tmp_name'] = rand();
        if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) {
        } else {
            return [
                'r'        => 'fail',
                'message' => '<strong>Error!</strong> Internal Error (21233)'
            ];
        }

        $id = $this->eqDb->insert('users', [
            'name'           => $name,
            'email'          => $email,
            'username'       => $username,
            'password'       => $password,
            'isProf'         => $isProf,
            'isStud'         => $isStud,
            'profilePicture' => $_FILES['userfile']['name'],
        ]);

        if ($id) {
            //Login 
            $this->process($username, $password);
            if ($isProf == 1) {
                return  [
                    "r"        => "success",
                    "redirect" => _SITEROOT_ . "teacher"
                ];
            } else {
                return  [
                    "r"        => "success",
                    "redirect" => _SITEROOT_ . "home"
                ];
            }
        } else {
            return [
                'r'        => 'fail',
                'message' => '<strong>Error!</strong> Internal Error (21233)'
            ];
        }
    }

    public function process($username, $password)
    {
        $loginHelper = new LoginHelper();

        //$password = md5($password);
        $this->eqDb->where("(username = ?)", [$username]);
        $this->eqDb->where('password', $password);
        $members = $this->eqDb->get(
            self::MEMBERTABLE,
            null,
            self::MEMBERRECORDS
        );


        if (count($members) == 1) {

            $user = $members[0];

            //company does exist
            // $this->method(); //checks if client has
            $_SESSION['automatic_user_id']         = $user['id'];
            $_SESSION['automatic_username']        = $user['username'];
            $_SESSION['automatic_name']            = $user['name'];
            $_SESSION['automatic_profile_pic']     = $user['profilePicture'];
        }
    }
}