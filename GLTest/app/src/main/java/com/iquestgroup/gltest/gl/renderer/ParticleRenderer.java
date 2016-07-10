package com.iquestgroup.gltest.gl.renderer;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import com.iquestgroup.gltest.engine.BasicParticleEngine;
import com.iquestgroup.gltest.engine.particle.Particle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.List;

/**
 * @author victor.rad on 10/07/16.
 */

public class ParticleRenderer implements GLSurfaceView.Renderer {

  private BasicParticleEngine engine;

  public ParticleRenderer(BasicParticleEngine engine) {
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
    GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

    List<Particle> particles = engine.getParticles();
    for (Particle particle : particles) {
      particle.initParticle();
      particle.draw();
    }

  }
}
