package org.openclicker.server.util.serverExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class WebServerException extends WebApplicationException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public WebServerException(Throwable arg0) {
    super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(arg0.getMessage()+"\n").type(MediaType.TEXT_PLAIN).build());
  }
   
}
