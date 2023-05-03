<?php


class Classroom_Model extends Model
{
	public function __construct()
	{

		global $eqDb;
		$this->eqDb = $eqDb;
		$this->messageHelper = new MessageHelper();
	}

	public function getClassRoomName($id)
	{
		$query = "SELECT className
            FROM class
            WHERE id = $id ";

		$response = $this->eqDb->rawQuery($query);

		return $response[0]['className'];
	}

	public function getAllClassProject($id)
	{
		$query = "SELECT *
            FROM projectclass
            WHERE classid = $id ";

		$response = $this->eqDb->rawQuery($query);

		return $response;
	}

	public function getAllProjects($id)
	{
		$singedinUserID = $_SESSION['automatic_user_id'];
		$query = "SELECT $singedinUserID as singedinUserID,  project.id as projectID, projectclassid, userid, projectname, date, content, askingprice, currentprice, users.id as UserID, username, email, name, profilePicture, user_ranking
        FROM project
        INNER JOIN users ON project.userid = users.id
        WHERE projectclassid = $id";


		$response = $this->eqDb->rawQuery($query);

		return $response;
	}

	public function addProject()
	{
		$projectname     = $this->eqDb->escape($_POST['projectname']);
		$content         = $this->eqDb->escape($_POST['content']);
		$price           = $this->eqDb->escape($_POST['price']);
		$projectclassid  = $this->eqDb->escape($_POST['projectclassid']);
		$classid         = $this->eqDb->escape($_POST['classid']);


		//Add project to the class
		$id = $this->eqDb->insert('project', [
			'projectname'       => $projectname,
			'userid'            => $_SESSION['automatic_user_id'],
			'content'           => $content,
			'askingprice'       => $price,
			'currentprice'      => $price,
			'date'              => date("Y/m/d"),
			'projectclassid'    => $projectclassid
		]);

		//If correct lets add them to the room
		if ($id) {
			return [
				'r'        => 'success',
				"redirect" => _SITEROOT_ . "classroom/" . $classid,
				's'        => $projectclassid
			];
		} else {
			return [
				'r'        => 'fail',
				'message' => '<strong>Error!</strong> Internal Error (21233)'
			];
		}
	}

	public function leaveClass($id)
	{
		$userid = $_SESSION['automatic_user_id'];
		$query = "DELETE 
            FROM classusers
            WHERE classid = $id AND userid = $userid";

		$response = $this->eqDb->rawQuery($query);

		$this->messageHelper->sendLeaveClass($id, $_SESSION['automatic_user_id']);
		return  [
			"r" 	   => "success",
			"redirect" => _SITEROOT_ . "home"
		];
	}

	public function addProjectClass($id)
	{

		$projectname     = $this->eqDb->escape($_POST['classprojectname']);

		$id = $this->eqDb->insert('projectclass', [
			'classid'           => $id,
			'date'              => date("Y/m/d"),
			'className'         => $projectname
		]);

		//If correct lets add them to the room
		if ($id) {
			// return [
			//     'r'        => 'success',
			//     "redirect" => _SITEROOT_ . "classroom/" . $id,
			// ];
			header("Refresh:0");
		} else {
			return [
				'r'        => 'fail',
				'message' => '<strong>Error!</strong> Internal Error (21233)'
			];
		}
	}

	public function bidOnProject($id)
	{
		$projectID       = $this->eqDb->escape($_POST['projectid']);
		$userID    		 = $_SESSION['automatic_user_id'];
		$bidAmount       = $this->eqDb->escape($_POST['bidAmount']);

		$query = "SELECT userid FROM project
		WHERE id = $projectID";
		$response = $this->eqDb->rawQuery($query);
		$sendToID = $response[0]['userid'];


		$insert = $this->eqDb->insert('projectbids', [
			'project_id'         => $projectID,
			'user_bid_id'        => $userID,
			'bid_amount'         => $bidAmount,
			'accepted'           => 0,
			'declined'           => 0
		]);
	
		if ($insert) {
			$this->messageHelper->sendBidMessageToNotifications($projectID, $sendToID);
			return [
				'r'        => 'success',
				'type'     => 'success',
				'message'  => 'Success - Bid Sent',
			    "redirect" => _SITEROOT_ . "classroom/" . $id
			];
		} else {
			return [
				'r'        => 'fail',
				'message' => '<strong>Error!</strong> Internal Error (21233)'
			];
		}
	}
}
