package org.openclicker.server.resources;

import java.io.IOException;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.Session;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Student;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.JSONUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class CollectionStudentResource extends ServerResource {
  
  @Get
  public String retrieve() throws ResourceException {
    
    Integer class_uid = Integer.parseInt((String) getRequestAttributes().get(
        "class_uid"));
    try {
      return getJSON(class_uid).toString();
    } catch (EmptyValueException e) {
      setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
      return JSONUtils.createNewError(e.getMessage()).toString();
    }
  }
  
  @Post
  public void acceptJsonRepresentation(Representation entity)
      throws ResourceException {
    System.out.println("HELLO WORLD");
    
    JSONObject json = null;
    
    try {
      if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
        json = (JSONObject) JSONSerializer.toJSON(entity.getText());
        System.out.println(json.toString());
      }else{
        System.out.println("FAIL");
      }

      // business logic and persistence
      
    } catch (JSONException e) {
      setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
      return;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  private JSONObject toJSON(Collection<Student> studentsUnmodifiable) {
    JSONObject object = new JSONObject();
    JSONArray array = new JSONArray();
    
    for (Student student : studentsUnmodifiable) {
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
