package org.openclicker.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
  private String studentUID;
  private String classUID;
  private String quizUID;
  // private String requestURL = "genome.hunter.cuny.edu:9998/quiz/";
  private String jString;
  private static final String requestURL = "http://192.168.11.43:9998/quiz/";
  private static final String TAG = "MyActivity";
  
  private Vector<Integer> cUIDVec = new Vector<Integer>();
  private Vector<String> cDescriptionVec = new Vector<String>();
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Log a message (only on dev platform)
    Log.i(TAG, "Random Logger Message");
    
    setContentView(R.layout.main);
    
    Button submitButton = (Button) findViewById(R.id.submitButton1);
    submitButton.setOnClickListener(new View.OnClickListener() {
      // When student press on the submit button, the program get the
      // Information from the 3 editText field and send request to the
      // server to get the quiz (with the helper function)
      public void onClick(View v) {
        EditText studentText = (EditText) findViewById(R.id.editText1);
        EditText classText = (EditText) findViewById(R.id.editText2);
        EditText quizText = (EditText) findViewById(R.id.editText3);
        
        try {
          
          studentUID = studentText.getText().toString();
          classUID = classText.getText().toString();
          quizUID = quizText.getText().toString();
          
          if (studentUID.length() == 0 || classUID.length() == 0
              || quizUID.length() == 0) {
            Toast.makeText(LoginActivity.this, "Fill in the information",
                Toast.LENGTH_SHORT).show();
            return;
          }
          
          // Create the request URL
          String currentRequestURL = requestURL + quizUID + "/choices";
          try {
            // Get the JSON from the server
            jString = readData(currentRequestURL);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
          Toast.makeText(LoginActivity.this, jString, Toast.LENGTH_LONG).show();
          
          // Parse the JSON String, get the data and put them into the
          // corresponding vector, pass them along to the next activity
          try {
            jParse(jString);
          } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
        } catch (Exception e) {
          Toast
              .makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
              .show();
        }
      }
    });
  }
  
  // Connect to the server and read the JSON into a long string
  private String readData(String myRequestURL) throws Exception {
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
        }
      }
    }
  }
  
  // JSON String parser method
  private void jParse(String myJString) throws JSONException {
    JSONObject root = new JSONObject(myJString);
    
    JSONArray jUID;
    jUID = root.getJSONArray("choice_uid");
    
    JSONArray jDescription;
    jDescription = root.getJSONArray("description");
    
    for (int i = 0; i < jUID.length(); i++) {
      JSONObject foo = jUID.getJSONObject(i);
      cUIDVec.add(Integer.parseInt(foo.getString("choice_uid")));
      
      JSONObject bar = jDescription.getJSONObject(i);
      cDescriptionVec.add(bar.getString("desription"));
    }
  }
}