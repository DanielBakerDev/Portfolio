<?php

class Controller extends View
{

    function __construct()
    {
        global $equrl;
        global $eqDb;

        $this->eqDb = $eqDb;
        $this->equrl = $equrl;
    }

    public function loadModel($name)
    {

        global $url;
        $this->Apps = new Functions();

        $path = 'mvc/' . $name . '/Model/' . $name . '_model.php';

        if (file_exists($path)) {

            require $path;
            $modelName   = ucfirst($name) . '_Model';
            $this->model = new $modelName();
        }
    }
}
