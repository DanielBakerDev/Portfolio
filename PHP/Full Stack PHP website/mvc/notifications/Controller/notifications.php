<?php

class Notifications extends Controller
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
        if (!empty($_POST['userid']) && !empty($_POST['type']) && !empty($_POST['message'])) {
            echo json_encode($this->model->createMsg());
            exit;
		}

		if (!empty($_POST['markNotifcationAsRead'])) {
            echo json_encode($this->model->markNotificationAsRead($_POST['markNotifcationAsRead']));
            exit;
		}

		if (!empty($_POST['markMessageAsRead'])) {
            echo json_encode($this->model->markMessageAsRead($_POST['markMessageAsRead']));
            exit;
		}

		if (!empty($_POST['acceptBID'])) {
            echo json_encode($this->model->acceptBid($_POST['acceptBID']));
            exit;
		}
		
		if (!empty($_POST['rejectBID'])) {
            echo json_encode($this->model->declineBid($_POST['rejectBID']));
            exit;
		}

		$menu = new MenuHelper();
		$this->classes       		= $menu->getAllClasses();

		$this->users 				= $this->model->getallUsers();
        
        $this->messages				= $this->model->getallMessagesUnread();
		$this->messagesRead 		= $this->model->getallMessagesRead(); //Use this after
		$this->projectBids			= $this->model->getallBids();
		
		$this->notificationsUnread	= $this->model->getallNotificationsUnread();
        $this->notificationsRead 	= $this->model->getallNotificationsRead(); //Use this after

        $this->title  = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
	}
	
	public function messages()
    {
		if (!empty($_POST['markMessageAsRead'])) {
            echo json_encode($this->model->markMessageAsRead($_POST['markMessageAsRead']));
            exit;
		}

		if (!empty($_POST['userid']) && !empty($_POST['type']) && !empty($_POST['message'])) {
            echo json_encode($this->model->createMsg());
            exit;
		}
		
		$menu = new MenuHelper();
		$this->classes       		= $menu->getAllClasses();
		$this->users 				= $this->model->getallUsers();

		$this->messages				= $this->model->getallMessagesUnread();
		$this->messagesRead 		= $this->model->getallMessagesRead(); //Use this after

		$this->title  = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('messages');
        $this->loadFooter();
	}

	public function notify()
    {
		if (!empty($_POST['markNotifcationAsRead'])) {
            echo json_encode($this->model->markNotificationAsRead($_POST['markNotifcationAsRead']));
            exit;
		}

		$menu = new MenuHelper();
		$this->classes       		= $menu->getAllClasses();

		$this->notificationsUnread	= $this->model->getallNotificationsUnread();
		$this->notificationsRead 	= $this->model->getallNotificationsRead(); //Use this after
		
		$this->title  = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('notify');
        $this->loadFooter();
	}

	public function bids()
    {
		if (!empty($_POST['acceptBID'])) {
            echo json_encode($this->model->acceptBid($_POST['acceptBID']));
            exit;
		}
		
		if (!empty($_POST['rejectBID'])) {
            echo json_encode($this->model->declineBid($_POST['rejectBID']));
            exit;
		}

		if (!empty($_POST['ranking1']) && !empty($_POST['ranking2']) && !empty($_POST['ranking3']) && !empty($_POST['grade']) ) {
            echo json_encode($this->model->submitReview());
            exit;
		}

		

		$menu = new MenuHelper();
		$this->classes       		= $menu->getAllClasses();
		
		$this->projectBids			= $this->model->getallBids();
		$this->projectCode          = $this->model->getAccceptedProjectCode();

		$this->title  = "Automatic Enterprises";
        $this->noMenu = false;
        $this->loadPage();
        $this->render('bids');
        $this->loadFooter();
	}

		
}