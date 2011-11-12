package org.openclicker.server.resources;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class StudentResource extends ServerResource {
  
  @Get
  public String retrieve() {
    try {
      Integer student_uid = Integer.parseInt((String) getRequestAttributes()
          .get("student_uid"));
      
      return toJSON(fetchStudent(student_uid)).toString();
      
    } catch (NumberFormatException e) {
      // The number did not parse correctly
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    } catch (EmptyValueException e) {
      // There is no student by that unique ID
      throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
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
