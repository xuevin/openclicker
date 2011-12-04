package org.openclicker.server.resources;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
import org.openclicker.server.domain.QuizResponse;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.serverExceptions.WebBadRequestException;
import org.openclicker.server.util.serverExceptions.WebNotFoundException;
import org.openclicker.server.util.serverExceptions.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/student")
public class StudentResource {
  
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @GET
  @Path("/{student_uid_text}")
  @Produces("application/json")
  public String getStudent(
      @PathParam("student_uid_text") String student_uid_text) {
    try {
      Integer student_uid = Integer.parseInt((String) student_uid_text);
      return toJSON(fetchStudent(student_uid)).toString();
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
  public Response addStudent(String context) {
    
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int id = addNewStudent(json);
      return Response.status(Status.ACCEPTED).entity(
          "New Student " + id  + " added\n").type(MediaType.TEXT_PLAIN).build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (JSONException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
    
  }
  
  @GET
  @Produces("application/json")
  @Path("/{student_uid_text}/class/{class_uid_text}/quiz")
  public String getAllQuizesTakenInClass(
      @PathParam("student_uid_text") String student_uid_text,
      @PathParam("class_uid_text") String class_uid_text) {
    try {
      int class_uid = Integer.parseInt(class_uid_text);
      int student_uid = Integer.parseInt(student_uid_text);
      
      return getJSONArrayOfQuizResponses(student_uid, class_uid).toString();
      
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  public static JSONObject toJSON(Student student) {
    
    JSONObject object = new JSONObject();
    object.put("student_uid", student.getStudent_uid());
    object.put("gender", student.getGender());
    object.put("email", student.getEmail_address());
    object.put("first_name", student.getFirst_name());
    object.put("last_name", student.getLast_name());
    return object;
  }
  
  public static JSONArray toJSON(Collection<Student> studentsUnmodifiable) {
    JSONArray array = new JSONArray();
    for (Student student : studentsUnmodifiable) {
      array.add(toJSON(student));
    }
    return array;
  }
  
  private int addNewStudent(JSONObject json) {
    int id;
    
    // Parse Gender
    String genderAsString = json.getString("gender");
    Gender gender;
    if (genderAsString.toLowerCase().equals("m")) {
      gender = Gender.M;
    } else if (genderAsString.toLowerCase().equals("f")) {
      gender = Gender.F;
    } else {
      throw new WebBadRequestException("Gender should only be M or F");
    }
    
    // Parse Remaining Options
    String email = json.getString("email");
    String first_name = json.getString("first_name");
    String last_name = json.getString("last_name");
    
    // Begin Hibernate Session
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      // Add a student and commit
      Student testStudent = new Student(first_name, last_name, gender, email);
      session.save(testStudent);
      
      id = testStudent.getStudent_uid();
      session.getTransaction().commit();
      
      logger.info("Student successfully added");
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebServerException(e);
    }
  }
  
  private Student fetchStudent(int student_uid) throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Student tempStudent = (Student) session.get(Student.class, student_uid);
    session.close();
    if (tempStudent != null) {
      return tempStudent;
    } else {
      throw new EmptyValueException("student_uid " + student_uid
          + " is not available");
    }
  }
  
  private JSONArray getJSONArrayOfResponses(Collection<QuizResponse> responses) {
    JSONArray array = new JSONArray();
    for (QuizResponse instance : responses) {
      JSONObject object = new JSONObject();
      object.put("quiz_uid", instance.getQuiz().getQuiz_uid());
      object.put("select_choice_uid", instance.getSelected_choice()
          .getChoice_uid());
      object.put("class_uid", instance.getClass_taken().getClass_uid());
      array.add(object);
    }
    return array;
  }
  
  private JSONArray getJSONArrayOfQuizResponses(int student_uid, int class_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Student tempStudent = (Student) session.get(Student.class, student_uid);
    if (tempStudent == null) {
      session.close();
      throw new EmptyValueException("Such a student does not exist");
    } else {
      HashSet<QuizResponse> quizesResponses = new HashSet<QuizResponse>();
      Set<QuizResponse> allQuizResponses = tempStudent
          .getQuizResponse_Unmodifiable();
      if (allQuizResponses.size() == 0) {
        session.close();
        throw new EmptyValueException("Student never took a quiz in this class");
      }
      // Iterate through all the quiz respones and collect all the quizzes which
      // come from the same class
      // TODO find a better way to do this.
      for (QuizResponse instance : allQuizResponses) {
        if (instance.getClass_taken().getClass_uid() == class_uid) {
          quizesResponses.add(instance);
        }
      }
      JSONArray returnJSONArray = getJSONArrayOfResponses(quizesResponses);
      session.close();
      return returnJSONArray;
    }
    
  }
}
