package youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import bot.Main;
import youtube.Auth;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
//import java.util.Properties;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class Search
{

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
//    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param args command line args.
     */
    
    private static YouTube.Search.List search;
    public static void go()
    {
    	
        try
        {
        	BufferedReader r = new BufferedReader(new FileReader("youtube.key"));
    		String apiKey = r.readLine();

            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Prompt the user to enter a query term.
//            String queryTerm = getInputQuery();

            // Define the API request for retrieving search results.
            search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            search.setKey(apiKey);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            r.close();
        }
        catch (IOException ex)
        {
        	ex.printStackTrace();
        }
    }
    public static String searchVideo(String searchTerm)
    {
        String idCarry = "NONE";
    	try
    	{
	        search.setQ(searchTerm);
	     // Call the API and print results.
	        SearchListResponse searchResponse = search.execute();
	        List<SearchResult> searchResultList = searchResponse.getItems();
	        if (searchResultList != null) {
	            idCarry = prettyPrint(searchResultList.iterator(), searchTerm);
	        }
    	}
    	catch (GoogleJsonResponseException e)
    	{
        System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
	    }
    	catch (IOException e)
    	{
	        System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
	    }
    	catch (Throwable t)
    	{
	        t.printStackTrace();
	    }
        return idCarry;

    }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
/*    private static String getInputQuery() throws IOException {

        String inputQuery = "";

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // Use the string "YouTube Developers Live" as a default.
            inputQuery = "YouTube Developers Live";
        }
        return inputQuery;
    }	*/

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private static String prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        Main.logMessage("\n=======================================================");
        Main.logMessage(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        Main.logMessage("=======================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            Main.logMessage(" There aren't any results for your query.");
        }
        String videoID = "placeholder";

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                Main.logMessage(" Video Id: " + rId.getVideoId());
                if(videoID == "placeholder")
                {
                	videoID = rId.getVideoId();
                }
                Main.logMessage(" Title: " + singleVideo.getSnippet().getTitle());
                Main.logMessage(" Thumbnail: " + thumbnail.getUrl());
                Main.logMessage("\n-------------------------------------------------------------\n");
            }
        }
        return videoID;
    }
}