package org.openclicker.server.util;

public class EmptyValueException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public EmptyValueException() {
    super();
  }
  
  public EmptyValueException(String message) {
    super(message);
  }
  
  public EmptyValueException(Throwable throwable) {
    super(throwable);
  }
  
  public EmptyValueException(String message, Throwable throwable) {
    super(message, throwable);
  }
  
}
