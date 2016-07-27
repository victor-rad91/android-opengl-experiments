package com.iquestgroup.gltest.activities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.iquestgroup.gltest.R;
import com.iquestgroup.gltest.gl.view.RotatableGLSurfaceView;

/**
 * Object at (0,0,0), camera rotates around a sphere of diameter 1 to view it.
 *
 * @author victor.rad on 10/07/16.
 */

public class CameraRotationActivity extends Activity implements SensorEventListener {

  private SensorManager sensorManager;
  private Sensor rotationSensor;
  private Sensor accelerometer;

  private RotatableGLSurfaceView view;

  private long lastAccelerometerTimestamp = -1;

  private float[] orientation = new float[3];
  private float[] rotationVector = new float[3];
  private float[] rotation = new float[16];
  private float[] remappedRotation = new float[16];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rotatable);
    view = (RotatableGLSurfaceView) findViewById(R.id.glSurfaceView);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  protected void onPause() {
    sensorManager.unregisterListener(this);
    super.onPause();
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    if (sensorEvent.sensor == rotationSensor) {
      rotationVector[0] = sensorEvent.values[0];
      rotationVector[1] = sensorEvent.values[1];
      rotationVector[2] = sensorEvent.values[2];
      SensorManager.getRotationMatrixFromVector(rotation, rotationVector);
      SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotation);
      SensorManager.getOrientation(remappedRotation, orientation);
      view.onRotationChange(rotationVector[0], rotationVector[1], rotationVector[2]);
    }
    if (sensorEvent.sensor == accelerometer) {
      float xAcceleration = sensorEvent.values[0];
      float yAcceleration = sensorEvent.values[1];
      float zAcceleration = sensorEvent.values[2];
      if (lastAccelerometerTimestamp == -1) {
        lastAccelerometerTimestamp = System.currentTimeMillis();
      }
      else {
        long millis = System.currentTimeMillis() - lastAccelerometerTimestamp;
        lastAccelerometerTimestamp = System.currentTimeMillis();
        view.onTranslationChange(xAcceleration, yAcceleration, zAcceleration, millis);
      }
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }
}
