package org.openclicker.server.resources;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/quiz")
public class QuizResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @GET
  @Path("/{quiz_uid_text}")
  @Produces("application/json")
  public String getQuiz(@PathParam("quiz_uid_text") String quiz_uid_text) {
    Integer quiz_uid = Integer.parseInt(quiz_uid_text);
    try {
      return toJSON(fetchQuiz(quiz_uid)).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  @GET
  @Path("/{quiz_uid_text}/answers")
  @Produces("application/json")
  public String getQuizAnswers(@PathParam("quiz_uid_text") String quiz_uid_text) {
    Integer quiz_uid = Integer.parseInt(quiz_uid_text);
    try {
      return toJSON(fetchQuizAnswersByUID(quiz_uid)).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  @GET
  @Path("/{quiz_uid_text}/choices")
  @Produces("application/json")
  public String getQuizChoices(@PathParam("quiz_uid_text") String quiz_uid_text) {
    Integer quiz_uid = Integer.parseInt(quiz_uid_text);
    try {
      return toJSON(fetchQuizChoicesByUID(quiz_uid)).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  @POST
  @Consumes("application/json")
  public Response addQuiz(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      addNewQuiz(json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  private int addNewQuiz(JSONObject json) {
    
    int id;
    
    Topic topic = parseTopicText(json.getString("topic"));
    Type type = parseTypeText(json.getString("type"));
    String question = json.getString("question");
    
    // Begin Hibernate Session
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      // Add a quiz and commit
      Quiz tempQuiz = new Quiz(topic, type, question);
      session.save(tempQuiz);
      
      id = tempQuiz.getQuiz_uid();
      session.getTransaction().commit();
      
      logger.info("Quiz successfully added");
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
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
  
  private static JSONObject toJSON(Collection<Integer> list) {
    JSONObject json = new JSONObject();
    
    JSONArray temp = new JSONArray();
    for (Integer i : list) {
      temp.add(i);
    }
    json.put("choices", temp);
    return json;
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
  
  private Collection<Integer> fetchQuizAnswersByUID(Integer quiz_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    HashSet<Integer> allAnswers = new HashSet<Integer>();
    
    if (tempQuiz != null) {
      for (AvailableChoice choice : tempQuiz.getAnswers_Unmodifiable()) {
        allAnswers.add(choice.getChoice_uid());
      }
      session.close();
      return allAnswers;
    } else {
      session.close();
      throw new EmptyValueException("quiz_uid " + quiz_uid
          + " is not available");
    }
  }
  
  private Collection<Integer> fetchQuizChoicesByUID(Integer quiz_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
    HashSet<Integer> allAnswers = new HashSet<Integer>();
    
    if (tempQuiz != null) {
      for (AvailableChoice choice : tempQuiz.getChoices_Unmodifiable()) {
        allAnswers.add(choice.getChoice_uid());
      }
      session.close();
      return allAnswers;
    } else {
      session.close();
      throw new EmptyValueException("quiz_uid " + quiz_uid
          + " is not available");
    }
  }
}
