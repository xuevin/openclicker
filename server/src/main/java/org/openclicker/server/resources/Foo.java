package org.openclicker.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.sf.json.JSONObject;

@Path("/foo")
public class Foo {
  @GET
  @Produces("application/json")
  public String getChoice(){
    JSONObject json = new JSONObject();
    json.put("quiz", "How much caffine does pepsi have?");
    json.put("answer", "52mg");
    json.put("choice1", "23mg");
    json.put("choice2", "46mg");
    json.put("choice3", "0mg");
    return json.toString();
  }
  

}
