package sample.feed;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.glassfish.jersey.test.JerseyTest;

public class FeedServiceTest extends JerseyTest {
//TODO: Implement
//    @Override
//    public Application configure() {
//        enable(TestProperties.LOG_TRAFFIC);
//        enable(TestProperties.DUMP_ENTITY);
//        return new ResourceConfig(HttpFeedReaderService.class);
//    }
//    
//    @Test
//    public void testCreateFeed(){
//    	HttpFeedReaderService feedReaderService = new HttpFeedReaderService();
//        Response output = target("/feedservice/feed")
//                .request()
//                .post(Entity.entity(feedReaderService, MediaType.APPLICATION_JSON));
//
//        assertEquals("Should return status 200", 200, output.getStatus());
//        assertNotNull("Should return notification", output.getEntity());
//    }
}
