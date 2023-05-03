<?php


class Notifications_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
		$this->eqDb = $eqDb;
		
		$this->message_helper = new MessageHelper();
    }

	public function getallUsers()
    {
		$query1 = "SELECT * from users";
		$users = $this->eqDb->rawQuery($query1);
		return $users;
	}

    public function getallMessagesRead()
    {
        $id = $_SESSION['automatic_user_id'];

        $query1 = "SELECT messages.*, users.username 
        FROM messages 
        INNER JOIN users ON messages.userid=users.id
        WHERE messages.isread = true
        AND messages.userid = $id
        ";
        $notifications = $this->eqDb->rawQuery($query1);

        return $notifications;
    }

    public function getallMessagesUnread()
    {
        $id = $_SESSION['automatic_user_id'];

        $query1 = "SELECT messages.*, users.username 
        FROM messages 
        INNER JOIN users ON messages.userid=users.id
        WHERE messages.isread = false
        AND messages.userid = $id
        ";
        $notificationsRead = $this->eqDb->rawQuery($query1);

        return $notificationsRead;
	}
	public function getallNotificationsUnread()
    {
		$id = $_SESSION['automatic_user_id'];
		$query1 = "SELECT * from notifications where userid = $id and isread = false";
		$notificationsRead = $this->eqDb->rawQuery($query1);

		return $notificationsRead;
	}

	public function getallNotificationsRead()
    {
		$id = $_SESSION['automatic_user_id'];
		$query1 = "SELECT * from notifications where userid = $id and isread = true";
		$notificationsRead = $this->eqDb->rawQuery($query1);

		return $notificationsRead;
	}

	public function getallBids(){
		$id = $_SESSION['automatic_user_id'];

		$query1 = "SELECT * from projectbids pb
		inner join project p on pb.project_id = p.id
		inner join users u on p.userid = u.id
		where p.userid = $id and accepted = false and declined = false";

		$allBids = $this->eqDb->rawQuery($query1);

		return $allBids;
	}

    public function createMsg()
    {
        $useridname      = $this->eqDb->escape($_POST['userid']); //send to
        $useridname = "'" . $useridname . "'";
        $query1 = "SELECT messages.userid
        FROM messages 
        INNER JOIN users ON messages.userid=users.id
        WHERE users.username  = $useridname";
        echo ($query1);

        $userid     = $this->eqDb->escape($_POST['userid']);
        //$userid2       = $this->eqDb->rawQuery($query1);
        //$userid        = $userid2[0];
        $type         = $this->eqDb->escape($_POST['type']);
        $message      = $this->eqDb->escape($_POST['message']);
        $isread       = true; // i have no clue why but putting false makes it read and true makes it unread lmfao ????
        $date         = date("Y-m-d");

        $id = $this->eqDb->insert('messages', [
            'userid'           => $userid,
            'type'             => $type,
            'message'          => $message,
            'isread'           => 0,
            'date'             => $date,
        ]);

        if ($id) {
            return [
                'r'       => 'success',
                'type'    => 'success',
                'message'    => 'Success Message Sent'
               
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
	}
	
	public function markNotificationAsRead($id){

		$data = [
            'isread'           => 1
        ];

        $this->eqDb->where('id', $id);
        if ($this->eqDb->update('notifications', $data)) {

            return [
                'r'       => 'success',
                'type'    => 'success',
                'message'    => 'Success Notification marked as read',
                "redirect" => _SITEROOT_ . "notifications"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
	}

	public function markMessageAsRead($id){

		// $query1 = "update messages set isread = true where id = $id ";
		// $notificationsRead = $this->eqDb->rawQuery($query1);


		$data = [
			'isread'           => 1
        ];

        $this->eqDb->where('id', $id);
        if ($this->eqDb->update('messages', $data)) {

            return [
                'r'        => 'success',
                'type'     => 'success',
                'message'  => 'Success Message Marked as read',
                "redirect" => _SITEROOT_ . "notifications"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
	}

	public function acceptBid($id){
		$data = [
			'accepted'           => 1
        ];

        $this->eqDb->where('bid_id', $id);
        if ($this->eqDb->update('projectbids', $data)) {

			$this->message_helper->sendApproveBidToUser($id);

            return [
                'r'        => 'success',
                'type'     => 'success',
                'message'  => 'Success project has been accepted!',
                "redirect" => _SITEROOT_ . "notifications"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
	}

	public function declineBid($id){
		$data = [
			'declined'           => 1
        ];

        $this->eqDb->where('bid_id', $id);
        if ($this->eqDb->update('projectbids', $data)) {

			$this->message_helper->sendDeclineBidToUser($id);

            return [
                'r'        => 'success',
                'type'     => 'success',
                'message'  => 'Success project has been accepted!',
                "redirect" => _SITEROOT_ . "notifications"
            ];
        }

        return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];
	}

	public function getAccceptedProjectCode() {
		$id = $_SESSION['automatic_user_id'];

		$query1 = "SELECT pb.bid_id, p.projectname, p.content, pb.review_left from projectbids pb
		inner join project p on pb.project_id = p.id
		inner join users u on p.userid = u.id
		where pb.user_bid_id = $id AND pb.accepted = true";

		$code = $this->eqDb->rawQuery($query1);

		return $code;
	}

	public function submitReview(){

		$ranking1     = $this->eqDb->escape($_POST['ranking1']);
		$ranking2     = $this->eqDb->escape($_POST['ranking2']);
		$ranking3     = $this->eqDb->escape($_POST['ranking3']);
		$grade        = $this->eqDb->escape($_POST['grade']);
		$project_id   = $this->eqDb->escape($_POST['idOfProject']);

		$totalRanking = ($ranking1 + $ranking2 + $ranking3 + $grade) /4;

		//Update users ranking
		$query1 = "SELECT p.userid from project p 
		inner join projectbids pb on p.id = pb.project_id
		where bid_id = $project_id";
		$code = $this->eqDb->rawQuery($query1);


		$data = [
			'user_ranking'           => $totalRanking
        ];

        $this->eqDb->where('id', $code[0]['userid']);
        if ($this->eqDb->update('users', $data)) {
			//Set notification to non reviewable
			$data2 = [
				'review_left'           => 1
			];
			$this->eqDb->where('bid_id', $project_id);
			if ($this->eqDb->update('projectbids', $data2)) {
				return [
					'r'        => 'success',
					'type'     => 'success',
					'message'  => 'Success Message Marked as read',
					"redirect" => _SITEROOT_ . "notifications"
				];
			}

		}

		return [
            'r'       => 'fail',
            'type'    => 'danger',
            'message' => '<strong>Error!</strong> Internal Error (2314)!'
        ];

	}

}