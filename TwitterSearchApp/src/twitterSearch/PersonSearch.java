package twitterSearch;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.RateLimitStatus;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Fetches followers and time-line of specified <code>username</code> on
 * Twitter. Provides functionality to create edges to be used for graphing with
 * edges representing users that are following one another and vertices
 * representing the users.
 * 
 * @author Charlotte Saethre
 *
 */
public class PersonSearch {
	private Twitter twitter;
	private String username;
	private List<User> followers;
	private List<String> followersNames;
	private List<String> edges;

	/**
	 * Initializes the fields <code>twitter</code> and <code>username</code>.
	 * Creates ArrayLists for <code>followers</code> and <code>edges</code>. Starts
	 * the process of fetching followers based on specified user.
	 * 
	 * @param twitter  twitter instance
	 * @param username username of person on twitter
	 */
	public PersonSearch(Twitter twitter, String username) {
		this.twitter = twitter;
		this.username = username;
		this.followers = new ArrayList<>();
		this.followersNames = new ArrayList<>();
		this.edges = new ArrayList<>();
		collectFollowers();
	}

	/**
	 * Fetches followers of specified person given <code>username</code>.
	 * 
	 * @param username username of person to search for
	 */
	public void collectFollowers() {
		// Setting up list for followers to iterate through and list to insert the
		// followers into
		PagableResponseList<User> followersList;
		long cursor = -1L;

		try {
			// Fetching follower list for specified user
			followersList = twitter.getFollowersList(username, cursor, 10);
			// Filling list of users and printing the names
			for (int i = 0; i < followersList.size(); i++) {
				User user = followersList.get(i);
				followers.add(user);
				followersNames.add(user.getScreenName());
			}

		} catch (TwitterException e) {
			e.printStackTrace();
		}

		createFollowersEdges();

	}
	
	public List<String> getFollowers() {
		return this.followersNames;
	}

	/**
	 * Creates edges that represent users that are following each other given a
	 * random subset of followers based on the specified user given by
	 * <code>username</code>.
	 * 
	 * @param twitter twitter instance
	 */
	private void createFollowersEdges() {
		// Sets up list of strings that represent the edges found amongst the list of
		// followers
		boolean exceeded = false;
		try {
			for (int i = 0; i < followers.size(); i++) {
				for (int j = i + 1; j < followers.size(); j++) {
					if (!exceeded) {
						User source = followers.get(i);
						User target = followers.get(j);

						Relationship rel = twitter.showFriendship(source.getId(), target.getId());

						// Deal with rate limit
						RateLimitStatus relRateLimit = rel.getRateLimitStatus();

						if (relRateLimit.getRemaining() == 1) {
							exceeded = true;
							break;
						}
						boolean sourceFollowedByTarget = rel.isSourceFollowedByTarget();
						boolean sourceFollowingTarget = rel.isSourceFollowingTarget();

						if (sourceFollowedByTarget || sourceFollowingTarget)
							edges.add(followers.get(i).getScreenName() + " " + followers.get(j).getScreenName());
					} else
						return;

				}
			}
		} catch (TwitterException e) {
			System.out.println("Rate limit exceeded, please try again in 15 minutes. Thank you.");
		}
	}

	/**
	 * Returns the edges represented in a list of strings. Names are separated by a
	 * space indicating there is connection between the two users.
	 * 
	 * @return list of edges
	 */
	public List<String> getEdges() {
		return this.edges;
	}

	/**
	 * Returns the time-line of the <code>User</code> specified by
	 * <code>username</code> as a list of Strings.
	 * 
	 * @return list of user tweets
	 */
	public List<String> getTimeline() {
		List<String> results = new ArrayList<>();
		try {
			ResponseList<Status> timeline = twitter.getUserTimeline(username);
			for (Status s : timeline) {
				results.add(s.getText());
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return results;
	}

	/********** TEST CLIENT **********/
	public static void main(String[] args) {
		Twitter twitter = TwitterAuth.getTwitterInstance();
		PersonSearch personSearch = new PersonSearch(twitter, "char_saethre");
		for (String edge : personSearch.getEdges()) {
			System.out.println(edge);
		}

	}

}
