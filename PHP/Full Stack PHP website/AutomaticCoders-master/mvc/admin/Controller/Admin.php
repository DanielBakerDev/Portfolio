<?php

class Admin extends Controller
{

    function __construct()
    {
        parent::__construct();
    }

    public function index()
    {

        $this->title = "Automatic Coders Admin";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('index');
        // $this->loadFooter();
    }

    public function users()
    {
        if (isset($_POST['userId'])) {
            echo json_encode($this->model->deleteUser($_POST['userId']));
            exit;
        }

        $this->users = $this->model->getAllUsers();

        $this->title = "Automatic Coders Admin";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('users');
        // $this->loadFooter();
    }

    public function edituser($id)
    {
        if (!empty($_POST['name']) && !empty($_POST['email']) && !empty($_POST['username']) && !empty($_POST['password'])) {
            echo json_encode($this->model->updateUser($id));
            exit;
        }

        $this->user = $this->model->getAllUserInformation($id);

        $this->title = "Automatic Coders Admin";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('edituser');
        // $this->loadFooter();
    }

    public function class()
    {
        if (isset($_POST['userId'])) {
            echo json_encode($this->model->deleteClass($_POST['userId']));
            exit;
        }

        $this->classes = $this->model->getAllClasses();

        $this->title = "Automatic Coders Admin";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('classes');
        // $this->loadFooter();
	}
	
	public function editclass($id)
    {
		if (!empty($_POST['name']) && !empty($_POST['institution']) && !empty($_POST['description']) && !empty($_POST['password']) && !empty($_POST['profilePicture'])) {
            echo json_encode($this->model->updateClass($id));
            exit;
        }

		$this->class = $this->model->getAllClassInformation($id);
		
		$this->title = "Automatic Coders Admin";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('editclass');
        // $this->loadFooter();
	}
}
