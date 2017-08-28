package sample.feed;

public class FeedApplicationException extends Exception {
	public FeedApplicationException(){
		super();
	}
	public FeedApplicationException(String message) {
		super(message);
	}
	public FeedApplicationException(Throwable t) {
		super(t);
	}
	public FeedApplicationException(String message, Throwable t) {
		super(message, t);
	}
}
