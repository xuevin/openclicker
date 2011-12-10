package org.openclicker.android.activity;

import java.io.BufferedReader;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openclicker.android.Graph;
import org.openclicker.android.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;

public class ClickerGraphActivity extends Activity {
  private String dataString = null;
  private Vector<String> choiceVec = new Vector<String>();
  private Vector<String> answerVec = new Vector<String>();
  private Vector<Integer> countVec = new Vector<Integer>();
  private final static String TAG = "ClickerGraphActivity";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      dataString = Utils
          .getData("http://192.168.11.43:9998/class/3/quiz/3/stats");
      // Toast.makeText(ClickerGraphActivity.this, dataString,
      // Toast.LENGTH_LONG).show();
      jParse(dataString);
    } catch (Exception e) {
      Toast.makeText(ClickerGraphActivity.this, "jParse problem",
          Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
    
    // Get the dimension of the screen at the landscape
    Display display = getWindowManager().getDefaultDisplay();
    int screenX = display.getWidth();
    int screenY = display.getHeight();
    int numOfChoice = choiceVec.size();
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(new Graph(this, screenX, screenY, choiceVec, countVec,
        answerVec, numOfChoice));
  }
  
  // JSON String parser method
  private void jParse(String myJString) throws JSONException {
    // JSONObjects and arrays used by the jParse method
    JSONObject root = new JSONObject(myJString);
    JSONArray choices;
    JSONArray answers;
    
    // Parsing the choices into the choices label and counts
    choices = root.getJSONArray("choices");
    for (int i = 0; i < choices.length(); i++) {
      JSONObject foo = choices.getJSONObject(i);
      choiceVec.add(foo.getString("description"));
      countVec.add(Integer.parseInt(foo.getString("count")));
    }
    
    // Parsing the answers into the answer vector
    answers = root.getJSONArray("answers");
    for (int j = 0; j < answers.length(); j++) {
      answerVec.add(j, answers.getString(j));
    }
    
  }
}