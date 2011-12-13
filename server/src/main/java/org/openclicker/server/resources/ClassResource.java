package org.openclicker.server.resources;

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
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.serverExceptions.WebNotFoundException;
import org.openclicker.server.util.serverExceptions.WebBadRequestException;
import org.openclicker.server.util.serverExceptions.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/class")
public class ClassResource {
  private static volatile Class temp;
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @GET
  @Path("/{class_uid_text}")
  @Produces("application/json")
  public String getClass(@PathParam("class_uid_text") String class_uid_text) {
    try {
      Integer class_uid = Integer.parseInt((String) class_uid_text);
      temp = fetchClass(class_uid);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
    return toJSON(temp).toString();
  }
  
  @GET
  @Path("/{class_uid_text}/students")
  @Produces("application/json")
  public String getAllStudents(
      @PathParam("class_uid_text") String class_uid_text) {
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      return getJSONArrayOfAllStudents(class_uid).toString();
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  @GET
  @Path("/{class_uid_text}/quiz")
  @Produces("application/json")
  public String getAllQuizes(@PathParam("class_uid_text") String class_uid_text) {
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      return getJSONArrayOfAllQuizzes(class_uid).toString();
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
  public Response addClass(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int id = addNewClass(json);
      return Response.status(Status.ACCEPTED).entity(
          "New Class " + id + " added\n").type(MediaType.TEXT_PLAIN).build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (JSONException e) {
      throw new WebBadRequestException(e);
    }
  }
  
  @POST
  @Path("/{class_uid_text}/students")
  @Consumes("application/json")
  public Response addStudentToClass(
      @PathParam("class_uid_text") String class_uid_text, String context) {
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      int student_uid = json.getInt("student");
      addNewStudentToClass(class_uid, student_uid);
      return Response.status(Status.ACCEPTED).entity(
          "New Student " + student_uid + " added to class " + class_uid_text
              + "\n").type(MediaType.TEXT_PLAIN).build();
      
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebNotFoundException(e);
    } catch (JSONException e) {
      logger.warn(e.getMessage());
      throw new WebBadRequestException(e);
    }
  }
  
  private JSONArray getJSONArrayOfAllQuizzes(int class_uid)
      throws EmptyValueException {
    
    Class tempClass;
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    tempClass = (Class) session.get(Class.class, class_uid);
    
    if (tempClass == null) {
      throw new EmptyValueException("class_uid " + class_uid
          + " is not available");
    }
    JSONArray foo = QuizResource.toJSON(tempClass.getQuizzes_Unmodifiable());
    session.close();
    
    return (foo);
  }
  
  private JSONArray getJSONArrayOfAllStudents(int class_uid)
      throws EmptyValueException {
    
    Class tempClass;
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    tempClass = (Class) session.get(Class.class, class_uid);
    
    if (tempClass == null) {
      throw new EmptyValueException("class_uid " + class_uid
          + " is not available");
    }
    JSONArray foo = StudentResource
        .toJSON(tempClass.getStudents_Unmodifiable());
    session.close();
    
    return (foo);
  }
  
  private int addNewClass(JSONObject json) {
    int id;
    
    String class_name = json.getString("class_name");
    
    // Begin Hibernate Session
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      // Add a class and commit
      Class testClass = new Class(class_name);
      session.save(testClass);
      
      id = testClass.getClass_uid();
      session.getTransaction().commit();
      
      logger.info("Class successfully added: " + id);
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebServerException(e);
    }
    
  }
  
  private void addNewStudentToClass(int class_uid, int student_uid)
      throws EmptyValueException {
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Class tempClass = (Class) session.get(Class.class, class_uid);
    Student tempStudent = (Student) session.get(Student.class, student_uid);
    
    if (tempClass == null) {
      throw new EmptyValueException("Class " + class_uid + " was not found.");
    } else if (tempStudent == null) {
      throw new EmptyValueException("Student " + student_uid
          + " was not found.");
    }
    
    logger.info("Student {}  was added to class {}",student_uid,class_uid);
    
    // If the class does not contain the student add them
    if (!tempClass.getStudents_Unmodifiable().contains(tempStudent)) {
      tempClass.addStudent(tempStudent);
      session.save(tempClass);
      session.save(tempStudent);
    }
    session.getTransaction().commit();
    
  }
  
  public static JSONObject toJSON(Class uniqueClass) {
    JSONObject json = new JSONObject();
    json.put("class_uid", uniqueClass.getClass_uid());
    json.put("name", uniqueClass.getClass_name());
    return json;
  }
  
  private Class fetchClass(int class_uid) throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Class tempClass = (Class) session.get(Class.class, class_uid);
    session.close();
    if (tempClass != null) {
      return tempClass;
    } else {
      throw new EmptyValueException("class_uid " + class_uid
          + " is not available");
    }
  }
}
