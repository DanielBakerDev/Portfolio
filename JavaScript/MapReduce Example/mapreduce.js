//7) [40pts] Produce a new collection that contains each hashtag used in the collection of tweets, 
//along with the number of times that hashtag was used.

function myMapper() {
	for (let i = 0; i < this.entities.hashtags.length; i++) {
		emit(this.entities.hashtags[i].text, 1);
	}
}

function myReducer(key, values) {
	let sum = 0;
	for (let i = 0; i < values.length; i++) {
		sum += values[i];
	}
	return sum;
}

db.tweets.mapReduce(myMapper, myReducer, { query: {}, out: "count" });
db.count.find().sort({ value: -1 });
