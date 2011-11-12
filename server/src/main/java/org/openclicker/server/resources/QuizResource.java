package org.openclicker.server.resources;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.JSONUtils;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class QuizResource extends ServerResource {
  
  @Get
  public String retrieve() {
    Integer quiz_uid = Integer.parseInt((String) getRequestAttributes().get(
        "quiz_uid"));
    try {
      return toJSON(fetchQuiz(quiz_uid)).toString();
    } catch (EmptyValueException e) {
      return JSONUtils.createNewError(e.getMessage()).toString();
    }
    
  }
  
  private static JSONObject toJSON(Quiz quiz) {
    JSONObject json = new JSONObject();
    json.put("quiz_uid", quiz.getQuiz_uid());
    json.put("question", quiz.getQuestion());
    json.put("topic", quiz.getTopic());
    json.put("type", quiz.getType());
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
}
