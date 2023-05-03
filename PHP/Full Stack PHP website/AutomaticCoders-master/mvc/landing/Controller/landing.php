<?php

class Landing extends Controller
{

    function __construct()
    {
        parent::__construct();
    }

    public function index()
    {

        if (!empty($_POST['name']) && !empty($_POST['email']) && !empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['accountType'])) {
            echo json_encode($this->model->createAccount());
            exit;
        }

        $this->title = "Automatic Enterprises";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }
}