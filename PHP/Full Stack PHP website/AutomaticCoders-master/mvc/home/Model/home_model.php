<?php


class Home_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
        parent::__construct();
        $this->messageHelper = new MessageHelper();
    }

    public function getNumberOfClasses()
    {
        $query = "SELECT 
        COUNT(className) AS numClass
        FROM class";

        $response = $this->eqDb->rawQuery($query);

        return $response;
    }

    public function enterRoom($id)
    {
        if (!isset($id) || $id != 0) {
            return [
                'r'        => 'success',
                "redirect" => _SITEROOT_ . "classroom/" . $id
            ];
        }
        return [
            "r"       => "fail",
            'message' => '<strong>Error!</strong> Internal Error (21233)'
        ];
    }

    public function joinRoom()
    {
        $ids           = $this->eqDb->escape($_POST['classId']);
        $password      = $this->eqDb->escape($_POST['classPassword']);


        //Now lets check if the password is correct for the class
        $this->eqDb->where('id', $ids);
        $this->eqDb->where('classPassword', $password);
        $class = $this->eqDb->get('class', null, '*');

        //If correct lets add them to the room
        if (!empty($class)) {
            $id = $this->eqDb->insert('classusers', [
                'userId'      => $_SESSION['automatic_user_id'],
                'classid'     => $ids,
                'isModerator' => 0
            ]);
            if ($id) {
                $this->messageHelper->sendJoinMessageToClassModerator($ids, $_SESSION['automatic_user_id']);
                return [
                    'r'        => 'success',
                    "redirect" => _SITEROOT_ . "classroom/" . $ids
                ];
            } else {
                return [
                    'r'        => 'fail',
                    'message' => '<strong>Error!</strong> Internal Error (21233)'
                ];
            }
        } else {
            return [
                "r"       => "fail",
                'message' => '<strong>Error!</strong> Incorrect password '
            ];
        }
    }

    public function createClass()
    {
        $className      = $this->eqDb->escape($_POST['className']);
        $institution    = $this->eqDb->escape($_POST['institution']);
        $description    = $this->eqDb->escape($_POST['description']);
        $password       = $this->eqDb->escape($_POST['password']);

        //Image portion
        $image = $_FILES["userfile"]["name"];
        $uploaddir =  'assets/img/classPictures/';
        $uploadfile = $uploaddir . basename($_FILES['userfile']['name']);
        //Lets get a random number for the id
        // $_FILES['userfile']['tmp_name'] = rand();
        if (move_uploaded_file($_FILES['userfile']['tmp_name'], $uploadfile)) {
            
        }
        else{
            return [
                'r'        => 'fail',
                'message' => '<strong>Error!</strong> Internal Error (21233)'
            ];
        }

        $id = $this->eqDb->insert('class', [
            'className'      => $className,
            'institution'    => $institution,
            'description'    => $description,
            'classPassword'  => $password,
            'profilePicture' => $_FILES['userfile']['name'],
            'dateCreated'    => date("Y/m/d")
        ]);

        if ($id) {
            //If class created lets add the creator to the class 
            $classUser = $this->eqDb->insert('classusers', [
                'userId'      => $_SESSION['automatic_user_id'],
                'classid'     => $id,
                'isModerator' => 1
            ]);

            if ($classUser) {
                return  [
                    "r"        => "success",
                    "redirect" => _SITEROOT_ . "classroom/" . $id
                ];
            } else {
                return [
                    'r'        => 'fail',
                    'message' => '<strong>Error!</strong> Internal Error (21233)'
                ];
            }
        } else {
            return [
                'r'        => 'fail',
                'message' => '<strong>Error!</strong> Internal Error (21233)'
            ];
        }
    }
}
