package com.iquestgroup.gltest.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.iquestgroup.gltest.engine.SimpleColorEngine;


public class TriangleTrailGLSurfaceView extends GLSurfaceView implements SimpleColorEngine.EngineUpdateListener {

  private float previousX;
  private float previousY;

  private SimpleColorEngine engine;

  public TriangleTrailGLSurfaceView(Context context) {
    super(context);
    init();
  }

  public TriangleTrailGLSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setEGLContextClientVersion(3);
    engine = new SimpleColorEngine();
    setRenderer(new SimpleColorEngineRenderer(engine));
    engine.setListener(this);
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        previousX = event.getX();
        previousY = event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        float deltaX = event.getX() - previousX;
        float deltaY = event.getY() - previousY;
        engine.updateR(deltaX);
        engine.updateG(deltaY);
        engine.putPointInQueue(getConvertedCoordsY(event.getY()), getConvertedCoordsX(event.getX()), engine.getR(),
            engine.getG(), engine.getB());
        break;
      case MotionEvent.ACTION_UP:
        break;
    }
    previousX = event.getX();
    previousY = event.getY();
    return true;
  }

  public void startTimer() {
    engine.startTimer();
  }

  public void stopTimer() {
    engine.stopTimer();
  }

  @Override
  public void onEngineUpdate() {
    requestRender();
  }

  private float getConvertedCoordsX(float eventX) {
    float midpoint = (float) (getWidth() / 2);
    return (eventX - midpoint) / midpoint;
  }

  private float getConvertedCoordsY(float eventY) {
    float midpoint = (float) (getHeight() / 2);
    return (midpoint - eventY) / midpoint;
  }
}
