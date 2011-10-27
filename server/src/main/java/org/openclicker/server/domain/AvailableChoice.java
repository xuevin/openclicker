package org.openclicker.server.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AvailableChoice implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private int choice_uid;
  private String description;
  private Set<Quiz> quizesAsAnswer = new HashSet<Quiz>();
  private Set<Quiz> quizesAsChoice = new HashSet<Quiz>();
  
  public AvailableChoice() {

  }
  
  public AvailableChoice(String description) {
    this.setDescription(description);
  }
  
  protected void setChoice_uid(int choice_uid) {
    this.choice_uid = choice_uid;
  }
  
  public int getChoice_uid() {
    return choice_uid;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getDescription() {
    return description;
  }
  
  protected void setQuizesAsChoice(Set<Quiz> quizesAsChoice) {
    this.quizesAsChoice = quizesAsChoice;
  }
  
  protected Set<Quiz> getQuizesAsChoice() {
    return quizesAsChoice;
  }
  
  protected void setQuizesAsAnswer(Set<Quiz> quizesAsAnswer) {
    this.quizesAsAnswer = quizesAsAnswer;
  }
  
  protected Set<Quiz> getQuizesAsAnswer() {
    return quizesAsAnswer;
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof AvailableChoice)) return false;
    
    final AvailableChoice tempChoice = (AvailableChoice) other;
    if (tempChoice.getChoice_uid() == getChoice_uid()) {
      return true;
    }
    
    return false;
    
  }
  
  @Override
  public int hashCode() {
    int result;
    result = choice_uid * 26;
    return result;
  }
  
}
