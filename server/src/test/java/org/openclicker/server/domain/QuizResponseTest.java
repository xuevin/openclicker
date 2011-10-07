package org.openclicker.server.domain;

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
  
  @Before
  public void setUp() {
    
    quizResponse1 = new QuizResponse();
    todaysDate = new Date();
    choice = new AvailableChoice("Foobar");
    quiz = new Quiz(Topic.English, Type.MC, "What is foo?");
    quizResponse2 = new QuizResponse(quiz, choice, todaysDate);
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
    assertEquals(date,quizResponse2.getDate_taken());
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
    assertEquals(null,quizResponse1.getQuiz());
    assertEquals(choice,quizResponse2.getSelected_choice());
  }
  
}
