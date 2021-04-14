package twitterSearch;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.ST;
import twitter4j.IDs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Fetches tweets based on a users specified <code>query</code>. Provides
 * functionality to return tweet information to the user. Provides functionality
 * to create edges to be used for graphing with vertices representing users that
 * retweeted a specified tweet and edges representing users that follower one
 * another.
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
	private List<String> edges;

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
		edges = new ArrayList<>();
		searchQuery();
	}

	/**
	 * Searches twitter for the specified query and initializes <code>tweets</code>
	 * with the returned results.
	 * 
	 */
	private void searchQuery() {
		Query q = new Query(query);
		q.setCount(100);
		QueryResult result;

		try {
			result = twitter.search(q);
			tweets = result.getTweets();

		} catch (TwitterException e) {
			e.printStackTrace();
		}

		// createFollowersEdges();

	}

	// Gets IDs of the users that tweeted the first 10 tweets that were returned
	private long[] getListOfUserIds(int index) {
		long[] userIds = null;

		for (int i = 0; i < 10; i++) {
			IDs ids;
			try {
				ids = twitter.getRetweeterIds(tweets.get(index).getId(), -1L);
				userIds = ids.getIDs();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		return userIds;
	}

	/**
	 * Finds relationships between different users given the list of user IDs
	 * provided from the list of tweets returned by <code>SearchQuery</code>.
	 * Creates edges when there are users that has a follower connection.
	 * 
	 * @return list of strings representing edges found
	 */
	private void createFollowersEdges() {
		long[] userIds = getListOfUserIds(0);

		boolean exceeded = false;
		try {
			for (int i = 0; i < userIds.length; i++) {
				for (int j = i + 1; j < userIds.length; j++) {
					if (!exceeded) {
						Relationship rel = twitter.showFriendship(userIds[i], userIds[j]);

						// Deal with rate limit
						RateLimitStatus relRateLimit = rel.getRateLimitStatus();
						if (relRateLimit.getRemaining() == 1) {
							exceeded = true;
							break;
						}

						boolean sourceFollowed = rel.isSourceFollowedByTarget();
						boolean sourceFollowing = rel.isSourceFollowingTarget();

						if (sourceFollowed || sourceFollowing) {
							// Add edge
							String source = twitter.showUser(userIds[i]).getName();
							String target = twitter.showUser(userIds[j]).getName();
							edges.add(source + " " + target);
						}
					} else {
						return;
					}

				}
			}
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
		for (Status tweet : getPriorityTweets()) {
			results.add("TWEET " + count + ": " + tweet.getText() + "\nUSER: " + tweet.getUser().getName()
					+ "\nRetweet Count: " + tweet.getRetweetCount() + "\nFavorite Count: " + tweet.getFavoriteCount()
					+ "\n" + tweet.getCreatedAt() + "\n-------------------------------------------");
			count++;

		}

		return results;
	}

	public void printTweets() {
		for (String tweet : getTweetsInformation()) {
			System.out.println(tweet);
		}
	}

	/**
	 * 
	 */
	public void showEdges() {
		// Prints the edges found
		for (String edge : edges) {
			System.out.println(edge);
		}
	}

	// TODO - find better way to sort by highest retweet here
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

	public static void main(String[] args) {
		Twitter twitter = TwitterAuth.getTwitterInstance();
		QuerySearch qs = new QuerySearch(twitter, "Elon Musk");
		qs.printTweets();
		// qs.showEdges();

	}

}
