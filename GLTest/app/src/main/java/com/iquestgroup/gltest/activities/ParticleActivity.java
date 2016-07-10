package com.iquestgroup.gltest.activities;

import android.app.Activity;
import android.os.Bundle;
import com.iquestgroup.gltest.R;
import com.iquestgroup.gltest.gl.view.ParticleGLSurfaceView;

/**
 * Created by victor.rad on 10/07/16.
 */

public class ParticleActivity extends Activity {

  private ParticleGLSurfaceView view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_particle);
    view = (ParticleGLSurfaceView) findViewById(R.id.glSurfaceView);
  }

  @Override
  protected void onResume() {
    super.onResume();
    view.start();
  }

  @Override
  protected void onPause() {
    view.stop();
    super.onPause();
  }
}
