package com.iquestgroup.gltest.gl;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import com.iquestgroup.gltest.engine.SimpleColorEngine;
import com.iquestgroup.gltest.shapes.TrianglePoint;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class SimpleColorEngineRenderer implements GLSurfaceView.Renderer {

  private SimpleColorEngine engine;

  SimpleColorEngineRenderer(SimpleColorEngine engine) {
    this.engine = engine;
  }

  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    GLES30.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
  }

  @Override
  public void onSurfaceChanged(GL10 gl10, int width, int height) {
    GLES30.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(GL10 gl10) {
    if (engine != null) {
      GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.3f);
      GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

      for (TrianglePoint point : engine.getPointQueue()) {
        if (point != null) {
          point.init();
          point.draw();
        }
      }
    }
  }
}
