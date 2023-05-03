<?php

class Classroom extends Controller
{

    function __construct()
    {
        if (empty($_SESSION['automatic_user_id'])) {
            header('location: /login');
            exit;
        }
        parent::__construct();
    }

    public function index($id)
    {
        if (!empty($_POST['projectClassId'])) {
            echo json_encode($this->model->getAllProjects($_POST['projectClassId']));
            exit;
        }

        if (!empty($_POST['projectname']) || !empty($_POST['content']) || !empty($_POST['price']) || !empty($_POST['projectclassid'])) {
            echo json_encode($this->model->addProject());
            exit;
        }
		
		if (!empty($_POST['leaveClass'])) {
			echo json_encode($this->model->leaveClass($id));
            exit;
		}

		if (!empty($_POST['classprojectname']) || !empty($_POST['classid'])) {
			echo json_encode($this->model->addProjectClass($id));
            exit;
		}

		if (!empty($_POST['bidAmount'])) {
			echo json_encode($this->model->bidOnProject($id));
            exit;
		}

		$this->classId        = $id;
        $this->classProjects  = $this->model->getAllClassProject($id);
        $this->className      = $this->model->getClassRoomName($id);

        $menu = new MenuHelper();
        $this->classes = $menu->getAllClasses();

        $this->title = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }
}
