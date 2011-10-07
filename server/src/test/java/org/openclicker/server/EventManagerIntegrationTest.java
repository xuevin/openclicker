package org.openclicker.server;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.HibernateUtil;

public class EventManagerIntegrationTest {
  
  @Before
  public void setUp() throws Exception {

  }
  
  @Test
  public void testAddNewClass() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Add a class
    Class englishClass = new Class("TESTCLASS");
    session.save(englishClass);
    session.getTransaction().commit();
    
    // Make a new session
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Retrieve and check to make sure that they are the same
    Class tempClass = (Class) session.get(Class.class, englishClass
        .getClass_uid());
    assertNotNull(tempClass);
    assertEquals(englishClass, tempClass);
    
    // Delete
    session.delete(session.get(Class.class, englishClass.getClass_uid()));
    session.getTransaction().commit();
    
  }
  
  @Test
  public void testAddNewStudent() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Add a student
    Student testStudent = new Student("TEST", "TEST", Gender.M,
        "TEST@hunter.cuny.edu");
    session.save(testStudent);
    session.getTransaction().commit();
    
    // Make a new session
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Retrieve and check to make sure that they are the same
    Student tempStudent = (Student) session.get(Student.class, testStudent
        .getStudent_uid());
    assertNotNull(tempStudent);
    assertEquals(testStudent, tempStudent);
    
    // Delete
    session.beginTransaction();
    session.delete(session.get(Student.class, testStudent.getStudent_uid()));
    session.getTransaction().commit();
  }
  
  @Test
  public void testAddNewQuiz() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Add a quiz
    Quiz testQuiz = new Quiz(Topic.English, Type.MC,
        "What is the world made of?");
    session.save(testQuiz);
    session.getTransaction().commit();
    
    // Make a new session
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Retrieve and check to make sure that they are the same
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, testQuiz.getQuiz_uid());
    assertNotNull(tempQuiz);
    assertEquals(testQuiz, tempQuiz);
    
    // Delete
    session.beginTransaction();
    session.delete(session.get(Quiz.class, testQuiz.getQuiz_uid()));
    session.getTransaction().commit();
  }
  
  @Test
  public void testAddNewAvailableChoice() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Add a AvailableChoice
    AvailableChoice testChoice = new AvailableChoice("Chocolate");
    session.save(testChoice);
    session.getTransaction().commit();
    
    // Make a new session
    session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Retrieve and check to make sure that they are the same
    AvailableChoice tempChoice = (AvailableChoice) session.get(
        AvailableChoice.class, testChoice.getChoice_uid());
    assertNotNull(tempChoice);
    assertEquals(testChoice, tempChoice);
    
    // Delete
    session.beginTransaction();
    session.delete(session.get(AvailableChoice.class, testChoice
        .getChoice_uid()));
    session.getTransaction().commit();
  }
  
  @Test
  public void testAddNewQuizResponse() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Add a AvailableChoice
    Student robert = new Student("TEST", "TEST", Gender.M, "TEST@hunter.cuny.edu");
    
    Class englishClass = new Class("English");
    englishClass.addStudent(robert);
    
    Quiz quiz1 = new Quiz(Topic.English, Type.MC, "Who wrote Harry Potter?");
    
    AvailableChoice choice1 = new AvailableChoice("Steven King");
    AvailableChoice choice2 = new AvailableChoice("J.R.R Tolkien");
    AvailableChoice choice3 = new AvailableChoice("J.K Rowling");
    AvailableChoice choice4 = new AvailableChoice("Dr. Seuss");
    
    quiz1.addChoice(choice1);
    quiz1.addChoice(choice2);
    quiz1.addChoice(choice3);
    quiz1.addChoice(choice4);
    quiz1.addAnswer(choice3);
    
    englishClass.addQuizes(quiz1);
    
    QuizResponse response = new QuizResponse(quiz1, choice3, new Date());
    
    robert.addQuizReponse(response);
    
    session.save(choice1);
    session.save(choice2);
    session.save(choice3);
    session.save(choice4);
    session.save(englishClass);
    session.save(quiz1);
    session.save(robert);
    
    for(QuizResponse r : robert.getQuizResponse_Unmodifiable()){
      System.out.println(r.getQuiz().getQuestion());
      System.out.println(r.getSelected_choice().getDescription());
      System.out.println(r.getDate_taken());
      
    }
    
//    session.getTransaction().commit();
    
    // Make a new session
//    session = HibernateUtil.getSessionFactory().getCurrentSession();
//    session.beginTransaction();
    
//    // Retrieve and check to make sure that they are the same
//    Student tempStudent = (Student) session.get(Student.class, robert.getStudent_uid());
//    assertTrue(tempStudent.getQuizResponse_Unmodifiable().contains(response));
//    assertEquals(testChoice, tempChoice);
//    
//    // Delete
//    session.beginTransaction();
//    session.delete(session.get(AvailableChoice.class, testChoice
//        .getChoice_uid()));
//    session.getTransaction().commit();
  }
  
}
