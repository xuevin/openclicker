package org.openclicker.server.resources;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.resources.containers.ClassQuizIdentifier;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student/{student_uid_text}/class/{class_uid_text}/quiz/{quiz_uid_text}")
public class QuizResponseResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @POST
  @Consumes("application/json")
  public Response addResponse(
      @PathParam("quiz_uid_text") String student_uid_text,
      @PathParam("student_uid_text") String quiz_uid_text,
      @PathParam("class_uid_text") String class_uid_text, String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int quiz_uid = Integer.parseInt(quiz_uid_text);
      int student_uid = Integer.parseInt(student_uid_text);
      int class_uid = Integer.parseInt(class_uid_text);
      addNewResponse(class_uid, quiz_uid, student_uid, json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
  }
  
  private void addNewResponse(int classUid, int quizUid, int studentUid,
      JSONObject json) throws EmptyValueException {
    ClassQuizIdentifier foo = new ClassQuizIdentifier(classUid, quizUid);
    if (StartQuizResource.activeClassQuizContainer.contains(foo)) {
      
      // Begin Hibernate Session
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        
        session.beginTransaction();
        
        // Attempt to get the class and quiz
        Class tempClass = (Class) session.get(Class.class, classUid);
        Quiz tempQuiz = (Quiz) session.get(Quiz.class, quizUid);
        Student tempStudent = (Student) session.get(Student.class, studentUid);
        if (tempClass == null) {
          throw new EmptyValueException("Class is not available");
        } else if (tempQuiz == null) {
          throw new EmptyValueException("Quiz is not available");
        } else if (tempStudent == null) {
          throw new EmptyValueException("Student is not available");
        }
        
        String response = json.get("response").toString();
        AvailableChoice tempChoice;
        if (tempQuiz.getType().equals(Quiz.Type.MC)) {
          int choiceUID = Integer.parseInt(response);
          tempChoice = (AvailableChoice) session.get(AvailableChoice.class,
              choiceUID);
          
        } else {
          tempChoice = new AvailableChoice(response);
        }
        
        QuizResponse newQuizResponse = new QuizResponse(tempClass, tempQuiz,
            tempChoice, new Date());
        session.save(tempQuiz);
        logger.info("QuizResponse successfully Saved");
        session.getTransaction().commit();
        
      } catch (HibernateException e) {
        session.close();
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
      }
      
    } else {
      throw new EmptyValueException(
          "The class is currently not asking this quiz.");
    }
    
  }
}
