package org.openclicker.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
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
import org.openclicker.android.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
  private String studentUID;
  private String classUID;
  private String quizUID;
  private String requestURL = "http://genome.hunter.cuny.edu:9998/quiz/";
  // private static final String requestURL = "http://192.168.11.43:9998/quiz/";
  private static final String TAG = "MyActivity";
  
  private Vector<Integer> cUIDVec = new Vector<Integer>();
  private Vector<String> cDescriptionVec = new Vector<String>();
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Log a message (only on dev platform)
    // Log.i(TAG, "Random Logger Message");
    
    setContentView(R.layout.main);
//    Log.i(TAG, "Random Logger Message");
    
    Button submitButton = (Button) findViewById(R.id.submitButton1);
    submitButton.setOnClickListener(new View.OnClickListener() {
      private String quizJSONString;
      private String choiceJSONString;
      
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
          
          ////////////////////////////////////////////////////
          // Query Server
          ////////////////////////////////////////////////////
          
          try {
            String quizURL = requestURL + quizUID;
            quizJSONString = readData(quizURL);
            Log.i(TAG, quizJSONString);

            String choiceURL = requestURL + quizUID + "/choices";
            choiceJSONString = readData(choiceURL);
            Log.i(TAG, choiceJSONString);
          } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            return;
          }
          
          Toast.makeText(LoginActivity.this, choiceJSONString,
              Toast.LENGTH_LONG).show();
          
          // Fetch Quiz
          // ////////////////////////////////////////////////////////////////////////////
          
          String question = new JSONObject(quizJSONString).getString("question");
          HashMap<Integer,String> choices = jParseChoices(choiceJSONString);
          
          setContentView(R.layout.quiz);
          
          LinearLayout foo = (LinearLayout) findViewById(R.id.linearLayout1);
          ((TextView)findViewById(R.id.questionPlaceHolder)).setText(question);
          for (Integer key : choices.keySet()) {
            Button temp = new Button(getContext());
            
            temp.setOnClickListener(new CustomOnClickerListener(getContext(), key, choices.get(key)));
            temp.setText(choices.get(key));
            foo.addView(temp);
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
  
  // JSON String parser method
  private HashMap<Integer,String> jParseChoices(String myJString) throws JSONException {
    JSONArray myArray = new JSONArray(myJString);
    HashMap<Integer,String> choices = new HashMap<Integer,String>();
    for (int i = 0; i < myArray.length(); i++) {
      String description = myArray.getJSONObject(i).getString("description");
      int uid = myArray.getJSONObject(i).getInt("choice_uid");
      choices.put(uid, description);
    }
    return choices;
  }
  private LoginActivity getContext() {
    return this;
    
  }
}