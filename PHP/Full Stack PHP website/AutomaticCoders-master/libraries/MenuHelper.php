<?php

class MenuHelper
{


    //Create a cookie
    public function getAllClasses()
    {
        global $eqDb;
        $this->eqDb = $eqDb;

        //$response = $this->eqDb->get("class", null, "*");
        $userid = $_SESSION['automatic_user_id'];
        $query = "SELECT 
            className,
            institution,
            dateCreated,
            description,
            class.profilePicture AS profilePicture,
            class.id AS classid,
            COUNT(DISTINCT users.id) AS numUsers,
            IF(userId = $userid,'1','0') AS inClass
            FROM class
            inner JOIN classusers ON class.id=classusers.classid
            inner JOIN users ON classusers.userid = users.id
            GROUP BY class.id
            ORDER BY dateCreated";
        $response = $this->eqDb->rawQuery($query);

        $query2 = "SELECT * FROM classusers";
        $response2 = $this->eqDb->rawQuery($query2);

        for ($i = 0; $i < count($response); $i++) {
            for ($p = 0; $p < count($response2); $p++) {
                if ($response[$i]['classid'] == $response2[$p]['classid'] && $userid == $response2[$p]['userId']) {
                    $response[$i]['inClass'] = 1;
                }
            }
		}


        return $response;
	}
	
	public function getAllClassesNotIn()
    {
		global $eqDb;
        $this->eqDb = $eqDb;


		$userid = $_SESSION['automatic_user_id'];
		

		$query2 = "SELECT c.id, c.className, c.institution, c.profilePicture
		 FROM class c
		LEFT join classusers cu ON c.id = cu.classid";
		$response2 = $this->eqDb->rawQuery($query2);

		$query = "SELECT c.id FROM class c
		inner JOIN classusers cu ON c.id = cu.classid    
		WHERE cu.userId = $userid";
		$response = $this->eqDb->rawQuery($query);


		$classes = [];
		$InClass = false;

		for ($i = 0; $i < count($response2); $i++) {
            for ($p = 0; $p < count($response); $p++) {
                if ($response[$p]['id'] == $response2[$i]['id'] ) {
                    $InClass = true;
                }
			}
			
			if($InClass == false){
				array_push($classes, $response2[$i]);
			}
			$InClass = false;
		}
		
		return $classes;
	}
}