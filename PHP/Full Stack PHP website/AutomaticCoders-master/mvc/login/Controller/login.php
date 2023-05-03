<?php

class Login extends Controller
{

    function __construct()
    {
        parent::__construct();
    }

    public function index()
    {
        //If logged in send us to the home screen
        if (isset($_SESSION['automatic_email'])) {
            header('location: ' . _SITEROOT_ . 'home');
            exit;
        }

        if (!empty($_POST['username']) && !empty($_POST['password'])) {
            $login = $this->model->process();
            echo json_encode($login);
            exit;
        }

        $this->title = "Automatic Coders";
        $this->noMenu = true;
        $this->loadPage();
        $this->render('index');
        $this->loadFooter();
    }

    public function logout()
    {
        $cookieHelper     = new CookieHelper();

        unset($_SESSION['automatic_user_id']);
        unset($_SESSION['automatic_username']);
        unset($_SESSION['automatic_name']);

        if (!isset($_COOKIE['site_user'])) {
            header("refresh:0;" . _SITEROOT_ . "login");
        }
        exit;
    }
}