package com.iquestgroup.gltest.engine.particle;

import java.util.Random;

/**
 * Simple particle that moves according to initial velocity and a drag factor, with a given lifetime.
 *
 * @author victor.rad on 09/07/16.
 */

public class Particle {

  public static final int VELOCITY_REFRESH_INTERVAL_IN_MS = 20;
  public static final int MINIMUM_LIFETIME = 1500;
  public static final int MAXIMUM_LIFETIME = 10000;

  private float xVelocity;
  private float yVelocity;
  private float zVelocity;

  private float xPosition;
  private float yPosition;
  private float zPosition;

  private float drag;

  private int lifetimeInMs;
  private int hasLivedForMs;

  private int vertexShader;
  private int fragmentShader;

  private ParticleDeathCallback callback;

  public Particle(float initialX, float initialY, float initialZ,
      float xVelocity, float yVelocity, float zVelocity,
      float drag, ParticleDeathCallback callback) {
    this.xPosition = initialX;
    this.yPosition = initialY;
    this.zPosition = initialZ;
    this.xVelocity = xVelocity;
    this.yVelocity = yVelocity;
    this.zVelocity = zVelocity;
    this.drag = drag;
    this.callback = callback;
    Random random = new Random(System.currentTimeMillis());
    lifetimeInMs = MINIMUM_LIFETIME + random.nextInt(MAXIMUM_LIFETIME - MINIMUM_LIFETIME);
  }

  public void initShaders() {
    String vertexShaderCode =
        "#version 300 es\n"
        + "in vec4 vPosition;\n"
        + "void main() {\n"
        + "   gl_Position = vPosition;\n"
        + "}\n";
  }

  /**
   * Call this method in the engine's update cycle every 20 ms.
   */
  public void updatePosition() {
    hasLivedForMs += 20;
    if (hasLivedForMs > lifetimeInMs) {
      callback.onParticleDeath(this);
      callback = null;
    }
    else {
      xPosition += xVelocity;
      yPosition += yVelocity;
      zPosition += zVelocity;
      xVelocity *= (1 - drag);
      yVelocity *= (1 - drag);
      zVelocity *= (1 - drag);
    }
  }

  /**
   * Callback to notify engine to remove this particle from the draw list.
   */
  public interface ParticleDeathCallback {

    void onParticleDeath(Particle deadParticle);

  }
}
