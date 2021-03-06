package org.openclicker.server.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;

public class QuizResponseTest {
  private QuizResponse quizResponse1;
  private QuizResponse quizResponse2;
  private Date todaysDate;
  private AvailableChoice choice;
  private Quiz quiz;
  private Class class_taken;
  
  @Before
  public void setUp() {
    
    quizResponse1 = new QuizResponse();
    todaysDate = new Date();
    choice = new AvailableChoice("Foobar");
    quiz = new Quiz(Topic.English, Type.MC, "What is foo?");
    class_taken = new Class("English 100");
    quizResponse2 = new QuizResponse(class_taken, quiz, choice, todaysDate);
  }
  
  @Test
  public void testQuizResponse() {
    assertNotNull(quizResponse1);
  }
  
  @Test
  public void testQuizResponseQuizAvailableChoiceDate() {
    assertNotNull(quizResponse2);
  }
  
  @Test
  public void testSetQuiz() {
    
    quizResponse1.setQuiz(quiz);
    assertEquals(quiz, quizResponse1.getQuiz());
  }
  
  @Test
  public void testGetQuiz() {
    assertEquals(null, quizResponse1.getQuiz());
    quizResponse2.setQuiz(quiz);
    assertEquals(quiz, quizResponse2.getQuiz());
  }
  
  @Test
  public void testSetDate_taken() {
    Date date = new Date();
    quizResponse2.setDate_taken(date);
    assertEquals(date, quizResponse2.getDate_taken());
  }
  
  @Test
  public void testGetDate_taken() {
    assertEquals(null, quizResponse1.getDate_taken());
    assertEquals(todaysDate, quizResponse2.getDate_taken());
  }
  
  @Test
  public void testSetSelected_choice() {
    quizResponse1.setSelected_choice(choice);
    assertEquals(choice, quizResponse1.getSelected_choice());
  }
  
  @Test
  public void testGetSelected_choice() {
    assertEquals(null, quizResponse1.getQuiz());
    assertEquals(choice, quizResponse2.getSelected_choice());
  }
  
  @Test
  public void testSetClass() {
    quizResponse1.setClass_taken(class_taken);
    assertEquals(class_taken, quizResponse1.getClass_taken());
  }
  
  @Test
  public void testGetClass() {
    assertEquals(null, quizResponse1.getClass_taken());
    assertEquals(class_taken, quizResponse2.getClass_taken());
  }
  
  @Test
  public void testEquals() {
    assertThat(quizResponse1, not(equalTo(quizResponse2)));
    
    quizResponse1.setQuiz(quiz);
    quizResponse1.setSelected_choice(choice);
    quizResponse1.setDate_taken(todaysDate);
    quizResponse1.setClass_taken(class_taken);
    assertEquals(quizResponse1, quizResponse2);
    
  }
  
}
