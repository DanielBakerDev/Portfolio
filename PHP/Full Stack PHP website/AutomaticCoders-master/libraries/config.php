<?php

class Config
{
    function __construct()
    {
        define('_SITEROOT_', '/');

        define('_TMPL_', "template/");
		define('_RES_',  "assets/");
		define('_PLUG_',  "/mvc/");
		define('_UPLOADS_', "/");
		define('_ASSETS_',  "assets/");
		define('_SITEBODY_', _TMPL_ . "header.php");
		define('_SITEFOOT_', _TMPL_ . "footer.php");
    }
}
