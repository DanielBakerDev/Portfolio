

//3) [10pts] Produce a list of users, together with the total number of times they tweeted, sorted in decreasing order.
db.tweets.aggregate([
	{
		$group: { _id: "$user.screen_name", tweets: { $sum: 1 } }
	},
	{
		$sort: { tweets: -1 }
	}
])

//4) [10pts] Produce a list of place names, together with the total number of tweets from that place name, sorted in decreasing order.
db.tweets.aggregate([
	{
		$match: { "place.name": { $ne: null } }
	},
	{
		$group: { _id: "$place.name", tweets: { $sum: 1 } }
	},
	{
		$sort: { tweets: -1 }
	}
])

//5) [15pts] Produce a list of users, together with the total number of replies to that user, sorted in decreasing order.
db.tweets.aggregate([
	{
		$match: { "in_reply_to_screen_name": { $ne: null } }
	},
	{
		$group: { _id: "$in_reply_to_screen_name", tweets: { $sum: 1 } }
	},
	{
		$sort: { tweets: -1 }
	}
])

//6) [15pts] Produce a list of users, together with the total number of hashtags used by that user, sorted in decreasing order.
db.tweets.aggregate([
	{
		$unwind: "$entities.hashtags"
	},
	{
		$group: { _id: "$user.screen_name", count: { $sum: 1 } }
	},
	{
		$sort: { count: -1 }
	}
])