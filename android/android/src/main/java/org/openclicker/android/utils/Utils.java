package org.openclicker.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class Utils {
  //TODO - Throw an ServerException when the appropriate JSON is not returned.
  
  public final static String SERVERADDRESS = "http://192.168.11.43:9998/";
  
  // Connect to the server and read the JSON into a long string
  public static String getData(String myRequestURL) throws Exception {
    BufferedReader in = null;
    try {
      HttpClient client = new DefaultHttpClient();
      HttpGet request = new HttpGet();
      request.setURI(new URI(myRequestURL));
      HttpResponse response = client.execute(request);
      in = new BufferedReader(new InputStreamReader(response.getEntity()
          .getContent()));
      StringBuffer sb = new StringBuffer("");
      String line = "";
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      in.close();
      String page = sb.toString();
      return page;
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
          throw e;
        }
      }
    }
  }
  
  public static String postData(String myRequestURL, JSONObject json) {
    BufferedReader in = null;
    try {
      HttpClient client = new DefaultHttpClient();
      
      HttpPost post = new HttpPost();
      post.setURI(new URI(myRequestURL));
      post.setHeader("Content-type", "application/json");
      post.setEntity(new StringEntity(json.toString()));
      
      HttpResponse response = client.execute(post);
      
      in = new BufferedReader(new InputStreamReader(response.getEntity()
          .getContent()));
      StringBuffer sb = new StringBuffer("");
      String line = "";
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
      
      in.close();
      return sb.toString();
      
    }catch(URISyntaxException e){
      throw new RuntimeException("URL Incorrect",e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Unsupported Encoding",e);
    } catch (ClientProtocolException e) {
      throw new RuntimeException("Client Protocol Exception",e);
    } catch (IOException e) {
      throw new RuntimeException("IOException",e);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException("IOException",e);
        }
      }
    }
  }
  
}
