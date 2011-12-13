package org.openclicker.server.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import org.openclicker.server.util.SetStatusException;
import org.openclicker.server.util.serverExceptions.WebBadRequestException;
import org.openclicker.server.util.serverExceptions.WebNotFoundException;
import org.openclicker.server.util.serverExceptions.WebServerException;
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
      throw new WebBadRequestException(e);
    }
    
  }
  
  @POST
  @Path("/start")
  public Response start(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text, String context) {
    setStatusOfQuiz(class_uid_text, quiz_uid_text, true);
    return Response.status(Status.ACCEPTED).entity(
        "Quiz " + quiz_uid_text + " Started\n").type(MediaType.TEXT_PLAIN)
        .build();
    
  }
  
  @POST
  @Path("/stop")
  public Response stop(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text, String context) {
    setStatusOfQuiz(class_uid_text, quiz_uid_text, false);
    return Response.status(Status.ACCEPTED).entity(
        "Quiz " + quiz_uid_text + " Stopped\n").type(MediaType.TEXT_PLAIN)
        .build();
  }
  
  @GET
  @Path("/stats")
  @Produces("application/json")
  public String getStats(@PathParam("class_uid_text") String class_uid_text,
      @PathParam("quiz_uid_text") String quiz_uid_text) {
    
    try {
      int class_uid = Integer.parseInt((String) class_uid_text);
      int quiz_uid = Integer.parseInt(quiz_uid_text);
      
      return getQuizStatsAsJSONObj(class_uid, quiz_uid).toString();
      
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  private void setStatusOfQuiz(String class_uid_text, String quiz_uid_text,
      boolean acceptResponses) {
    
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      Integer quiz_uid = Integer.parseInt(quiz_uid_text);
      
      // TODO Set this to true when you want to commit
      setStatusOfQuiz(class_uid, quiz_uid, acceptResponses, true);
      
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (SetStatusException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
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
  
  private JSONObject getQuizStatsAsJSONObj(int classUid, int quizUid)
      throws EmptyValueException {
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Class tempClass = (Class) session.get(Class.class, classUid);
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quizUid);

    if(!tempClass.getQuizzes_Unmodifiable().contains(tempQuiz)){
      throw new EmptyValueException("The class has never asked this quiz");
    }
    
    // FIXME - THis is a disgusting hack to get the job done.
    // You should really only be doing one query but this explodes!
    
    // Instantiate the choices to zero
    HashMap<String,Integer> choiceAndCount = new HashMap<String,Integer>();
    for (AvailableChoice choices : tempQuiz.getChoices_Unmodifiable()) {
      choiceAndCount.put(choices.getDescription(), 0);
    }
    
    // Tally up the scores
    Collection<Student> tempStudents = tempClass.getStudents_Unmodifiable();
    for (Student student : tempStudents) {
      Collection<QuizResponse> responses = student
          .getQuizResponse_Unmodifiable();
      for (QuizResponse response : responses) {
        if (response.getQuiz().equals(tempQuiz)) {
          String selectedString = response.getSelected_choice()
              .getDescription();
          choiceAndCount.put(selectedString,
              choiceAndCount.get(selectedString) + 1);
        }
      }
    }
    
    // JSON-ify all the choices/count
    JSONArray choiceArray = new JSONArray();
    for (String key : choiceAndCount.keySet()) {
      JSONObject temp = new JSONObject();
      temp.put("description", key);
      temp.put("count", choiceAndCount.get(key));
      choiceArray.add(temp);
    }
    // JSON-ify all the answers
    JSONArray answerArray = new JSONArray();
    for (AvailableChoice answer : tempQuiz.getAnswers_Unmodifiable()) {
      answerArray.add(answer.getDescription());
    }
    
    session.close();
    
    JSONObject rootObj = new JSONObject();
    rootObj.put("choices", choiceArray);
    rootObj.put("answers", answerArray);
    return rootObj;
    
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
      throw new WebServerException(e);
    }
  }
}
