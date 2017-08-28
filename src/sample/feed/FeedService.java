package sample.feed;

import java.util.List;
import sample.db.SQLLiteDAO;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedService {
	static final String INTERNAL_ERROR = "internal_error";
	private final Logger Logger = LoggerFactory.getLogger(FeedService.class);
	
    public String subscribe(String userId, String feedId) {
    	Logger.info("log-Subscribe User to Feed: userId: {}, feedId: {}", userId, feedId);
    	try {
    		String subscribedUserId = SQLLiteDAO.subscribeUserToFeed(userId, feedId);
	    	if (subscribedUserId != null) {
	    		return subscribedUserId;
	    	}
	    	Logger.info("Unable to subscribe the user to the feed");
    	} catch (FeedApplicationException e) {
    		Logger.info("Unable to subscribe the user to the feed, Exception: {}", e.getMessage());
    	} catch (Exception e) {
    		Logger.info("Unable to subscribe the user to the feed: {}", e.getMessage());
    		return INTERNAL_ERROR;
    	}
    	return null;
    }

    public String unsubscribe(String userId, String feedId) {
    	Logger.info("log-UnSubscribe User to Feed: userId: {}, feedId: {}", userId, feedId);
    	try {
    		String subscribedUserId = SQLLiteDAO.unsubscribeUserToFeed(userId, feedId);
	    	if (subscribedUserId != null) {
	    		return subscribedUserId;
	    	}
	    	Logger.info("Unable to unsubscribe the user to the feed");
    	} catch (FeedApplicationException e) {
    		Logger.info("Unable to unsubscribe the user to the feed, Exception: {}", e.getMessage());
    	} catch (Exception e) {
    		Logger.info("Unable to unsubscribe the user to the feed: {}", e.getMessage());
    		return INTERNAL_ERROR;
    	}
    	return null;
    }

    public String create(Feed feed) {  
    	//System.out.println("sys- Create Feed:" + feed);
    	Logger.info("log-Create Feed: {}", feed);
    	try {
    		String feedUUID = SQLLiteDAO.createFeed(feed);
	    	if (feedUUID != null) {
	    		return feedUUID;
	    	}
	    	Logger.info("Unable to create the Feed");
    	} catch (FeedApplicationException e) {
    		Logger.info("Unable to create Feed, Exception: {}", e.getMessage());
    	} catch (Exception e) {
    		Logger.info("Unable to create Feed, Exception: {}", e.getMessage());
    		return INTERNAL_ERROR;
    	}
    	return null;
    }

    public String addArticleToFeed(String feedId, Article article) {
    	try {
    		String articleUUID = SQLLiteDAO.createArticle(feedId, article);
	    	if (articleUUID != null) {
	    		return articleUUID;
	    	}
	    	//System.out.println("Unable to create the Article");
    	} catch (FeedApplicationException e) {
    		Logger.error("Unable to create Article, Exception: ", e);
    	} catch (Exception e) {
    		Logger.error("Unable to create Article, Exception: ", e);
    		return INTERNAL_ERROR;
    	}

    	return null;
    }

    public List<Feed> getFeedsForSubscriber(String userId) {
    	try {
    		Logger.info("FeedService: get list of feeds for subscriber: " + userId);
	        List<Feed> feeds = SQLLiteDAO.getFeedsForSubscriber(userId);
	        Logger.info("FeedService: get list of feeds for subscriber: " + userId + ", /nfeed: " + Arrays.toString(feeds.toArray()));
	        return feeds;
    	} catch (FeedApplicationException e) {
    		Logger.error("Unable to get list of feeds for User", e);
    		return null;
    	}
    }

    public List<Article> getArticlesForSubscriber(String userId) {
    	try {
    		Logger.info("FeedService: get list of articles for subscriber: " + userId);
	        List<Article> articles = SQLLiteDAO.getArticlesForSubscriber(userId);
	        Logger.info("FeedService: get list of articles for subscriber: " + userId + ", articles: " + Arrays.toString(articles.toArray()));
	        return articles;
    	} catch (FeedApplicationException e) {
    		Logger.error("Unable to get list of articles for User, Exception: ", e);
    		return null;
    	}
    }
}