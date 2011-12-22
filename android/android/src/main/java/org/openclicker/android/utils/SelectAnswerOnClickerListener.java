package org.openclicker.android.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openclicker.android.activity.WaitActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SelectAnswerOnClickerListener implements OnClickListener {
  
  private Bundle extras;
  
  public SelectAnswerOnClickerListener(Bundle extras) {
    
    this.extras = extras;
  }
  
  public void onClick(View v) {
    
    String studentUID = extras.getString("studentUID");
    String quizUID = extras.getString("quizUID");
    String classUID = extras.getString("classUID");
    String response = extras.getString("choiceKey");
    JSONObject foo = new JSONObject();
    try {
      foo.put("response", response);
      Utils.postData((Utils.SERVERADDRESS + "/student/" + studentUID
          + "/class/" + classUID + "/quiz/" + quizUID), foo);
    } catch (Exception e) {
      Toast.makeText(v.getContext(), "There was a problem parsing",
          Toast.LENGTH_LONG);
      return;
    }
    
    Intent intent = new Intent(v.getContext(), WaitActivity.class);
    intent.putExtras(extras);
    v.getContext().startActivity(intent);
    
  }
}
