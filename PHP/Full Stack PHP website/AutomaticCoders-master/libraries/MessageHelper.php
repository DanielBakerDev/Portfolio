<?php

class MessageHelper
{

    public function sendJoinMessageToClassModerator($classId, $joinedUserId)
    {
        global $eqDb;
        $this->eqDb = $eqDb;

        $query1 = "SELECT * FROM class WHERE id = $classId";
        $classData = $this->eqDb->rawQuery($query1);
        $className = $classData[0]['className'];
        $classId = $classData[0]['id'];

        $query2 = "SELECT * FROM users WHERE id = $joinedUserId";
        $userData = $this->eqDb->rawQuery($query2);
        $userName = $userData[0]['name'];

        //Lets find the moderator of this class
        $query3 = "SELECT 
            users.id AS id
            FROM users
            INNER JOIN classusers ON users.id = classusers.userId
            INNER JOIN class ON classusers.classid = class.id
            WHERE class.id = $classId
            AND classusers.isModerator =1";
		$modData = $this->eqDb->rawQuery($query3);

		if($modData != null){
			$modId = $modData[0]['id'];

			//Lets create the message to send them
			$message = "$userName has joined $className!";
	
			$id = $this->eqDb->insert('notifications', [
				'userid'     => $modId,
				'type'       => "Join",
				'message'    => $message,
				'isread'       => 0,
				'date'       => date("Y/m/d")
			]);
		}

	}
	
	public function sendLeaveClass($classId, $joinedUserId){
		global $eqDb;
        $this->eqDb = $eqDb;

        $query1 = "SELECT * FROM class WHERE id = $classId";
        $classData = $this->eqDb->rawQuery($query1);
        $className = $classData[0]['className'];
        $classId = $classData[0]['id'];

        $query2 = "SELECT * FROM users WHERE id = $joinedUserId";
        $userData = $this->eqDb->rawQuery($query2);
        $userName = $userData[0]['name'];

        //Lets find the moderator of this class
        $query3 = "SELECT 
            users.id AS id
            FROM users
            INNER JOIN classusers ON users.id = classusers.userId
            INNER JOIN class ON classusers.classid = class.id
            WHERE class.id = $classId
            AND classusers.isModerator =1";
		$modData = $this->eqDb->rawQuery($query3);
		if($modData != null){
			$modId = $modData[0]['id'];

			//Lets create the message to send them
			$message = "$userName has left your class: $className!";
	
			$id = $this->eqDb->insert('notifications', [
				'userid'     => $modId,
				'type'       => "Leave",
				'message'    => $message,
				'isread'       => 0,
				'date'       => date("Y/m/d")
			]);
		}

	}

	public function sendBidMessageToNotifications($projectID, $bidPersonID){
		global $eqDb;
		$this->eqDb = $eqDb;
		
		$query1 = "SELECT * FROM project WHERE id = $projectID";
        $userData = $this->eqDb->rawQuery($query1);
		$userName = $userData[0]['projectname'];

		$query2 = "SELECT * FROM users WHERE id = $bidPersonID";
        $userData = $this->eqDb->rawQuery($query2);
		$projectName = $userData[0]['name'];
		
		//Message to send to user
		$message = "$userName has bid on your project: $projectName!";

		//User ID
	

		$id = $this->eqDb->insert('notifications', [
            'userid'     => $bidPersonID,
            'type'       => "Bid",
            'message'    => $message,
            'isread'     => 0,
            'date'       => date("Y/m/d")
        ]);
	}

	public function sendApproveBidToUser($projectBIDID){
		global $eqDb;
		$this->eqDb = $eqDb;

		$query1 = "SELECT * FROM projectbids WHERE bid_id = $projectBIDID";
		$bid_data = $this->eqDb->rawQuery($query1);
		$sendToUserID = $bid_data[0]['user_bid_id'];
		$projectID = $bid_data[0]['project_id'];
		$bid_amount = $bid_data[0]['bid_amount'];

		$query2 = "SELECT * FROM project WHERE id = $projectID";
		$project_data = $this->eqDb->rawQuery($query2);
		$project_code = $project_data[0]['content'];
		$project_name = $project_data[0]['projectname'];

		$message = "Your bid of $" . $bid_amount . " was accepted for " . $project_name ;

		$id = $this->eqDb->insert('notifications', [
            'userid'     => $sendToUserID ,
            'type'       => "Bid Accepted",
            'message'    => $message,
            'isread'     => 0,
            'date'       => date("Y/m/d")
        ]);
	}

	public function sendDeclineBidToUser($projectBIDID){
		global $eqDb;
		$this->eqDb = $eqDb;

		$query1 = "SELECT * FROM projectbids WHERE bid_id = $projectBIDID";
		$bid_data = $this->eqDb->rawQuery($query1);
		$sendToUserID = $bid_data[0]['user_bid_id'];
		$projectID = $bid_data[0]['project_id'];
		$bid_amount = $bid_data[0]['bid_amount'];

		$query2 = "SELECT * FROM project WHERE id = $projectID";
		$project_data = $this->eqDb->rawQuery($query2);
		$project_name = $project_data[0]['projectname'];
		
		$message = "Your bid of $" . $bid_amount . " was declined for " . $project_name ;

		$id = $this->eqDb->insert('notifications', [
            'userid'     => $sendToUserID ,
            'type'       => "Bid Declined",
            'message'    => $message,
            'isread'     => 0,
            'date'       => date("Y/m/d")
        ]);
	}

	public function rankAUser($projectBIDID){
		global $eqDb;
		$this->eqDb = $eqDb;
		
		$query1 = "SELECT * FROM projectbids WHERE bid_id = $projectBIDID";
		$bid_data = $this->eqDb->rawQuery($query1);

	}

}