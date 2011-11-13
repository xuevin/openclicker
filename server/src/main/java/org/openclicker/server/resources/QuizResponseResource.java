package org.openclicker.server.resources;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.Session;
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("student/{student_uid_text}/quiz/{quiz_uid_text}")
public class QuizResponseResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @GET
  @Produces("application/json")
  public String retrieve(@PathParam("quiz_uid_text") String student_uid_text,
      @PathParam("student_uid_text") String quiz_uid_text) {
    try {
      int quiz_uid = Integer.parseInt(quiz_uid_text);
      int student_uid = Integer.parseInt(student_uid_text);
      
      return getJSONByStudentQuizUID(student_uid, quiz_uid).toString();
      
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
  public Response addChoice(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      addNewResponse(json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    
  }
  
  
  
  
  private void addNewResponse(JSONObject json) {
    // TODO Auto-generated method stub
    
  }
  private JSONObject toJSON(QuizResponse response) {
    JSONObject object = new JSONObject();
    object.put("date", response.getDate_taken());
    object.put("availablechoice_uid", response.getSelected_choice().getChoice_uid());
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
