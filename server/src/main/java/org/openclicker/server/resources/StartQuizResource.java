package org.openclicker.server.resources;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.resources.containers.ClassQuizIdentifier;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.SetStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Hypermedia Actions
@Path("class/{class_uid_text}/quiz/{quiz_uid_text}")
public class StartQuizResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  public static final Collection<ClassQuizIdentifier> activeClassQuizContainer = new HashSet<ClassQuizIdentifier>();
  
  @GET
  @Produces("application/json")
  public String getStatus(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text) {
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      Integer quiz_uid = Integer.parseInt(quiz_uid_text);
      
      return getStatusOfQuiz(class_uid, quiz_uid);
      
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    
  }
  
  @POST
  @Path("/start")
  public Response start(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text, String context) {
    setStatusOfQuiz(class_uid_text, quiz_uid_text, true);
    return Response.ok().entity("").build();
    
  }
  
  @POST
  @Path("/stop")
  public Response stop(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text, String context) {
    setStatusOfQuiz(class_uid_text, quiz_uid_text, false);
    return Response.ok().entity("").build();
  }
  
  private void setStatusOfQuiz(String class_uid_text, String quiz_uid_text,
      boolean acceptResponses) {
    
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      Integer quiz_uid = Integer.parseInt(quiz_uid_text);
      
      // TODO Set this to true when you want to commit
      setStatusOfQuiz(class_uid, quiz_uid, acceptResponses, false);
      
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } catch (SetStatusException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  private String getStatusOfQuiz(Integer class_uid, Integer quiz_uid) {
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    // Attempt to get the class and quiz
    Class tempClass = (Class) session.get(Class.class, class_uid);
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    if (tempClass == null) {
      return ("Class is not available");
    } else if (tempQuiz == null) {
      return ("Quiz is not available");
    }
    
    ClassQuizIdentifier classQuizPair = new ClassQuizIdentifier(class_uid,
        quiz_uid);
    
    if (activeClassQuizContainer.contains(classQuizPair)) {
      return "The quiz is currently in progress";
    }
    
    // Situation when the quiz in question has already been asked
    if (tempClass.getQuizzes_Unmodifiable().contains(tempQuiz)) {
      return ("Quiz " + quiz_uid
          + " is no longer accepting responses for class " + class_uid);
    } else {
      return "The Quiz has not been asked for this class yet";
    }
  }
  
  protected void setStatusOfQuiz(int class_uid, int quiz_uid,
      boolean acceptResponses, boolean commit) throws EmptyValueException,
      SetStatusException {
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      
      // Attempt to get the class and quiz
      Class tempClass = (Class) session.get(Class.class, class_uid);
      Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
      if (tempClass == null) {
        throw new EmptyValueException("Class is not available");
      } else if (tempQuiz == null) {
        throw new EmptyValueException("Quiz is not available");
      }
      
      ClassQuizIdentifier classQuizPair = new ClassQuizIdentifier(class_uid,
          quiz_uid);
      
      if (activeClassQuizContainer.contains(classQuizPair)) {
        // The only thing you can do is stop the quiz!
        if (acceptResponses == false) {
          activeClassQuizContainer.remove(classQuizPair);
          logger.info("Quiz " + quiz_uid + " successfully stopped for "
              + class_uid);
          return;
        } else {
          throw new SetStatusException("Quiz " + quiz_uid + " in class "
              + class_uid + " is already active!");
        }
      }
      
      // Situation when the quiz in question has already been asked
      if (tempClass.getQuizzes_Unmodifiable().contains(tempQuiz)) {
        throw new SetStatusException("Quiz " + quiz_uid
            + " is no longer accepting responses for class " + class_uid);
      }
      
      // Time to add a new quiz to the class
      if (acceptResponses == false) {
        throw new SetStatusException("Cannot stop a quiz which is not active!");
      } else {
        // Add the quiz to the class
        tempClass.addQuizes(tempQuiz);
        session.save(tempClass);// Not sure if this will work
        if (commit) {
          session.getTransaction().commit();
        } else {
          session.close();
        }
        logger.info("Quiz " + quiz_uid + " successfully started for Class "
            + class_uid);
        activeClassQuizContainer.add(classQuizPair);
      }
    } catch (HibernateException e) {
      session.close();
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}
