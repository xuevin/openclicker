package org.openclicker.server.resources;

import java.util.Set;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class QuizResponseResource extends ServerResource {
  
  @Get
  public String retrieve() {
    
    try {
      int quiz_uid = Integer.parseInt((String) getRequestAttributes().get(
          "quiz_uid"));
      int student_uid = Integer.parseInt((String) getRequestAttributes().get(
          "student_uid"));
      
      return getJSONByStudentQuizUID(student_uid, quiz_uid).toString();
      
    } catch (EmptyValueException e) {
      throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    } catch (NumberFormatException e) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    }
    
  }
  
  private JSONObject toJSON(QuizResponse response) {
    JSONObject object = new JSONObject();
    object.put("foo", response.getDate_taken());
    object.put("bar", response.getSelected_choice().getChoice_uid());
    return object;
  }
  
  private JSONObject getJSONByStudentQuizUID(int student_uid, int quiz_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    
    Student tempStudent = (Student) session.get(Student.class, student_uid);
    if (tempStudent == null) {
      throw new EmptyValueException("Such a student does not exist");
    } else {
      Set<QuizResponse> allQuizeResponses = tempStudent
          .getQuizResponse_Unmodifiable();
      for (QuizResponse instance : allQuizeResponses) {
        if (instance.getQuiz().getQuiz_uid() == quiz_uid) {
          session.close();
          return toJSON(instance);
        }
      }
      session.close();
      throw new EmptyValueException("Such a quiz was not found");
    }
    
  }
  
}
