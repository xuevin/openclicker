package org.openclicker.server.resources;

import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class AvailableChoiceResource extends ServerResource {
  private static volatile AvailableChoice temp;
  
  @Get
  public String retrieve() {
    
    try {
      Integer choice_uid = Integer.parseInt((String) getRequestAttributes()
          .get("choice_uid"));
      temp = fetchAvailableChoice(choice_uid);
    } catch (EmptyValueException e) {
      throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    } catch (NumberFormatException e) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    }
    return toJSON(temp).toString();
  }
  
  private static JSONObject toJSON(AvailableChoice choice) {
    JSONObject json = new JSONObject();
    json.put("choice_uid", choice.getChoice_uid());
    json.put("description", choice.getDescription());
    return json;
  }
  
  private AvailableChoice fetchAvailableChoice(int choice_uid)
      throws EmptyValueException {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    AvailableChoice tempChoice = (AvailableChoice) session.get(
        AvailableChoice.class, choice_uid);
    session.close();
    if (tempChoice != null) {
      return tempChoice;
    } else {
      throw new EmptyValueException("choice_uid " + choice_uid
          + " is not available");
    }
  }
}
