package org.openclicker.server.resources.containers;

public class ClassQuizIdentifier {
  private int class_uid;
  private int quiz_uid;
  
  public ClassQuizIdentifier(int class_uid, int quiz_uid) {
    this.class_uid = class_uid;
    this.quiz_uid = quiz_uid;
  }
  
  public int getClassUID() {
    return class_uid;
  }
  
  public int getQuizUID() {
    return quiz_uid;
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof ClassQuizIdentifier)) return false;
    
    final ClassQuizIdentifier tempID = (ClassQuizIdentifier) other;
    if (tempID.getClassUID() == getClassUID()
        && tempID.getQuizUID() == getQuizUID()) {
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    
    return getClassUID() * 127 + getQuizUID();
  }
}
