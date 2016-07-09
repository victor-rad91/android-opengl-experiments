package com.iquestgroup.gltest;

import android.app.Activity;
import android.os.Bundle;
import com.iquestgroup.gltest.gl.ExampleGLSurfaceView;

public class MainActivity extends Activity {

  private ExampleGLSurfaceView glView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    glView = (ExampleGLSurfaceView) findViewById(R.id.glSurfaceView);
  }

  @Override
  protected void onResume() {
    super.onResume();
    glView.startTimer();
  }

  @Override
  protected void onPause() {
    glView.stopTimer();
    super.onPause();
  }
}
