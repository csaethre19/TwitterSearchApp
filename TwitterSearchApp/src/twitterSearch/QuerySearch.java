package twitterSearch;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.ST;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Fetches tweets based on a users specified <code>query</code>. Provides
 * functionality to return tweet information to the user.
 * 
 * When returning returning the results of the tweets to the user they will be
 * sorted by highest retweet count.
 * 
 * @author Charlotte Saethre
 *
 */
public class QuerySearch {
	private Twitter twitter;
	private String query;
	private List<Status> tweets;

	/**
	 * Initializes the fields <code>twitter</code> and <code>query</code>. Starts
	 * the process of searching for tweets based on the provided query.
	 * 
	 * @param twitter the twitter instance
	 * @param query   the query
	 */
	public QuerySearch(Twitter twitter, String query) {
		this.twitter = twitter;
		this.query = query;
		searchQuery();
	}

	/**
	 * Searches twitter for the specified query and initializes <code>tweets</code>
	 * with the returned results.
	 * 
	 */
	private void searchQuery() {
		Query q = new Query(query);
		q.setCount(20);
		QueryResult result;

		try {
			result = twitter.search(q);
			tweets = result.getTweets();

		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Returns a list of strings that provide the necessary tweet information to be
	 * displayed to the user.
	 * 
	 * @return list of tweet information
	 */
	public List<String> getTweetsInformation() {
		List<String> results = new ArrayList<>();

		int count = 1;
		for (Status tweet : tweets) {
			results.add("TWEET " + count + ": " + tweet.getText() + "\nUSER: " + tweet.getUser().getName()
					+ "\nRetweet Count: " + tweet.getRetweetCount() + "\nFavorite Count: " + tweet.getFavoriteCount()
					+ "\n" + tweet.getCreatedAt() + "\n-------------------------------------------");
			count++;

		}

		return results;
	}

	/**
	 * Display tweets to console for testing purpposes.
	 */
	public void printTweets() {
		for (String tweet : getTweetsInformation()) {
			System.out.println(tweet);
		}
	}

	// Sorts tweets by highest retweet count.
	private List<Status> getPriorityTweets() {
		MaxPQ<Integer> retweetPq = new MaxPQ<>();
		ST<Integer, Status> st = new ST<>();
		List<Status> priorityTweets = new ArrayList<>();

		for (Status status : tweets) {
			retweetPq.insert(status.getRetweetCount());
			st.put(status.getRetweetCount(), status);
		}
		while (!retweetPq.isEmpty()) {
			Integer max = retweetPq.delMax();
			priorityTweets.add(st.get(max));
		}

		return priorityTweets;
	}

	/********** TEST CLIENT **********/
	public static void main(String[] args) {
		Twitter twitter = TwitterAuth.getTwitterInstance();
		QuerySearch qs = new QuerySearch(twitter, "Elon Musk");
		qs.printTweets();

	}

}
