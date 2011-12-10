package org.openclicker.android.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.openclicker.android.R;
import org.openclicker.android.R.id;
import org.openclicker.android.R.layout;
import org.openclicker.android.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WaitActivity extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wait);
    addFunctionToButtons();
    
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      TextView selectedChoice = (TextView) findViewById(R.id.selectedChoice);
      selectedChoice.setText(extras.getString("choiceDescription"));
      
      // (extras.getString("selectedKey"));
      
      // private int key;
      // private String description;
      // String TAG = "CustomClickListener";
      // private String studentUID;
      // private String classUID;
      // private String quizUID;
      //      
      // this.key = extras.getInt("key");
      // this.description = extras.getString("description");
      // this.studentUID = extras.getString("classUID");
      // this.quizUID = extras.getString("quizUID");
      // this.classUID = extras.getString("studentUID");
      
    }
    
  }
  
  private void addFunctionToButtons() {
    
    Button changeAnswer = (Button) findViewById(R.id.changeAnswer);
    changeAnswer.setOnClickListener(new OnClickListener() {
      
      public void onClick(View v) {
        finish();
      }
    });
    
    Button submitAnswer = (Button) findViewById(R.id.getGrade);
    submitAnswer.setOnClickListener(new OnClickListener() {
      
      public void onClick(View v) {
        JSONObject foo = new JSONObject();
        try {
          foo.put("class_name", "Android");
//          String response = Utils.postData(Utils.SERVERADDRESS + "class", foo);
          Intent intent = new Intent(getApplicationContext(),
              ClickerGraphActivity.class);
          startActivity(intent);
//          Toast.makeText(WaitActivity.this, response, Toast.LENGTH_LONG).show();
          
        } catch (RuntimeException e) {
          Toast.makeText(WaitActivity.this, "Failed " + e.getMessage(),
              Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
          Toast.makeText(WaitActivity.this, "Failed " + e.getMessage(),
              Toast.LENGTH_LONG).show();
        }
      }
    });
    
  }
  
}
