package org.openclicker.server.resources.collections;

import java.io.IOException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.HibernateUtil;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionStudent extends ServerResource {
  
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Get
  public String retrieve() {
    // For now, there is no getting allowed.
    throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    // try {
    // Integer class_uid = Integer.parseInt((String) getRequestAttributes().get(
    // "class_uid"));
    // return getJSON(class_uid).toString();
    // } catch (EmptyValueException e) {
    // throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    // } catch (NumberFormatException e) {
    // throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    // }
  }
  
  @Post
  public Representation acceptJsonRepresentation(Representation entity) {
    
    try {
      if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
        
        JSONObject json = (JSONObject) JSONSerializer.toJSON(entity.getText());
        
        int student_uid = addNewStudent(json);
        
        setStatus(Status.SUCCESS_CREATED);
        
        Representation rep = new StringRepresentation("Student created: "
            + student_uid, MediaType.TEXT_PLAIN);
        return rep;
      } else {
        throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
      }
    } catch (JSONException e) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    } catch (IOException e) {
      logger.warn(e.getMessage());
      throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
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
      throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
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
      throw new ResourceException(Status.CLIENT_ERROR_CONFLICT);
    }
  }
  // private JSONObject toJSON(Collection<Student> studentsUnmodifiable) {
  // JSONObject object = new JSONObject();
  // JSONArray array = new JSONArray();
  //    
  // for (Student student : studentsUnmodifiable) {
  // array.add(student.getStudent_uid());
  // }
  // object.put("student_uids", array);
  // return object;
  // }
  
  // private JSONObject getJSON(int class_uid) throws EmptyValueException {
  //    
  // Class tempClass;
  //    
  // JSONObject object = null;
  //    
  // Session session = HibernateUtil.getSessionFactory().getCurrentSession();
  // session.beginTransaction();
  // tempClass = (Class) session.get(Class.class, class_uid);
  //    
  // if (tempClass == null) {
  // throw new EmptyValueException("class_uid " + class_uid
  // + " is not available");
  // }
  // tempClass.getStudents_Unmodifiable();
  // object = toJSON(tempClass.getStudents_Unmodifiable());
  //    
  // session.close();
  // return object;
  //    
  // }
  
}
