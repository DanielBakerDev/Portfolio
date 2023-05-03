<?php


class Admin_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
    }

    //------------------------------------------------------------------------------------
    //User
    public function getAllUsers()
    {
        $query = "SELECT * FROM users
        ORDER BY id";

        $response = $this->eqDb->rawQuery($query);

        return $response;
    }

    public function getAllUserInformation($id)
    {
        $query = "SELECT * FROM users
        WHERE id = $id";

        $response = $this->eqDb->rawQuery($query);

        return $response[0];
    }

    public function updateUser($id)
    {

        $name              = $this->eqDb->escape($_POST['name']);
        $email             = $this->eqDb->escape($_POST['email']);
        $username          = $this->eqDb->escape($_POST['username']);
        $password          = $this->eqDb->escape($_POST['password']);
        $profilePicture    = $this->eqDb->escape($_POST['profilePicture']);

        //Form is being a little sfduhafshfasi so I have to do this bullshit
        $isStudent = 0;
        $isProffessor = 0;
        if ($_POST['isStudent'])
            $isStudent         = $this->eqDb->escape($_POST['isStudent']);

        if (isset($_POST['isProffessor']))
            $isProffessor         = $this->eqDb->escape($_POST['isProffessor']);


        $data = [
            'name'           => $name,
            'email'          => $email,
            'username'       => $username,
            'password'       => $password,
            'profilePicture' => $profilePicture,
            'isStud'         => $isStudent,
            'isProf'         => $isProffessor
        ];

        $this->eqDb->where('id', $id);
        if ($this->eqDb->update('users', $data)) {

            return [
                'r'       => 'success',
                'type'    => 'success',
                'message'    => 'Success User updated',
                "redirect" => _SITEROOT_ . "admin"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
    }

    public function deleteUser(int $id)
    {
        $id = $this->eqDb->escape($id);

        //Delete user from classrooms
        $query = "SELECT * FROM classusers
		WHERE userId = $id";

        $response = $this->eqDb->rawQuery($query);

        for ($i = 0; $i < count($response); $i++) {
            $this->eqDb->where('userId', $response[$i]['userId']);
            $this->eqDb->delete('classusers');
        }

        //Delete user 
        $this->eqDb->where('id', $id);
        if ($this->eqDb->delete('users')) {
            return [
                'r'       => 'sucess',
                'type'    => 'success',
                'message'    => 'Success - User Deleted',
                'message' => '<strong>Success!</strong> Company saved.'
            ];
        }
        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
    }

    //------------------------------------------------------------------------------------
    //Class
    public function getAllClasses()
    {
        $query = "SELECT * FROM class
        ORDER BY dateCreated";

        $response = $this->eqDb->rawQuery($query);

        return $response;
    }

    public function deleteClass(int $id)
    {
        $id = $this->eqDb->escape($id);

        $query = "SELECT * FROM classusers
		WHERE classid = $id";

        $response = $this->eqDb->rawQuery($query);

        for ($i = 0; $i < count($response); $i++) {
            $this->eqDb->where('id', $response[$i]['id']);
            $this->eqDb->delete('classusers');
        }

        $this->eqDb->where('id', $id);
        if ($this->eqDb->delete('class')) {
            return [
                'r'       => 'sucess',
                'type'    => 'success',
                'message'    => 'Success - Class Deleted',
                'message' => '<strong>Success!</strong> Class deleted.'
            ];
        }
        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
    }

    public function getAllClassInformation($id)
    {
        $query = "SELECT * FROM class
        WHERE id = $id";

        $response = $this->eqDb->rawQuery($query);

        return $response[0];
    }

    public function updateClass($id)
    {

        $name              = $this->eqDb->escape($_POST['name']);
        $institution       = $this->eqDb->escape($_POST['institution']);
        $description       = $this->eqDb->escape($_POST['description']);
        $password          = $this->eqDb->escape($_POST['password']);
        $profilePicture    = $this->eqDb->escape($_POST['profilePicture']);

        $data = [
            'className'      => $name,
            'institution'    => $institution,
            'description'    => $description,
            'classPassword'  => $password,
            'profilePicture' => $profilePicture
        ];

        $this->eqDb->where('id', $id);
        if ($this->eqDb->update('class', $data)) {

            return [
                'r'       => 'sucess',
                'type'    => 'success',
                'message'    => 'Success - Class Updated',
                "redirect" => _SITEROOT_ . "admin"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
    }

    //------------------------------------------------------------------------------------
    //
}