<?php

class Functions
{

    function __construct()
    {
    }

    public function jsonDecode($str)
    {
        if ($str == NULL) {
            return [];
        }

        return json_decode($str, 1);
    }

    public function redirect($r)
    {
        header("Location: " . $r);
    }

    public function now()
    {
        return date('Y-m-d h:i:s');
    }
}
