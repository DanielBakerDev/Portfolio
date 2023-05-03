<?php

class Home extends Controller
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
		$this->classes 		= $menu->getAllClasses();
		$this->classesNotIn = $menu->getAllClassesNotIn();

        $this->numClasses = $this->model->getNumberOfClasses();

        // if (!empty($_POST['enterRoom'])) {
        //     echo json_encode($this->model->enterRoom($_POST['enterRoom']));
        //     exit;
        // }

        if (!empty($_POST['classId']) && !empty($_POST['classPassword'])) {
            echo json_encode($this->model->joinRoom());
            exit;
        }


        $this->title = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }

    public function create()
    {

        if (!empty($_POST['className']) && !empty($_POST['institution']) && !empty($_POST['description']) && !empty($_POST['password'])) {
            echo json_encode($this->model->createClass());
            exit;
        }

        $menu = new MenuHelper();
        $this->classes = $menu->getAllClasses();
        
        $this->title = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('create');
        $this->loadFooter();
    }
}
