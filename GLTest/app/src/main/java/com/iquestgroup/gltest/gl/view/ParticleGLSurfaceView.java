package com.iquestgroup.gltest.gl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.iquestgroup.gltest.engine.BasicParticleEngine;
import com.iquestgroup.gltest.gl.renderer.ParticleRenderer;

/**
 * Created by victor.rad on 10/07/16.
 */

public class ParticleGLSurfaceView extends GLSurfaceView implements BasicParticleEngine.EngineUpdateListener {

  private BasicParticleEngine engine;

  public ParticleGLSurfaceView(Context context) {
    super(context);
    init();
  }

  public ParticleGLSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setEGLContextClientVersion(3);
    engine = new BasicParticleEngine();
    setRenderer(new ParticleRenderer(engine));
    engine.setListener(this);
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      engine.generateParticlesFromPoint(getConvertedCoordsY(event.getY()), getConvertedCoordsX(event.getX()), 0);
    }
    return true;
  }

  @Override
  public void onEngineUpdate() {
    requestRender();
  }

  public void start() {
    super.onResume();
    engine.startEngine();
  }

  public void stop() {
    super.onPause();
    engine.stopEngine();
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
