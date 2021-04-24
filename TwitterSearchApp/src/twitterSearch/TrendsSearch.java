package twitterSearch;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Detects the leading trends based on the user specified <code> location </code>.
 * 
 * @author Briana Murdock
 *
 */
public final class TrendsSearch {

	private Twitter twitter;
	private String location;

	/**
	 * Initializes the fields <code>twitter</code> and <code>location</code>.
	 * Creates ArrayLists for <code>trends</code>. Starts the process of fetching
	 * trends based on specified location.
	 * 
	 * @param twitter  twitter instance
	 * @param location location of twitter trends
	 */
	public TrendsSearch(Twitter twitter, String location) {
		this.twitter = twitter;
		this.location = location;
	}

	/**
	 * Creates an arraylist of trends in the user specified <code> location </code>.
	 * Detects if the <code> location </code> is a location,
	 *  if not it returns location not found.
	 * 
	 * @return list of trends
	 */
	public List<String> getTrendsInformation() {
		List<String> results = new ArrayList<>();
		
		try {
			
			ResponseList<Location> locations = twitter.getAvailableTrends();

			Integer idTrendLocation = getTrendLocationId(location);

			if (idTrendLocation == null) {
				System.out.println("Trend Location Not Found");
			}

			Trends trends = twitter.getPlaceTrends(idTrendLocation);
			for (int i = 0; i < trends.getTrends().length; i++) {
				results.add(trends.getTrends()[i].getName());
			}

		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		return results;

	}

	/**
	 * Displays the list of trends.
	 */
	private void printTrends() {

		for (String trend : getTrendsInformation()) {
			StdOut.println(trend);
		}

	}
	
	/**
	 * Gets the id of the <code> location </code>, saving the user from having to look
	 * up the location ID.
	 * 
	 * @param locationName
	 * @return
	 */
	private static Integer getTrendLocationId(String locationName) {

		int idTrendLocation = 0;

		try {

			Twitter twitter = TwitterAuth.getTwitterInstance();

			ResponseList<Location> locations;
			locations = twitter.getAvailableTrends();

			for (Location location : locations) {
				if (location.getName().toLowerCase().equals(locationName.toLowerCase())) {
					idTrendLocation = location.getWoeid();
					break;
				}
			}

			if (idTrendLocation > 0) {
				return idTrendLocation;
			}

			return null;

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get trends: " + te.getMessage());
			return null;
		}

	}

	/************************* TEST CLIENT ***************************************/
	public static void main(String[] args) {
		Twitter twitter = TwitterAuth.getTwitterInstance();
		TrendsSearch trendsSearch = new TrendsSearch(twitter, "united states");
		trendsSearch.printTrends();

	}
}