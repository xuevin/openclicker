package org.openclicker.server.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Student {
  private int student_uid;
  private String email_address;
  private String first_name;
  private String last_name;
  private String gender;
  private Set<Class> classes = new HashSet<Class>();
  private Set<QuizResponse> quizReponses = new HashSet<QuizResponse>();
  
  public enum Gender {
    M, F
  }
  
  public Student() {

  }
  
  public Student(String firstName, String lastName, Gender gender,
      String emailAddress) {
    setFirst_name(firstName);
    setLast_name(lastName);
    setGender(gender.toString());
    setEmail_address(emailAddress);
  }
  
  protected void setStudent_uid(int student_uid) {
    this.student_uid = student_uid;
  }
  
  public int getStudent_uid() {
    return student_uid;
  }
  
  public void setEmail_address(String email_address) {
    this.email_address = email_address;
  }
  
  public String getEmail_address() {
    return email_address;
  }
  
  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }
  
  public String getFirst_name() {
    return first_name;
  }
  
  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }
  
  public String getLast_name() {
    return last_name;
  }
  
  public void setGender(Gender gender) {
    this.setGender(gender.toString());
  }
  
  protected void setGender(String gender) {
    this.gender = gender;
  }
  
  public String getGender() {
    return gender;
  }
  
  public void addQuizReponse(QuizResponse quizResponse) {
    this.getQuizReponses().add(quizResponse);
  }
  
  public void removeQuizResponse(QuizResponse quizResponse) {
    this.getQuizReponses().remove(quizResponse);
  }
  
  protected void setClasses(Set<Class> classes) {
    this.classes = classes;
  }
  
  protected Set<Class> getClasses() {
    return classes;
  }
  
  protected void setQuizReponses(Set<QuizResponse> quizReponses) {
    this.quizReponses = quizReponses;
  }
  
  protected Set<QuizResponse> getQuizReponses() {
    return quizReponses;
  }
  
  public void addToClass(Class classToAdd) {
    this.getClasses().add(classToAdd);
    classToAdd.getStudents().add(this);
  }
  
  public void removeFromClass(Class classToRemove) {
    this.getClasses().remove(classToRemove);
    classToRemove.getStudents().remove(this);
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Student)) return false;
    
    final Student tempStudent = (Student) other;
    if (tempStudent.getStudent_uid() == getStudent_uid()) {
      return true;
    }
    return false;
  }
  
  public Set<QuizResponse> getQuizResponse_Unmodifiable() {
    return Collections.unmodifiableSet(quizReponses);
  }
}
