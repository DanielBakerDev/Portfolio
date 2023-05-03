<?php

class Profile extends Controller
{

    function __construct()
    {
        if (empty($_SESSION['automatic_user_id'])) {
            header('location: /login');
            exit;
        }
        parent::__construct();
    }

    public function index()
    {
        $menu = new MenuHelper();
        $this->classes = $menu->getAllClasses();
        $this->user = $this->model->getUserInfo($_SESSION['automatic_user_id']);


        $this->title = "Automatic";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }

    public function edituser()
    {
        if (!empty($_POST['name']) && !empty($_POST['email']) && !empty($_POST['username']) && !empty($_POST['password'])) {
            echo json_encode($this->model->updateUser($_SESSION['automatic_user_id']));
            exit;
        }

        $menu = new MenuHelper();
        $this->classes = $menu->getAllClasses();
        $this->user = $this->model->getUserInfo($_SESSION['automatic_user_id']);

        $this->title = "Automatic";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('edituser');
        $this->loadFooter();
    }
}