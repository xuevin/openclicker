package org.openclicker.server.domain;

import java.util.HashSet;
import java.util.Set;

public class AvailableChoice {
  private int choice_uid;
  private String description;
  private Set<Quiz> quizesAsAnswer = new HashSet<Quiz>();
  private Set<Quiz> quizesAsChoice = new HashSet<Quiz>();
  
  public AvailableChoice() {

  }
  
  public AvailableChoice(String description) {
    this.setDescription(description);
  }
  
  @SuppressWarnings("unused")
  private void setChoice_uid(int choice_uid) {
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
  
}
