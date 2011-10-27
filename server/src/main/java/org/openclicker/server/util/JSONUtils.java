package org.openclicker.server.util;

import net.sf.json.JSONObject;

public class JSONUtils {
  public static JSONObject createNewError(String message) {
    JSONObject temp = new JSONObject();
    temp.put("error", message);
    return temp;
  }
  
}
