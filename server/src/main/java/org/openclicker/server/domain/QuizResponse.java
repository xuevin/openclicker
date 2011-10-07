package org.openclicker.server.domain;

import java.util.Date;

public class QuizResponse {
  private Date date_taken;
  private Quiz quiz;
  private AvailableChoice selected_choice;
  
  public QuizResponse() {
  }
  
  public QuizResponse(Quiz quiz, AvailableChoice choice, Date dateTaken) {
    this.setQuiz(quiz);
    this.setSelected_choice(choice);
    this.setDate_taken(dateTaken);
    this.setQuiz(quiz);
  }
  
  public void setDate_taken(Date date_taken) {
    this.date_taken = date_taken;
  }
  
  public Date getDate_taken() {
    return date_taken;
  }
  
  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }
  
  public Quiz getQuiz() {
    return quiz;
  }
  
  public void setSelected_choice(AvailableChoice selected_choice) {
    this.selected_choice = selected_choice;
  }
  
  public AvailableChoice getSelected_choice() {
    return selected_choice;
  }
  
}
