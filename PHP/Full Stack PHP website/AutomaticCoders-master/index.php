<?php 

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

ini_set('display_errors', 1);     # don't show any errors...
error_reporting(E_ALL ^ E_STRICT);  # ...but do log them

date_default_timezone_set('america/los_angeles');

session_start();

require('libraries/functions.php');
require('libraries/config.php');
require('libraries/boot.php');

require('libraries/database.php');
require('libraries/view.php');
require('libraries/controller.php');
require('libraries/model.php');

//Add in indvidual classes here
require('libraries/CookieHelper.php');
require('libraries/LoginHelper.php');
require('libraries/MessageHelper.php');
require('libraries/MenuHelper.php');

$app = new Boot();

session_write_close();
?>