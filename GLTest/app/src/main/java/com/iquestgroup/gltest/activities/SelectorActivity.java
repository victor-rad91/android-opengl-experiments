package com.iquestgroup.gltest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.iquestgroup.gltest.R;

/**
 * @author victor.rad on 09/07/16.
 */

public class SelectorActivity extends Activity implements View.OnClickListener {

  private Button triangleTrailButton;
  private Button particleButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selector);
    triangleTrailButton = (Button) findViewById(R.id.triangle_trail_button);
    triangleTrailButton.setOnClickListener(this);
    particleButton = (Button) findViewById(R.id.simple_particle_button);
    particleButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.equals(triangleTrailButton)) {
      startTriangleTrailActivity();
    }
    else if (view.equals(particleButton)) {
      startParticleActivity();
    }
  }

  private void startTriangleTrailActivity() {
    Intent intent = new Intent(this, TriangleTrailActivity.class);
    startActivity(intent);
  }

  private void startParticleActivity() {
    Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
  }
}
