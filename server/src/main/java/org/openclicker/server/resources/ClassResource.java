package org.openclicker.server.resources;

import java.util.Collection;

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
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
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
    Integer class_uid = Integer.parseInt((String) class_uid_text);
    try {
      temp = fetchClass(class_uid);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    return toJSON(temp).toString();
  }
  
  @GET
  @Path("/{class_uid_text}/students")
  @Produces("application/json")
  public String getStudents(@PathParam("class_uid_text") String class_uid_text) {
    try {
      Integer class_uid = Integer.parseInt(class_uid_text);
      return getJSON(class_uid).toString();
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
  public Response addClass(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      addNewClass(json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
  
  private JSONObject getJSON(int class_uid) throws EmptyValueException {
    
    Class tempClass;
    
    JSONObject object = null;
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    tempClass = (Class) session.get(Class.class, class_uid);
    
    if (tempClass == null) {
      throw new EmptyValueException("class_uid " + class_uid
          + " is not available");
    }
    tempClass.getStudents_Unmodifiable();
    object = toJSON(tempClass.getStudents_Unmodifiable());
    
    session.close();
    return object;
    
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
      
      logger.info("Class successfully added");
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
    
  }
  
  private static JSONObject toJSON(Collection<Student> studentsUnmodifiable) {
    JSONObject object = new JSONObject();
    JSONArray array = new JSONArray();
    
    for (Student student : studentsUnmodifiable) {
      array.add(student.getStudent_uid());
    }
    object.put("student_uids", array);
    return object;
  }

  private static JSONObject toJSON(Class uniqueClass) {
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
