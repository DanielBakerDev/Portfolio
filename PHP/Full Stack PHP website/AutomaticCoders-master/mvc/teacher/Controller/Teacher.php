<?php

class Teacher extends Controller
{

    function __construct()
    {
        if (empty($_SESSION['automatic_teacher_id'])) {
            header('location: /login');
            exit;
        }
        parent::__construct();
    }

    public function index()
    {
		if (!empty($_POST['codeSet1']) && !empty($_POST['codeSet2'])) {
            echo json_encode($this->model->testCode($_POST['codeSet1'], $_POST['codeSet2']));
            exit;
		}
		
        $this->title = "Automatic Coders";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }
}
