<?php

class Database
{

	public function __construct()
	{

		global $config;
		global $eqDb;

		$eqDb = new MysqliDb("localhost", "root", "", "automatic");
	}
}