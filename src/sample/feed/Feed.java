package sample.feed;

import java.io.Serializable;

public class Feed implements Serializable {
    private String id;
    private String name;
    
    public Feed() {
    }
    
    public Feed(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override 
    public String toString() {
    	return "id: " + id + ", name: " + name;
    }
}