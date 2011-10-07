package org.openclicker.server.domain;

import java.util.HashSet;
import java.util.Set;

public class Class {
  
  private int class_uid;
  private String class_name;
  private Set<Student> students = new HashSet<Student>();
  private Set<Quiz> quizes = new HashSet<Quiz>();
  
  public Class() {

  }
  
  public Class(String class_name) {
    this.setClass_name(class_name);
  }
  
  @SuppressWarnings("unused")
  private void setClass_uid(int class_uid) {
    this.class_uid = class_uid;
  }
  
  public int getClass_uid() {
    return class_uid;
  }
  
  public void setClass_name(String class_name) {
    this.class_name = class_name;
  }
  
  public String getClass_name() {
    return class_name;
  }
  
  public void addStudent(Student student) {
    this.getStudents().add(student);
    student.getClasses().add(this);
  }
  
  public void removeStudent(Student student) {
    this.getStudents().remove(student);
    student.getClasses().remove(this);
  }
  
  public void addQuizes(Quiz quiz) {
    this.getQuizes().add(quiz);
    quiz.getClasses().add(this);
  }
  
  public void removeQuizes(Quiz quiz) {
    this.getQuizes().remove(quiz);
    quiz.getClasses().remove(this);
    
  }
  
  protected void setStudents(Set<Student> students) {
    this.students = students;
  }
  
  protected Set<Student> getStudents() {
    return students;
  }
  
  protected void setQuizes(Set<Quiz> quizes) {
    this.quizes = quizes;
  }
  
  protected Set<Quiz> getQuizes() {
    return quizes;
  }
  
}
