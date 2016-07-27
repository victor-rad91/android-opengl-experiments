package com.iquestgroup.gltest.gl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.iquestgroup.gltest.gl.renderer.CameraRotationRenderer;


public class RotatableGLSurfaceView extends GLSurfaceView {

  private CameraRotationRenderer renderer;
  private float xTranslation = 0;
  private float xVelocity = 0;
  private float yTranslation = 0;
  private float yVelocity = 0;
  private float zTranslation = 0;
  private float zVelocity = 0;

  public RotatableGLSurfaceView(Context context) {
    super(context);
    init();
  }

  public RotatableGLSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setEGLContextClientVersion(3);
    renderer = new CameraRotationRenderer();
    setRenderer(renderer);
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  public void onRotationChange(float azimuth, float pitch, float roll) {
    float xPos = (float) (Math.sin(-azimuth) * Math.cos(-pitch));
    float yPos = (float) (Math.sin(-azimuth) * Math.sin(-pitch));
    float zPos = (float) (Math.cos(-azimuth));
    float xUp = (float) Math.cos(-roll);
    float yUp = (float) Math.sin(-roll);
    renderer.setCameraPosition(xPos + xTranslation, yPos + yTranslation, zPos + zTranslation,
        xUp, yUp, 0, xTranslation, yTranslation, zTranslation);
  }

  public void onTranslationChange(float xAcceleration, float yAcceleration, float zAcceleration, long millisSinceLast) {
    Log.d("RotatableGLSurfaceView", String.format("acceleration: (x=%.4f,y=%.4f,z=%.4f) for %d millis", xAcceleration,
        yAcceleration, zAcceleration, millisSinceLast));
    xVelocity += xAcceleration;
    yVelocity += yAcceleration;
    zVelocity += zAcceleration;
    float scaling = (float) millisSinceLast / 1000.0f;
    Log.d("RotatableGLSurfaceView", String.format("xVelocity=%.4f,yVelocity=%.4f,zVelocity=%.4f, scaling=%.3f",
        xVelocity, yVelocity, zVelocity, scaling));
    xTranslation += (scaling * xVelocity);
    yTranslation += (scaling * yVelocity);
    zTranslation += (scaling * zVelocity);
    Log.d("RotatableGLSurfaceView", String.format("xTranslation=%.4f, yTranslation=%.4f, zTranslation=%.4f",
        xTranslation, yTranslation, zTranslation));
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    return super.onTouchEvent(event);
  }
}
