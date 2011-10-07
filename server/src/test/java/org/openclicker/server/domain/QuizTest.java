package org.openclicker.server.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;

public class QuizTest {
  
  private Quiz quiz1;
  private Quiz quiz2;
  
  @Before
  public void setUp() throws Exception {
    quiz1 = new Quiz();
    quiz2 = new Quiz(Topic.English, Type.MC, "Whats up?");
  }
  
  @Test
  public void testQuiz() {
    assertNotNull(quiz1);
  }
  
  @Test
  public void testQuizTopicTypteString() {
    assertNotNull(quiz2);
  }
  
  @Test
  public void testAddToClass() {
    Class englishClass = new Class("English Clss");
    assertEquals(0, quiz1.getClasses().size());
    quiz1.addToClass(englishClass);
    assertEquals(1, quiz1.getClasses().size());
  }
  
  @Test
  public void testRemoveFromClass() {
    Class englishClass = new Class("English Clss");
    assertEquals(0, quiz1.getClasses().size());
    quiz1.addToClass(englishClass);
    assertEquals(1, quiz1.getClasses().size());
    quiz1.removeFromClass(englishClass);
    assertEquals(0, quiz1.getClasses().size());
  }
  
  @Test
  public void testAddAnswer() {
    AvailableChoice answer = new AvailableChoice();
    assertEquals(0, quiz1.getAnswers().size());
    quiz1.addAnswer(answer);
    assertEquals(1, quiz1.getAnswers().size());
  }
  
  @Test
  public void testRemoveAnswer() {
    AvailableChoice answer = new AvailableChoice();
    assertEquals(0, quiz1.getAnswers().size());
    quiz1.addAnswer(answer);
    assertEquals(1, quiz1.getAnswers().size());
    quiz1.removeAnswer(answer);
    assertEquals(0, quiz1.getAnswers().size());
  }
  
  @Test
  public void testAddChoice() {
    AvailableChoice choice = new AvailableChoice();
    assertEquals(0, quiz1.getChoices().size());
    quiz1.addChoice(choice);
    assertEquals(1, quiz1.getChoices().size());
  }
  
  @Test
  public void testRemoveChoice() {
    AvailableChoice choice = new AvailableChoice();
    assertEquals(0, quiz1.getChoices().size());
    quiz1.addChoice(choice);
    assertEquals(1, quiz1.getChoices().size());
    quiz1.removeChoice(choice);
    assertEquals(0, quiz1.getChoices().size());
  }
  
  @Test
  public void testSetTopicTopic() {
    quiz1.setTopic(Topic.English);
    assertEquals("English", quiz1.getTopic());
  }
  
  @Test
  public void testSetTopicString() {
    quiz1.setTopic("FOO");
    assertEquals("FOO", quiz1.getTopic());
    
  }
  
  @Test
  public void testGetTopic() {
    assertEquals(null, quiz1.getTopic());
    assertEquals("English", quiz2.getTopic());
  }
  
  @Test
  public void testSetTypeType() {
    quiz1.setType(Type.MC);
    assertEquals("MC", quiz1.getType());
  }
  
  @Test
  public void testSetTypeString() {
    quiz1.setType("FOO");
    assertEquals("FOO", quiz1.getType());
  }
  
  @Test
  public void testGetType() {
    assertEquals(null, quiz1.getType());
    assertEquals("MC", quiz2.getType());
  }
  
  @Test
  public void testSetQuestion() {
    quiz1.setQuestion("What is foo?");
    assertEquals("What is foo?", quiz1.getQuestion());
  }
  
  @Test
  public void testGetQuestion() {
    assertEquals(null, quiz1.getQuestion());
    assertEquals("Whats up?", quiz2.getQuestion());
  }
  
  @Test
  public void testGetQuiz_uid() {
    assertEquals(0, quiz1.getQuiz_uid());
  }
  
  @Test
  public void testSetClasses() {
    Set<Class> setOfClasses = new HashSet<Class>();
    quiz1.setClasses(setOfClasses);
    assertEquals(setOfClasses, quiz1.getClasses());
  }
  
  @Test
  public void testGetClasses() {
    Set<Class> setOfClasses = new HashSet<Class>();
    assertEquals(0, quiz1.getClasses().size());
    quiz2.setClasses(setOfClasses);
    assertEquals(setOfClasses, quiz2.getClasses());
    assertEquals(setOfClasses, quiz2.getClasses());
  }
  
  @Test
  public void testSetAnswers() {
    Set<AvailableChoice> answers = new HashSet<AvailableChoice>();
    quiz1.setAnswers(answers);
    assertEquals(answers, quiz1.getAnswers());
  }
  
  @Test
  public void testGetAnswers() {
    assertEquals(0,quiz1.getAnswers().size());
    Set<AvailableChoice> answers = new HashSet<AvailableChoice>();
    quiz2.setAnswers(answers);
    assertEquals(answers, quiz2.getAnswers());
  }
  
  @Test
  public void testSetChoices() {
    Set<AvailableChoice> choices = new HashSet<AvailableChoice>();
    quiz1.setChoices(choices);
    assertEquals(choices, quiz1.getChoices());
  }
  
  @Test
  public void testGetChoices() {
    assertEquals(0,quiz1.getChoices().size());
    Set<AvailableChoice> choices = new HashSet<AvailableChoice>();
    quiz2.setChoices(choices);
    assertEquals(choices, quiz2.getChoices());
  }
  
}
