package sample.feed;

public class Article {
    private String id;
    private String title;
    private String url;
    private String body;
    
    public Article() {
    }
    
    public Article(String id, String title, String url, String body) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }
    
    public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
    public String toString() {
    	return "id: " + id + ", url: " + url + ", body: " + body;
    }
}
