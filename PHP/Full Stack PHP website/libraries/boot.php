<?php
require_once('vendor/autoload.php');

class Boot
{
    function __construct()
    {
        global $equrl;
        global $config;
        
        $equrl = isset($_GET['v']) ? $_GET['v'] : null;
        $equrl = rtrim($equrl, '/');
        $equrl = rtrim($equrl, '-');
        $equrl = explode('/', $equrl);
        
        $this->db = new Database();
        new Config();

        $equrl[0] = empty($equrl[0]) ? 'landing' : $equrl[0];
        $newUrl   = $this->camelCaseUrl($equrl[0]);
        $file     = 'mvc/' . $newUrl . '/Controller/' . strtolower($newUrl) . '.php';
          
        if (file_exists($file)) {
            require $file;

            if(class_exists(ucfirst($newUrl))) {

                $controller = new $newUrl;
                $controller->loadModel($equrl[0]);
                // calling methods, if the url has more than 2 parameters
                // run method with
                if (count($equrl) > 2) {
                    if (method_exists($controller, $this->camelCaseUrl($equrl[1]))) {
                        $method = $equrl;
                        unset($method[0], $method[1]);
                        $customUrl = implode("/", $method);
                        $controller->{$this->camelCaseUrl($equrl[1])}($customUrl);
                    } else {
                        $controller->index($equrl[1]);
                    }
                } else {
                    if (isset($equrl[1])) {
                        // if url has two parameters ie.e /test/abc
                        if (method_exists($controller, $this->camelCaseUrl($equrl[1]))) {
                            //if that method 'abc' exists in that controller, then run that method,
                            $controller->{$this->camelCaseUrl($equrl[1])}();
                        } else {
                            //if that method 'abc' doesn't exist, check first if the index() method requires parameters
                            $reflection = new ReflectionMethod($controller, 'index');
                            if(count($reflection->getParameters())) {
                                //if the index method requires parameters, then plug 'abc' as an parameteter in index() i.e. index('abc')
                                $controller->index($equrl[1]);
                                $invokingIndex = true;

                            } else { 
                                //if that index method doesn't require a parameter, then send our user to a 404.
                                $this->error();
                            }
                        }
                    } else {
                        //run index method
                        $controller->index();
                    }
                }
            } else {
                $this->error();
            }
        } else {
            $this->error();
        }

        //header('Location: mvc/blog/views/index.php');
    }

    function camelCaseUrl($url) {
        $a = explode("-", $url);

        $str = "";
        foreach ($a as $key => $val) {
            $str .= ($key > 0 ? ucfirst($val) : $val);
        }
        return $str;
    }

    function error() {

        global $equrl;

        $equrl[0] = "whoops";
        
        require 'mvc/whoops/Controller/whoops.php';
        $error = new Whoops();
        $error->index();
      
        return false;
    }
}
