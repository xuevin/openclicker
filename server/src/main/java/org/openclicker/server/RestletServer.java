package org.openclicker.server;

import org.openclicker.server.resources.AvailableChoiceResource;
import org.openclicker.server.resources.ClassResource;
import org.openclicker.server.resources.StartQuizResource;
import org.openclicker.server.resources.QuizResource;
import org.openclicker.server.resources.QuizResponseResource;
import org.openclicker.server.resources.StudentResource;
import org.openclicker.server.resources.collections.CollectionClassQuizzes;
import org.openclicker.server.resources.collections.CollectionClassStudent;
import org.openclicker.server.resources.collections.CollectionStudent;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class RestletServer extends Application {
  
  public static void main(final String[] args) throws Exception {
    
    // Create a new Component.
    final Component component = new Component();
    
    // Add a new HTTP server listening on port 8182.
    component.getServers().add(Protocol.HTTP, 8182);
    
    Application application = new RestletServer();
    
    // Attach the sample application.
    component.getDefaultHost().attach("/restlet", application);
    
    // Start the component.
    component.start();
  }
  
  @Override
  public Restlet createInboundRoot() {
    Router router = new Router(getContext().createChildContext());
    //Resources
    router.attach("/student/{student_uid}", StudentResource.class);
    router.attach("/quiz/{quiz_uid}", QuizResource.class);
    router.attach("/availablechoice/{choice_uid}",AvailableChoiceResource.class);
    router.attach("/class/{class_uid}", ClassResource.class);
    router.attach("/student/{student_uid}/quiz/{quiz_uid}", QuizResponseResource.class);
    router.attach("/class/{class_uid}/quiz/{quiz_uid}", StartQuizResource.class);
    
    
    //Collections
    router.attach("/student", CollectionStudent.class);
    router.attach("/class/{class_uid}/student",CollectionClassStudent.class);
    router.attach("/class/{class_uid}/quiz", CollectionClassQuizzes.class);
    return router;
  }
  
}