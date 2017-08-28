package sample.feed;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import sample.feed.Article;
import sample.feed.Feed;
import sample.feed.FeedService;


@Path("/feedservice")
public class HttpFeedReaderService {
	private static final int RESPONSE_CODE_OK = 200;
	private static final int RESPONSE_CODE_BAD_REQUEST = 401;
	private static final int RESPONSE_CODE_INTERNAL_ERROR = 500;
	
    private FeedService feedService = new FeedService();

    /** Task 1: Subscribe/unsubscribe user to a feed*/
    @Path("/user/feed/action")
    @POST
    @Consumes("application/x-www-form-urlencoded;charset=UTF-8")
    @Produces("application/json")
    public Response userActionToFeed(@QueryParam("userId") String userId, @QueryParam("feedId") String feedId, @QueryParam("action") String action) {
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("userId", userId);
		jsonObject.put("feedId", feedId);
		jsonObject.put("action", action);
		String result = null;
		int responseCode = RESPONSE_CODE_OK;
    	switch (action) {
    		case "subscribe": 
    			result = feedService.subscribe(userId, feedId);
    			if (result == null) {
    				jsonObject.put("result", "Failed");
    				responseCode = RESPONSE_CODE_BAD_REQUEST;
    			} else if (result.equals(FeedService.INTERNAL_ERROR)) {
    				jsonObject.put("result", "Failed");
    				jsonObject.put("reason", "Internal Server Error");
    				responseCode = RESPONSE_CODE_INTERNAL_ERROR;
    			} else {
    				jsonObject.put("result", "Success");
    			}
    			break;
    		case "unsubscribe": 
    			result = feedService.unsubscribe(userId, feedId);
    			if (result == null) {
    				jsonObject.put("result", "Failed");
    				responseCode = RESPONSE_CODE_BAD_REQUEST;
    			} else if (result.equals(FeedService.INTERNAL_ERROR)) {
    				jsonObject.put("result", "Failed");
    				jsonObject.put("reason", "Internal Server Error");
    				responseCode = RESPONSE_CODE_INTERNAL_ERROR;
    			} else {
    				jsonObject.put("result", "Success");
    			}
    			break;
    		default:
    			jsonObject.put("result", "Failed");
    			jsonObject.put("reason", "supported actions:[subscribe/unsubscribe]");
    			responseCode = RESPONSE_CODE_BAD_REQUEST;
    	}
    	return Response.status(responseCode).entity(jsonObject.toString()).build();
    }

    /** Task 2: Add	Articles to a Feed
     * Step 1: create a feed, return feedId
     * Step 2: add a new article to a feed, returns articleId
     */
    @Path("/feed")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFeed(Feed feed) {
    	System.out.println("createFeed:" + feed.toString());
    	String result = feedService.create(feed);
    	int responseCode = RESPONSE_CODE_OK;
    	JSONObject jsonObject = new JSONObject();
    	if (result == null) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("reason", "Unable to create Feed.");
    		responseCode = RESPONSE_CODE_BAD_REQUEST;
    	} else if (result.equals(FeedService.INTERNAL_ERROR)) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("reason", "Internal Server Error");
    		responseCode = RESPONSE_CODE_INTERNAL_ERROR;
    	} else {
    		jsonObject.put("result", "Success");
    		jsonObject.put("feedUUID", result);
    	}
    	return Response.status(responseCode).entity(jsonObject.toString()).build();
    }

    @Path("/feed/article")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addArticleToFeed(@QueryParam("feedId") String feedId, Article articleJson) {
    	System.out.println("**** Calling endpoint to create an article");
    	String result = feedService.addArticleToFeed(feedId, articleJson);
    	int responseCode = RESPONSE_CODE_OK;
    	JSONObject jsonObject = new JSONObject();
    	if (result == null) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("reason", "Unable to create Article, feed must exist.");
    		responseCode = RESPONSE_CODE_BAD_REQUEST;
    	} else if (result.equals(FeedService.INTERNAL_ERROR)) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("reason", "Internal Server Error");
    		responseCode = RESPONSE_CODE_INTERNAL_ERROR;
    	} else {
    		jsonObject.put("result", "Success");
    		jsonObject.put("articleUUID", result);
    	}
    	return Response.status(responseCode).entity(jsonObject.toString()).build();
    }

    /** Task 3: Get all Feeds a Subscriber is following*/
    
    @Path("/users/feeds")
    @GET
    @Produces("application/json")
    public Response getFeedsForSubscriber(@QueryParam("userId") String userId) {
    	int responseCode = RESPONSE_CODE_OK;
    	JSONObject jsonObject = new JSONObject();
    	
    	List<Feed> feeds= feedService.getFeedsForSubscriber(userId);
    	if (feeds == null) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("reason", "Either no user or no feeds exist for the user");
    		responseCode = RESPONSE_CODE_BAD_REQUEST;
    	} else {
    		jsonObject.put("result", "Success");
    		jsonObject.put("userId", userId);
    		jsonObject.put("Feeds", Arrays.toString(feeds.toArray()));
    	}
    	return Response.status(responseCode).entity(jsonObject.toString()).build();
    }

    /** Task 4: Get Articles from the set of Feeds a Subscriber is following*/
    @GET
    @Path("users/articles")
	@Produces("application/json")
    public Response getArticlesForSubscriber(@QueryParam("userId") String userId) {
    	int responseCode = RESPONSE_CODE_OK;
    	JSONObject jsonObject = new JSONObject();
    	
    	List<Article> articles= feedService.getArticlesForSubscriber(userId);
    	if (articles == null) {
    		jsonObject.put("result", "Failed");
    		jsonObject.put("userId", userId);
    		jsonObject.put("reason", "Either no user, feeds or articles exist for the user");
    		responseCode = RESPONSE_CODE_BAD_REQUEST;
    	} else {
    		jsonObject.put("result", "Success");
    		jsonObject.put("userId", userId);
    		jsonObject.put("Articles", Arrays.toString(articles.toArray()));
    	}
    	return Response.status(responseCode).entity(jsonObject.toString()).build();
    }
    
}