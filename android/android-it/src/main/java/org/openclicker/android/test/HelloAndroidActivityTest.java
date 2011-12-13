package org.openclicker.android.test;

import org.openclicker.android.activity.LoginActivity;

import android.test.ActivityInstrumentationTestCase2;

public class HelloAndroidActivityTest extends
    ActivityInstrumentationTestCase2<LoginActivity> {
  
  public HelloAndroidActivityTest() {
    super("org.openclicker.android", LoginActivity.class);
  }
  
  public void testActivity() {
    LoginActivity activity = getActivity();
    assertNotNull(activity);
  }
}
