<?php

class CookieHelper
{
    const GHOSTLY_USER = 'ghostly_user';

    //Create a cookie
    public function getCookie()
    {
        return isset($_COOKIE[self::GHOSTLY_USER]) ?? $_COOKIE[self::GHOSTLY_USER]; // 86400 = 1 day
    }

    //Create a cookie
    public function setCookie($cookie_value, $time, $path)
    {
        setcookie(self::GHOSTLY_USER, $cookie_value, $time, $path); // 86400 = 1 day
    }

    //For now I've made this quite basic, in the future I'm probably going to want to return it as an array of objects to access the data better
    public function decodeCookie()
    {
        return json_decode($_COOKIE[self::GHOSTLY_USER]);
    }
}
