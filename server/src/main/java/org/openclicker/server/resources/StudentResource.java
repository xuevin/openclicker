package org.openclicker.server.resources;

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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/student")
public class StudentResource{
  
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
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  @POST
  @Consumes("application/json")
  public Response addStudent(String context) {
    
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      addNewStudent(json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    
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
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
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
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
  
  private static JSONObject toJSON(Student student) {
    
    JSONObject object = new JSONObject();
    object.put("student_uid", student.getStudent_uid());
    object.put("gender", student.getGender());
    object.put("email", student.getEmail_address());
    object.put("first_name", student.getFirst_name());
    object.put("last_name", student.getLast_name());
    return object;
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
}
