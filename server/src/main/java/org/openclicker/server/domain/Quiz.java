package org.openclicker.server.domain;

import java.util.HashSet;
import java.util.Set;

public class Quiz {
  private int quiz_uid;
  private String topic;
  private String type;
  private String question;
  private Set<Class> classes = new HashSet<Class>();
  private Set<AvailableChoice> answers = new HashSet<AvailableChoice>();
  private Set<AvailableChoice> choices = new HashSet<AvailableChoice>();
  
  public enum Type {
    MC, SR
  }
  
  public enum Topic {
    Math, Science, English, Other
  }
  
  public Quiz() {

  }
  
  public Quiz(Topic topic, Type type, String question) {
    setTopic(topic.toString());
    setType(type.toString());
    setQuestion(question.toString());
    
  }
  
  public void addToClass(Class classToBeAdded) {
    this.getClasses().add(classToBeAdded);
    classToBeAdded.getQuizes().add(this);
  }
  
  public void removeFromClass(Class classToBeRemovedFrom) {
    this.getClasses().remove(classToBeRemovedFrom);
    classToBeRemovedFrom.getQuizes().remove(this);
  }
  
  public void addAnswer(AvailableChoice answerToBeAdded) {
    this.getAnswers().add(answerToBeAdded);
    answerToBeAdded.getQuizesAsAnswer().add(this);
  }
  
  public void removeAnswer(AvailableChoice answerToBeRemoved) {
    this.getAnswers().remove(answerToBeRemoved);
    answerToBeRemoved.getQuizesAsAnswer().remove(this);
  }
  
  public void addChoice(AvailableChoice choiceToBeAdded) {
    this.getChoices().add(choiceToBeAdded);
    choiceToBeAdded.getQuizesAsChoice().add(this);
  }
  
  public void removeChoice(AvailableChoice choiceToBeRemoved) {
    this.getChoices().remove(choiceToBeRemoved);
    choiceToBeRemoved.getQuizesAsChoice().remove(this);
  }
  
  public void setTopic(Topic topic) {
    this.setTopic(topic.toString());
  }
  
  protected void setTopic(String topic) {
    this.topic = topic;
  }
  
  public String getTopic() {
    return topic;
  }
  
  public void setType(Type type) {
    this.setType(type.toString());
  }
  
  protected void setType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
  
  public void setQuestion(String question) {
    this.question = question;
  }
  
  public String getQuestion() {
    return question;
  }
  
  protected void setQuiz_uid(int quiz_uid) {
    this.quiz_uid = quiz_uid;
  }
  
  public int getQuiz_uid() {
    return quiz_uid;
  }
  
  protected void setClasses(Set<Class> classes) {
    this.classes = classes;
  }
  
  protected Set<Class> getClasses() {
    return classes;
  }
  
  protected void setAnswers(Set<AvailableChoice> answers) {
    this.answers = answers;
  }
  
  protected Set<AvailableChoice> getAnswers() {
    return answers;
  }
  
  protected void setChoices(Set<AvailableChoice> choices) {
    this.choices = choices;
  }
  
  protected Set<AvailableChoice> getChoices() {
    return choices;
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Quiz)) return false;
    
    final Quiz tempQuiz = (Quiz) other;
    if (tempQuiz.getQuiz_uid() == getQuiz_uid()) {
      return true;
    }
    
    return false;
  }
  
}
