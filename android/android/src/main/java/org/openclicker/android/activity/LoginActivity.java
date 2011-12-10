package org.openclicker.android.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openclicker.android.R;
import org.openclicker.android.R.id;
import org.openclicker.android.R.layout;
import org.openclicker.android.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
  // private static final String REQUEST_URL =
  // "http://genome.hunter.cuny.edu:9998/";
  private static final String TAG = "LoginActivity";
  private String quizJSONString;
  private String choiceJSONString;
  private String studentUID;
  private String classUID;
  private String quizUID;
  
  // private Vector<String> cDescriptionVec = new Vector<String>();
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    Button submitButton = (Button) findViewById(R.id.submitButton1);
    
    submitButton.setOnClickListener(new View.OnClickListener() {
      
      // When student press on the submit button, the program get the
      // Information from the 3 editText field and send request to the
      // server to get the quiz (with the helper function)
      public void onClick(View v) {
        
        try {
          queryServer();
          Intent intent = new Intent(getApplicationContext(),
              QuizActivity.class);
          intent.putExtra("quizJSON", quizJSONString);
          intent.putExtra("choicesJSON", choiceJSONString);
          intent.putExtra("classUID", classUID);
          intent.putExtra("studentUID", studentUID);
          intent.putExtra("quizUID", quizUID);
          startActivity(intent);
          // loadQuizActivity();
          
        } catch (Exception e) {
          Toast.makeText(LoginActivity.this, "Error" + e.getMessage(),
              Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
  
  protected void queryServer() throws Exception {
    
    EditText studentText = (EditText) findViewById(R.id.editText1);
    EditText classText = (EditText) findViewById(R.id.editText2);
    EditText quizText = (EditText) findViewById(R.id.editText3);
    
    studentUID = studentText.getText().toString();
    classUID = classText.getText().toString();
    quizUID = quizText.getText().toString();
    
    if (studentUID.length() == 0 || classUID.length() == 0
        || quizUID.length() == 0) {
      Toast.makeText(LoginActivity.this, "Fill in the information",
          Toast.LENGTH_SHORT).show();
      return;
    }
    
    try {
      String quizURL = Utils.SERVERADDRESS + "quiz/" + quizUID;
      quizJSONString = Utils.getData(quizURL);
      Log.i(TAG, quizJSONString);
      
      String choiceURL = Utils.SERVERADDRESS + "quiz/" + quizUID + "/choices";
      choiceJSONString = Utils.getData(choiceURL);
      Log.i(TAG, choiceJSONString);
      
      // Toast.makeText(LoginActivity.this, choiceJSONString, Toast.LENGTH_LONG)
      // .show();
    } catch (Exception e) {
      Log.i(TAG, "Network Problems");
      Toast.makeText(LoginActivity.this, "Network Problems....",
          Toast.LENGTH_LONG).show();
    }
    
  }
  
  // // JSON String parser method
  // private void jParse(String myJString) throws JSONException {
  // JSONObject root = new JSONObject(myJString);
  //    
  // JSONArray jUID;
  // jUID = root.getJSONArray("choice_uid");
  //    
  // JSONArray jDescription;
  // jDescription = root.getJSONArray("description");
  //    
  // for (int i = 0; i < jUID.length(); i++) {
  // JSONObject foo = jUID.getJSONObject(i);
  // cUIDVec.add(Integer.parseInt(foo.getString("choice_uid")));
  //      
  // JSONObject bar = jDescription.getJSONObject(i);
  // cDescriptionVec.add(bar.getString("desription"));
  // }
  // }
  
  // JSON String parser method
  
}