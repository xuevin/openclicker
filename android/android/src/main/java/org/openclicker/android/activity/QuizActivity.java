package org.openclicker.android.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openclicker.android.R;
import org.openclicker.android.R.id;
import org.openclicker.android.R.layout;
import org.openclicker.android.utils.SelectAnswerOnClickerListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
  private String TAG = "QuizActivity";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.quiz);
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      loadQuizChoices(extras);
      
    }
    
  }
  
  protected void loadQuizChoices(Bundle extras) {
    try {
      String quizJSON = extras.getString("quizJSON");
      String question = new JSONObject(quizJSON).getString("question");
      
      String choiceJSONString = extras.getString("choicesJSON");
      HashMap<Integer,String> choices = jParseChoices(choiceJSONString);
      
      LinearLayout quizLinearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
      ((TextView) findViewById(R.id.questionPlaceHolder)).setText(question);
      for (Integer key : choices.keySet()) {
        Button temp = new Button(this);
        Bundle extras2 = new Bundle(extras);
        extras2.putString("choiceKey", key.toString());
        extras2.putString("choiceDescription", choices.get(key));
        temp.setOnClickListener(new SelectAnswerOnClickerListener(extras2));
        temp.setText(choices.get(key));
        quizLinearLayout.addView(temp);
      }
    } catch (JSONException e) {
      Toast.makeText(this, "There was a problem parsing", Toast.LENGTH_LONG);
    }
    
  }
  
  private HashMap<Integer,String> jParseChoices(String myJString)
      throws JSONException {
    JSONArray myArray = new JSONArray(myJString);
    HashMap<Integer,String> choices = new HashMap<Integer,String>();
    for (int i = 0; i < myArray.length(); i++) {
      String description = myArray.getJSONObject(i).getString("description");
      int uid = myArray.getJSONObject(i).getInt("choice_uid");
      choices.put(uid, description);
    }
    return choices;
  }
  
}
