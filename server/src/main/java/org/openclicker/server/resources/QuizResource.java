package org.openclicker.server.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.domain.Quiz.Topic;
import org.openclicker.server.domain.Quiz.Type;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.serverExceptions.WebBadRequestException;
import org.openclicker.server.util.serverExceptions.WebNotFoundException;
import org.openclicker.server.util.serverExceptions.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/quiz")
public class QuizResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @GET
  @Path("/{quiz_uid_text}")
  @Produces("application/json")
  public String getQuiz(@PathParam("quiz_uid_text") String quiz_uid_text) {
    try {
      Integer quiz_uid = Integer.parseInt(quiz_uid_text);
      return toJSON(fetchQuiz(quiz_uid)).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  @GET
  @Path("/{quiz_uid_text}/answers")
  @Produces("application/json")
  public String getQuizAnswers(@PathParam("quiz_uid_text") String quiz_uid_text) {
    Integer quiz_uid = Integer.parseInt(quiz_uid_text);
    try {
      return fetchQuizAnswersByUID(quiz_uid).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  @GET
  @Path("/{quiz_uid_text}/choices")
  @Produces("application/json")
  public String getQuizChoices(@PathParam("quiz_uid_text") String quiz_uid_text) {
    Integer quiz_uid = Integer.parseInt(quiz_uid_text);
    try {
      return fetchQuizChoicesByUID(quiz_uid).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  @POST
  @Consumes("application/json")
  public Response addQuiz(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int id = addNewQuiz(json);
      return Response.status(Status.ACCEPTED).entity(
          "New Quiz " + id + " added\n").type(
          MediaType.TEXT_PLAIN).build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (JSONException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  private int addNewQuiz(JSONObject json) {
    
    int id;
    
    Topic topic = parseTopicText(json.getString("topic"));
    Type type = parseTypeText(json.getString("type"));
    String question = json.getString("question");
    JSONArray choices = json.getJSONArray("choices");
    
    //Add all answers to a ArrayList for convenience
    JSONArray answerIndices = json.getJSONArray("answers");
    ArrayList<Integer> answers = new ArrayList<Integer>();
    for(int i = 0;i<answerIndices.size();i++){
      answers.add(answerIndices.getInt(i));
    }
    
    // Begin Hibernate Session
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      // Add a quiz 
      Quiz tempQuiz = new Quiz(topic, type, question);
      
      //Add the corresponding answers
      for(int i = 0;i<choices.size();i++){
        AvailableChoice choice = new AvailableChoice(choices.getString(i));
        session.save(choice);
        tempQuiz.addChoice(choice);
        if(answers.contains(i)){
          tempQuiz.addAnswer(choice);
        }
      }
      
      
      session.save(tempQuiz);
      
      id = tempQuiz.getQuiz_uid();
      session.getTransaction().commit();
      
      logger.info("Quiz successfully added");
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebServerException(e);
    }
    
  }
  
  private Type parseTypeText(String string) {
    Type type;
    if (string.equalsIgnoreCase("mc")) {
      type = Type.MC;
    } else {
      type = Type.SR;
    }
    return type;
  }
  
  private Topic parseTopicText(String topic_text) {
    Topic topic;
    if (topic_text.equalsIgnoreCase("english")) {
      topic = Topic.English;
    } else if (topic_text.equalsIgnoreCase("science")) {
      topic = Topic.Science;
    } else if (topic_text.equalsIgnoreCase("math")) {
      topic = Topic.Math;
    } else {
      topic = Topic.Other;
    }
    return topic;
  }
  
  public static JSONObject toJSON(Quiz quiz) {
    JSONObject json = new JSONObject();
    json.put("quiz_uid", quiz.getQuiz_uid());
    json.put("question", quiz.getQuestion());
    json.put("topic", quiz.getTopic());
    json.put("type", quiz.getType());
    return json;
  }
  
  public static JSONArray toJSON(Collection<Quiz> listOfQuizes) {
    JSONArray array = new JSONArray();
    for (Quiz quiz : listOfQuizes) {
      array.add(toJSON(quiz));
    }
    return array;
  }
  
  
  private Quiz fetchQuiz(int quiz_uid) throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    session.close();
    if (tempQuiz != null) {
      return tempQuiz;
    } else {
      throw new EmptyValueException("quiz_uid " + quiz_uid
          + " is not available");
    }
  }
  
  private JSONArray fetchQuizAnswersByUID(Integer quiz_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    if (tempQuiz != null) {
      JSONArray a = AvailableChoiceResource.toJSON(tempQuiz.getAnswers_Unmodifiable());
      session.close();
      return a;
    } else {
      session.close();
      throw new EmptyValueException("quiz_uid " + quiz_uid
          + " is not available");
    }
  }
  
  private JSONArray fetchQuizChoicesByUID(Integer quiz_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    
    if (tempQuiz != null) {
      JSONArray allAnswers = AvailableChoiceResource.toJSON(tempQuiz.getChoices_Unmodifiable());
      session.close();
      return allAnswers;
    } else {
      session.close();
      throw new EmptyValueException("quiz_uid " + quiz_uid
          + " is not available");
    }
  }
}
