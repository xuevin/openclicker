package org.openclicker.server.resources;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.JSONUtils;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CollectionClassStudentResource extends ServerResource{
  
  @Get
  public String retrieve() {
    Integer class_uid = Integer.parseInt((String) getRequestAttributes().get(
        "class_uid"));
    try {
      return getJSON(class_uid).toString();
      
    } catch (EmptyValueException e) {
      return JSONUtils.createNewError(e.getMessage()).toString();
    }
  }
  
  private JSONObject toJSON(Collection<Student> studentsUnmodifiable) {
    JSONObject object = new JSONObject();
    JSONArray array = new JSONArray();

    for(Student student:studentsUnmodifiable){
      array.add(student.getStudent_uid());
    }
    object.put("student_uids", array);
    return object;
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
    System.out.println("this far");
    object = toJSON(tempClass.getStudents_Unmodifiable());
    
    session.close();
    return object;
    
  }

}
