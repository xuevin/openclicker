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
import org.openclicker.server.domain.AvailableChoice;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("availablechoice")
public class AvailableChoiceResource {
  private static volatile AvailableChoice temp;
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @GET
  @Path("/{choice_text}")
  @Produces("application/json")
  public String getChoice(@PathParam("choice_text") String choice_text) {
    
    try {
      Integer choice_uid = Integer.parseInt(choice_text);
      temp = fetchAvailableChoice(choice_uid);
    } catch (EmptyValueException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    } catch (NumberFormatException e)  {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    return toJSON(temp).toString();
  }
  
  @POST
  @Consumes("application/json")
  public Response addChoice(String context) {
    try {
      JSONObject json = (JSONObject) JSONSerializer.toJSON(context);
      addNewChoice(json);
      return Response.ok().entity("").build();
    } catch (NumberFormatException e) {
      logger.warn(e.getMessage());
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
    
  }
  
  private int addNewChoice(JSONObject json) {
    int id;
    
    String description = json.getString("description");
    
    // Begin Hibernate Session
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      // Add a Choice and commit
      AvailableChoice choice = new AvailableChoice(description);
      session.save(choice);
      
      id = choice.getChoice_uid();
      session.getTransaction().commit();
      
      logger.info("Choice successfully added");
      return id;
    } catch (HibernateException e) {
      session.close();
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
  
  public static JSONObject toJSON(AvailableChoice choice) {
    JSONObject json = new JSONObject();
    json.put("choice_uid", choice.getChoice_uid());
    json.put("description", choice.getDescription());
    return json;
  }
  public static JSONArray toJSON(Collection<AvailableChoice> choices) {
    JSONArray arrayOfJSON = new JSONArray();
    for(AvailableChoice choice:choices){
      arrayOfJSON.add(toJSON(choice));
    }
    return arrayOfJSON;
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
