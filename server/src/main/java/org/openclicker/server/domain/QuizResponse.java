package org.openclicker.server.domain;

import java.io.Serializable;
import java.util.Date;

public class QuizResponse implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Date date_taken;
  private Quiz quiz;
  private AvailableChoice selected_choice;
  
  public QuizResponse() {}
  
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
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof QuizResponse)) return false;
    
    final QuizResponse tempResponse = (QuizResponse) other;
    
    // Two quiz responses are equivalent if they have the same choice uid, date
    // and
    // have the same choice uid.
    
    try {
      if (tempResponse.getQuiz().equals(getQuiz())
          && tempResponse.getSelected_choice().equals(getSelected_choice())
          && tempResponse.getDate_taken().equals(getDate_taken())) {
        return true;
      }
    } catch (NullPointerException e) {
      System.out.println("In Comparing Quiz Responses, There was a null value");
      return false;
    }
    
    return false;
    
  }
  
  @Override
  public int hashCode() {
    int result;
    result = quiz.hashCode() * 26 + selected_choice.getChoice_uid()
        + date_taken.hashCode();
    return result;
  }
  
}
