package org.openclicker.server.resources;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.Class;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.JSONUtils;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class ClassResource extends ServerResource {
  private static volatile Class temp;
  
  @Get
  public String retrieve() {
    Integer class_uid = Integer.parseInt((String) getRequestAttributes().get(
        "class_uid"));
    try {
      temp = fetchClass(class_uid);
    } catch (EmptyValueException e) {
      return JSONUtils.createNewError(e.getMessage()).toString();
    }
    
    return toJSON(temp).toString();
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
