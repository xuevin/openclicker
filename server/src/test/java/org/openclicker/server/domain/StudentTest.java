package org.openclicker.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;
import org.openclicker.server.domain.Student.Gender;

public class StudentTest {
  
  private Student student1;
  private Student student2;
  
  @Before
  public void setUp() {
    student1 = new Student();
    student2 = new Student("Robert", "Revolt", Gender.M, "RobRev@gmail.com");
  }
  
  @Test
  public void testStudent() {
    assertNotNull(student1);
  }
  
  @Test
  public void testStudentStringStringGenderString() {
    assertNotNull(student2);
  }
  
  @Test
  public void testGetStudent_uid() {
    assertEquals(0, student2.getStudent_uid());
  }
  
  @Test
  public void testSetEmail_address() {
    student1.setEmail_address("foo@bar.org");
    assertEquals("foo@bar.org", student1.getEmail_address());
    assertEquals("RobRev@gmail.com", student2.getEmail_address());
  }
  
  @Test
  public void testGetEmail_address() {
    assertEquals("RobRev@gmail.com", student2.getEmail_address());
  }
  
  @Test
  public void testSetFirst_name() {
    student1.setFirst_name("Rob");
    assertEquals("Rob", student1.getFirst_name());
  }
  
  @Test
  public void testGetFirst_name() {
    assertEquals("Robert", student2.getFirst_name());
  }
  
  @Test
  public void testSetLast_name() {
    student1.setLast_name("Bellow");
    assertEquals("Bellow", student1.getLast_name());
  }
  
  @Test
  public void testGetLast_name() {
    assertEquals("Revolt", student2.getLast_name());
  }
  
  @Test
  public void testSetGenderGender() {
    student1.setGender(Gender.F);
    assertEquals("F", student1.getGender());
  }
  
  @Test
  public void testGetGender() {
    assertEquals("M", student2.getGender());
  }
  
  @Test
  public void testAddQuizReponse() {
    assertEquals(student1.getQuizReponses().size(), 0);
    
    Quiz quiz = new Quiz(Topic.English, Type.MC, "FOO");
    AvailableChoice choice = new AvailableChoice("Grapes");
    QuizResponse quizResponse = new QuizResponse(quiz, choice, new Date());
    student1.addQuizReponse(quizResponse);
    
    assertEquals(student1.getQuizReponses().size(), 1);
  }
  
  @Test
  public void testRemoveQuizResponse() {
    Quiz quiz = new Quiz(Topic.English, Type.MC, "FOO");
    AvailableChoice choice = new AvailableChoice("Grapes");
    QuizResponse quizResponse = new QuizResponse(quiz, choice, new Date());
    student1.addQuizReponse(quizResponse);
    assertEquals(student1.getQuizReponses().size(), 1);
    student1.removeQuizResponse(quizResponse);
    assertEquals(student1.getQuizReponses().size(), 0);
    
  }
  
  @Test
  public void testAddToClass() {
    
    assertEquals(0, student1.getClasses().size());
    Class englishClass = new Class("English");
    student1.addToClass(englishClass);
    assertEquals(1, student1.getClasses().size());
  }
  
  @Test
  public void testRemoveFromClass() {
    Class englishClass = new Class("English");
    student1.addToClass(englishClass);
    assertEquals(1, student1.getClasses().size());
    student1.removeFromClass(englishClass);
    assertEquals(0, student1.getClasses().size());
  }
  
  @Test
  public void testSetStudent_uid() {
    Set<Class> setOfClassess = new HashSet<Class>();
    student1.setClasses(setOfClassess);
    assertEquals(setOfClassess, student1.getClasses());
  }
  
  @Test
  public void testSetQuizResponses(){
    Set<QuizResponse> quizResponses = new HashSet<QuizResponse>();
    student1.setQuizReponses(quizResponses);
    assertEquals(quizResponses, student1.getQuizReponses());
  }
}
