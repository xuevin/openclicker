package org.openclicker.android.utils;

import org.openclicker.android.activity.WaitActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectAnswerOnClickerListener implements OnClickListener {
  
  private Bundle extras;
  
  public SelectAnswerOnClickerListener(Bundle extras) {
    
    this.extras = extras;
  }
  
  public void onClick(View v) {
    Intent intent = new Intent(v.getContext(), WaitActivity.class);
    intent.putExtras(extras);
    v.getContext().startActivity(intent);
    
  }
}
