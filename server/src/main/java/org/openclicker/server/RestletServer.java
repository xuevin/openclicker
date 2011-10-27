package org.openclicker.server;

import org.openclicker.server.resources.AvailableChoiceResource;
import org.openclicker.server.resources.ClassResource;
import org.openclicker.server.resources.CollectionStudentResource;
import org.openclicker.server.resources.QuizResource;
import org.openclicker.server.resources.CollectionClassStudentResource;
import org.openclicker.server.resources.StudentResource;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
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
    router.attach("/student/{student_uid}", StudentResource.class);
    router.attach("/student", CollectionStudentResource.class);
    router.attach("/quiz/{quiz_uid}",QuizResource.class);
    router.attach("/class/{class_uid}",ClassResource.class);
    router.attach("/availablechoice/{choice_uid}",AvailableChoiceResource.class);
    router.attach("/class/{class_uid}/student",CollectionClassStudentResource.class);
    return router;
  }
  
  @Override
  @Get
  public String toString() {
    return "hello, world";
  }
  
}