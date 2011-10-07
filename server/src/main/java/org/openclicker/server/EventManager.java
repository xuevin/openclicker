package org.openclicker.server;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.HibernateUtil;

public class EventManager {
  public static void main(String[] args) {
    EventManager mgr = new EventManager();
    
//    createAndStoreEvent();
    
    if (args[0].equals("list")) {
      List students = mgr.listStudents();
      for (int i = 0; i < students.size(); i++) {
        Student theStudent = (Student) students.get(i);
        System.out.println("Student: " + theStudent.getFirst_name());
      }
    }
    
//    if (args[0].equals("store")) {
//      mgr.createAndStoreEvent("My Event", new Date());
//    } else if (args[0].equals("list")) {
//      List events = mgr.listEvents();
//      for (int i = 0; i < events.size(); i++) {
//        Event theEvent = (Event) events.get(i);
//        System.out.println("Event: " + theEvent.getTitle() + " Time: "
//            + theEvent.getDate());
//      }
//    }
    
    HibernateUtil.getSessionFactory().close();
  }
  
  private List listStudents() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List result = session.createQuery("from Student").list();
    session.getTransaction().commit();
    return result;
    
  }
  
  // private List listEvents() {
  // Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  // session.beginTransaction();
  // List result = session.createQuery("from Event").list();
  // session.getTransaction().commit();
  // return result;
  // }
  //  
  
   private static void createAndStoreEvent() {
   Session session = HibernateUtil.getSessionFactory().getCurrentSession();
   session.beginTransaction();
   
   Student robert = new Student("Robert", "Berry", Gender.M, "robberry@hunter.cuny.edu");
   
   Class englishClass = new Class("English");
//   englishClass.addStudent(robert);
//   
//   Quiz quiz1 = new Quiz(Topic.English, Type.MC, "Who wrote Harry Potter?");
//   
//   AvailableChoice choice1 = new AvailableChoice("Steven King");
//   AvailableChoice choice2 = new AvailableChoice("J.R.R Tolkien");
//   AvailableChoice choice3 = new AvailableChoice("J.K Rowling");
//   AvailableChoice choice4 = new AvailableChoice("Dr. Seuss");
//   
//   quiz1.addChoice(choice1);
//   quiz1.addChoice(choice2);
//   quiz1.addChoice(choice3);
//   quiz1.addChoice(choice4);
//   quiz1.addAnswer(choice3);
//   
//   englishClass.addQuizes(quiz1);
//   
//   QuizResponse response = new QuizResponse(quiz1, choice3, new Date());
//   
//   robert.addQuizReponse(response);
//   
   session.save(robert);
   session.save(englishClass);
//   session.save(quiz1);
//   session.save(choice1);
//   session.save(choice2);
//   session.save(choice3);
//   session.save(choice4);
//   session.save(response);
//   
   session.getTransaction().commit();
   }
  //  
  // private void addPersonToEvent(Long personId, Long eventId) {
  // Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  // session.beginTransaction();
  //    
  // Person aPerson = (Person) session.load(Person.class, personId);
  // Event anEvent = (Event) session.load(Event.class, eventId);
  // aPerson.addToEvent(anEvent);
  //    
  // session.getTransaction().commit();
  // }
  //  
  // private void addEmailToPerson(Long personId, String emailAddress) {
  // Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  // session.beginTransaction();
  //    
  // Person aPerson = (Person) session.load(Person.class, personId);
  // // adding to the emailAddress collection might trigger a lazy load of the
  // // collection
  // aPerson.getEmailAddresses().add(emailAddress);
  //    
  // session.getTransaction().commit();
  // }
  
}
