<?php


class GiveBack_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
    }


}