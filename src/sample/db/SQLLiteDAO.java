package sample.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sample.feed.Article;
import sample.feed.Feed;
import sample.feed.FeedApplicationException;

public class SQLLiteDAO
{
	private static Connection connection = null;
	
	static {
	    // load the sqlite-JDBC driver using the current class loader
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("org.sqlite.JDBC Not Found, Exiting");
			System.exit(1);
		}

	    //Connection connection = null;
	    try
	    {
	      // create a database connection
	      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
	      Statement statement = connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      
	      //statement.executeUpdate("drop table if exists feed");
	      statement.executeUpdate("create table feed (id string PRIMARY KEY, name string)");
	      //statement.executeUpdate("drop table if exists article");
	      statement.executeUpdate("create table article (id string PRIMARY KEY, title string, url string, body string)");
	      //statement.executeUpdate("drop table if exists feed_article_map");
	      statement.executeUpdate("create table feed_article_map (feed_id string REFERENCES feed(id) ON UPDATE CASCADE, article_id string REFERENCES article(id) ON UPDATE CASCADE)");
	      //statement.executeUpdate("drop table if exists user");
	      statement.executeUpdate("create table user (id string, name string)");
	      //statement.executeUpdate("drop table if exists user_feed_map");
	      statement.executeUpdate("create table user_feed_map (user_id string REFERENCES user(id) ON UPDATE CASCADE, feed_id string REFERENCES feed(id) ON UPDATE CASCADE)");
	      statement.executeUpdate("insert into user values ('1', 'priti-1')");
	      statement.executeUpdate("insert into user values ('2', 'priti-2')");
	      statement.executeUpdate("insert into user values ('3', 'priti-3')");
	      statement.executeUpdate("insert into user values ('4', 'priti-4')");
	      //System.out.println("**** Tables created***");
	    }
	    catch(SQLException e)
	    {
	      System.out.println("SQLException in initialization: " + e.getMessage());
	      closeConnection();
	    }
	}
	
	public static void closeConnection() {
		try
	      {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.out.println("SQLException-Unable to close connection: " + e);
	      }
	}
	
	public static String createFeed(Feed feed) throws FeedApplicationException {
		try {
			Statement statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.
		    String sql = "insert or replace into feed values('"+ feed.getId() + "','"+ feed.getName() + "')";
		    int result = statement.executeUpdate(sql);
		    if(result == 1) {
		    	return feed.getId();
		    }
		    return null;
		} catch (SQLException e) {
			throw new FeedApplicationException("Unable to create Feed",e);
		}
	}
	
	public static String createArticle(String feedId, Article article) throws FeedApplicationException{
		int result = 0;
		try {
			Statement statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.
		    ResultSet rs = statement.executeQuery("select id from feed where id = " + feedId);
		    if (rs.next()) {
		    	String articleSQL = "insert or replace into article values('" + 
		    			article.getId() + "','" + 
		    			article.getTitle() + "','" +
		    			article.getUrl() + "','" +
		    			article.getBody() + "')";
		    	String feedArticleSQL = "insert or replace into feed_article_map values('" + 
		    			feedId + "','" +  
		    			article.getId() + "')";
		    	result = statement.executeUpdate(articleSQL);
		    	statement.executeUpdate(feedArticleSQL);
		    	if(result == 1) {
		    		return article.getId();
		    	}
		    }
		    return null;
		} catch (SQLException e) {
			throw new FeedApplicationException("Unable to create an Article", e); 
		}
	}
	
	public static String subscribeUserToFeed(String userId, String feedId) throws FeedApplicationException {
		int result = 0;
		try {
			Statement statement1 = connection.createStatement();
		    statement1.setQueryTimeout(30);  // set timeout to 30 sec.
		    
		    Statement statement2 = connection.createStatement();
		    statement2.setQueryTimeout(30);  // set timeout to 30 sec.
		    
		    ResultSet rsUser = statement1.executeQuery("select id from user where id = '" + userId + "'");
		    ResultSet rsFeed = statement2.executeQuery("select id from feed where id = '" + feedId + "'");
		    
		    if (rsUser.next() && rsFeed.next()) {
		    	String userFeedSQL = "insert or replace into user_feed_map values ('" + 
		    			userId + "','" + 
		    			feedId + "')";
		    	
		    	result = statement1.executeUpdate(userFeedSQL);
		    	if(result == 1) {
		    		return userId;
		    	}
		    }
		    return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FeedApplicationException("Unable to subscribe User to Feed, user: " + userId + ", feed: " + feedId, e); 
		}
	}
	
	public static String unsubscribeUserToFeed(String userId, String feedId) throws FeedApplicationException {
		int result = 0;
		try {
			Statement statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.
		    ResultSet rsUserFeed = statement.executeQuery("select * from user_feed_map where user_id = '" + userId + "' and feed_id = '" + feedId + "'");
		    if (rsUserFeed.next()) {
		    	String userFeedSQL = "delete from user_feed_map where user_id = " + 
		    			userId + " and feed_id = " + feedId;
		    	result = statement.executeUpdate(userFeedSQL);
		    	if(result == 1) {
		    		return userId;
		    	}
		    }
		    return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FeedApplicationException("Unable to unsubscribe User to Feed, user: " + userId + ", feed: " + feedId, e); 
		}
	}
	
	public static List<Feed> getFeedsForSubscriber(String userId) throws FeedApplicationException {
		List<Feed> feeds = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.
		    ResultSet rsFeed = statement.executeQuery("select * from feed where id in (select feed_id from user_feed_map where user_id = " + userId + ")");
		    while (rsFeed.next()) {
		    	//System.out.println(rsFeed.getString("id") + ":" + rsFeed.getString("name"));
		    	Feed feed = new Feed(rsFeed.getString("id"), rsFeed.getString("name"));
		    	feeds.add(feed);
		    }
		} catch(SQLException e) {
			throw new FeedApplicationException("Unable to get feeds user: " + userId , e);
		}
		return feeds;
	}
	
	public static List<Article> getArticlesForSubscriber(String userId) throws FeedApplicationException {
		List<Article> articles = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
		    statement.setQueryTimeout(30);  // set timeout to 30 sec.

		    String sql = "select a.* from article a where id in "
		    		+ "(select fam.article_id from feed_article_map fam "
		    		+ "inner join user_feed_map ufm "
		    		+ "on fam.feed_id = ufm.feed_id "
		    		+ "and ufm.user_id = " + userId + ")";
		    		
		    ResultSet rsArticle = statement.executeQuery(sql);
		    
		    while (rsArticle.next()) {
		    	String articleId = rsArticle.getString("id");
		    	String articleTitle = rsArticle.getString("title");
		    	String articleURL = rsArticle.getString("url");
		    	String articleBody = rsArticle.getString("body");
		    	articles.add(new Article(articleId, articleTitle, articleURL, articleBody));
		    }
		} catch(SQLException e) {
			throw new FeedApplicationException("Unable to get articles user is subscribed for: " + userId , e);
		}
		return articles;
	}
}
