package com.iquestgroup.gltest.activities;

import android.app.Activity;
import android.os.Bundle;
import com.iquestgroup.gltest.R;
import com.iquestgroup.gltest.gl.TriangleTrailGLSurfaceView;

public class TriangleTrailActivity extends Activity {

  private TriangleTrailGLSurfaceView glView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_triangle_trail);
    glView = (TriangleTrailGLSurfaceView) findViewById(R.id.glSurfaceView);
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
