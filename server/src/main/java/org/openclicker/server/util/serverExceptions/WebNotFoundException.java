package org.openclicker.server.util.serverExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class WebNotFoundException extends WebApplicationException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public WebNotFoundException(Throwable arg0) {
    super(Response.status(Status.NOT_FOUND).entity(arg0.getMessage()+"\n").type(MediaType.TEXT_PLAIN).build());
  }
   
}
