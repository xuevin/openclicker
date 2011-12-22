package org.openclicker.server.resources;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.resources.containers.ClassQuizIdentifier;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.serverExceptions.WebBadRequestException;
import org.openclicker.server.util.serverExceptions.WebNotFoundException;
import org.openclicker.server.util.serverExceptions.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student/{student_uid_text}/class/{class_uid_text}/quiz/{quiz_uid_text}")
public class QuizResponseResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @POST
  @Consumes("application/json")
  public Response addResponse(@PathParam("quiz_uid_text") String quiz_uid_text,
      @PathParam("student_uid_text") String student_uid_text,
      @PathParam("class_uid_text") String class_uid_text, String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int quiz_uid = Integer.parseInt(quiz_uid_text);
      int student_uid = Integer.parseInt(student_uid_text);
      int class_uid = Integer.parseInt(class_uid_text);
      addNewResponse(class_uid, quiz_uid, student_uid, json);
      return Response.status(Status.ACCEPTED).entity(
          "New Response for class " + class_uid + ", quiz " + quiz_uid
              + ", student " + student_uid + " added\n").type(
          MediaType.TEXT_PLAIN).build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (JSONException e) {
      throw new WebBadRequestException(e);
    }
  }
  
  private void addNewResponse(int classUid, int quizUid, int studentUid,
      JSONObject json) throws EmptyValueException {
    ClassQuizIdentifier identifier = new ClassQuizIdentifier(classUid, quizUid);
    if (StartQuizResource.activeClassQuizContainer.contains(identifier)) {
      
      // Begin Hibernate Session
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        
        session.beginTransaction();
        
        // Attempt to get the class and quiz
        Class tempClass = (Class) session.load(Class.class, classUid);
        Quiz tempQuiz = (Quiz) session.load(Quiz.class, quizUid);
        Student tempStudent = (Student) session.load(Student.class, studentUid);
        
        if (tempClass == null) {
          session.close();
          throw new EmptyValueException("Class is not available");
        } else if (tempQuiz == null) {
          session.close();
          throw new EmptyValueException("Quiz is not available");
        } else if (tempStudent == null) {
          session.close();
          throw new EmptyValueException("Student is not available");
        }
        //        
        String response = json.getString("response");
        AvailableChoice tempChoice;
        logger.info(tempQuiz.getType());
        logger.info(response);
        
        if (tempQuiz.getType().equalsIgnoreCase((Quiz.Type.MC).toString())) {
          int choiceUID = Integer.parseInt(response);
          logger.info(choiceUID + "");
          tempChoice = (AvailableChoice) session.get(AvailableChoice.class,
              choiceUID);
          if (tempChoice == null) {
            throw new EmptyValueException(
                "The following availablechoice UID is invalid: " + choiceUID);
          }
        } else {
          logger.info("New Response Created!");
          tempChoice = new AvailableChoice(response);
        }
        
        QuizResponse newQuizResponse = new QuizResponse(tempClass, tempQuiz,
            tempChoice, new Date());
        tempStudent.addQuizResponse(newQuizResponse);
        
        session.save(tempStudent);
        
        logger.info("Created Response");
        
        //        
        // //TODO FIXME
        // session.update(tempStudent);
        // logger.info("QuizResponse successfully Saved");
        // logger.debug(tempChoice.getDescription());
        // logger.debug("HELP");
         session.getTransaction().commit();
        
      } catch (HibernateException e) {
        session.close();
        throw new WebServerException(e);
      }
      
    } else {
      throw new EmptyValueException(
          "The class is currently not asking this quiz.");
    }
    
  }
}
