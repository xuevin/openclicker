package org.openclicker.android;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CustomOnClickerListener implements OnClickListener {
  private int key;
  private String description;
  LoginActivity context;
  
  public CustomOnClickerListener(LoginActivity context, int key, String description) {
    this.key=key;
    this.description=description;
    this.context=context;
  }
  
  public void onClick(View v) {
    context.setContentView(R.layout.wait);
    ((TextView)context.findViewById(R.id.selectedChoice)).setText(description);
  
  }
  
}
