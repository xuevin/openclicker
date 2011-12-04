package org.openclicker.server.util.serverExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class WebBadRequestException extends WebApplicationException {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public WebBadRequestException(Throwable arg0) {
    super(Response.status(Status.BAD_REQUEST).entity(arg0.getMessage() + "\n")
        .type(MediaType.TEXT_PLAIN).build());
  }
  
  public WebBadRequestException(String message) {
    super(Response.status(Status.BAD_REQUEST).entity(message + "\n").type(
        MediaType.TEXT_PLAIN).build());
  }
  
}
