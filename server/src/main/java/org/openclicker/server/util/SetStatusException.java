package org.openclicker.server.util;

public class SetStatusException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public SetStatusException() {
    super();
  }
  
  public SetStatusException(String message) {
    super(message);
  }
  
  public SetStatusException(Throwable throwable) {
    super(throwable);
  }
  
  public SetStatusException(String message, Throwable throwable) {
    super(message, throwable);
  }
  
}
