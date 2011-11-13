package org.openclicker.server;

import java.util.HashMap;
import java.util.Map;

//import org.openclicker.server.resources.AvailableChoiceResource;
//import org.openclicker.server.resources.ClassResource;
//import org.openclicker.server.resources.StartQuizResource;
//import org.openclicker.server.resources.QuizResource;
//import org.openclicker.server.resources.QuizResponseResource;
//import org.openclicker.server.resources.StudentResource;
//import org.openclicker.server.resources.collections.CollectionClassQuizzes;
//import org.openclicker.server.resources.collections.CollectionClassStudent;
//import org.openclicker.server.resources.collections.CollectionStudent;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class RootServer {
  
  public static void main(final String[] args) throws Exception {
    
    final String baseUri = "http://localhost:9998/";
    final Map<String,String> initParams = new HashMap<String,String>();
    
    initParams.put("com.sun.jersey.config.property.packages",
        "org.openclicker.server.resources");
    
    System.out.println("Starting grizzly...");
    
    SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri,
        initParams);
    System.out.println(String
        .format(
            "Jersey app started with WADL available at %sapplication.wadl\n"
                + "Try out %shelloworld\nHit enter to stop it...", baseUri,
            baseUri));
    System.in.read();
    threadSelector.stopEndpoint();
    System.exit(0);
  }
  
}