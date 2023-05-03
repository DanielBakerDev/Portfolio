<?php

class GiveBack extends Controller
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

        $this->title = "Automatic Coders";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }
}
