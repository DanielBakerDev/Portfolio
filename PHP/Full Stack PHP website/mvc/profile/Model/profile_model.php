<?php


class Profile_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
    }

    public function getUserInfo($id)
    {
        $query = "SELECT * FROM users WHERE id = $id ";
        $response = $this->eqDb->rawQuery($query);

        return $response[0];
    }

    public function updateUser($id)
    {

        $name              = $this->eqDb->escape($_POST['name']);
        $email             = $this->eqDb->escape($_POST['email']);
        $username          = $this->eqDb->escape($_POST['username']);
        $password          = $this->eqDb->escape($_POST['password']);
        //$profilePicture    = $this->eqDb->escape($_POST['profilePicture']);



        $data = [
            'name'           => $name,
            'email'          => $email,
            'username'       => $username,
            'password'       => $password,
            //'profilePicture' => $profilePicture
        ];

        $this->eqDb->where('id', $id);
        if ($this->eqDb->update ('users', $data)) {
        	return [
				'r'       => 'sucess',
				"redirect" => _SITEROOT_ . "profile",
        		'message' => '<strong>Success!</strong> Company saved.'
        	];	
        }

        return [
        	'r'       => 'fail',
        	'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
    }
}
