package org.openclicker.server.resources;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.Student;
import org.openclicker.server.domain.Student.Gender;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.JSONUtils;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class StudentResource extends ServerResource {
  private static volatile Student temp = new Student("Foo", "Bar", Gender.M,
      "FooBar@Foo.org");
  
  @Get
  public String retrieve() {
    Integer student_uid = Integer.parseInt((String) getRequestAttributes().get(
        "student_uid"));
    try {
      temp = fetchStudent(student_uid);
    } catch (EmptyValueException e) {
      return JSONUtils.createNewError(e.getMessage()).toString();
    }
    
    return toJSON(temp).toString();
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
