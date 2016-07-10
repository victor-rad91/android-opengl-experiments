package com.iquestgroup.gltest.engine.particle;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Simple particle that moves according to initial velocity and a drag factor, with a given lifetime.
 *
 * @author victor.rad on 09/07/16.
 */

public class Particle {

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private static final int MINIMUM_LIFETIME = 1500;
  private static final int MAXIMUM_LIFETIME = 10000;

  private static final float DISTANCE_FROM_CENTER = 0.05f;
  private static final float SQRT_3 = (float) Math.sqrt(3);

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
  private int glProgram;

  private float r;
  private float g;
  private float b;

  private boolean isInitialized = false;

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
    lifetimeInMs = MINIMUM_LIFETIME + RANDOM.nextInt(MAXIMUM_LIFETIME - MINIMUM_LIFETIME);
    r = RANDOM.nextFloat();
    g = RANDOM.nextFloat();
    b = RANDOM.nextFloat();
  }

  public void initParticle() {
    if (!isInitialized) {
      initShaders();
      initGLProgram();
      isInitialized = true;
    }
  }

  private void initGLProgram() {
    glProgram = GLES30.glCreateProgram();
    if (glProgram == 0) {
      Log.e("Particle","Could not create GL program");
      return;
    }
    GLES30.glAttachShader(glProgram, vertexShader);
    GLES30.glAttachShader(glProgram, fragmentShader);
    GLES30.glBindAttribLocation(glProgram, 0, "vPosition");
    GLES30.glLinkProgram(glProgram);

    int[] linked = new int[1];
    GLES30.glGetProgramiv(glProgram, GLES30.GL_LINK_STATUS, linked, 0);
    if (linked[0] == 0) {
      Log.e("Particle","Error linking program:" + GLES30.glGetProgramInfoLog(glProgram));
      GLES30.glDeleteProgram(glProgram);
    }
  }

  private void initShaders() {
    String vertexShaderCode =
        "#version 300 es\n"
            + "in vec4 vPosition;\n"
            + "void main() {\n"
            + "   gl_Position = vPosition;\n"
            + "}\n";
    String fragmentShaderCode =
        "#version 300 es		 			          	\n"
            + "precision mediump float;					  	\n"
            + "out vec4 fragColor;	 			 		  	\n"
            + "void main()                                  \n"
            + "{                                            \n"
            + "  fragColor = vec4 ( " + r + "f, " + g + "f, " + b + "f, 1.0f );	\n"
            + "}                                            \n";
    vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
    fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);
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

  public void draw() {
    GLES30.glUseProgram(glProgram);

    GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, getCoordinatesAsBuffer());
    GLES30.glEnableVertexAttribArray(0);

    GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
  }

  private FloatBuffer getCoordinatesAsBuffer() {
    float ratio = 1.0f - (float) hasLivedForMs / (float) lifetimeInMs;
    float[] coordinates = new float[9];
    coordinates[0] = yPosition;
    coordinates[1] = xPosition - DISTANCE_FROM_CENTER * ratio;
    coordinates[2] = zPosition;

    coordinates[3] = yPosition - (DISTANCE_FROM_CENTER / 2.0f) * ratio;
    coordinates[4] = xPosition + ((DISTANCE_FROM_CENTER * 3.0f) / (2.0f * SQRT_3)) * ratio;
    coordinates[5] = zPosition;

    coordinates[6] = yPosition + (DISTANCE_FROM_CENTER / 2.0f) * ratio;
    coordinates[7] = xPosition + ((DISTANCE_FROM_CENTER * 3.0f) / (2.0f * SQRT_3)) * ratio;
    coordinates[8] = zPosition;

    ByteBuffer buffer = ByteBuffer.allocateDirect(coordinates.length * 4);
    buffer.order(ByteOrder.nativeOrder());

    FloatBuffer vertexBuffer = buffer.asFloatBuffer();
    vertexBuffer.put(coordinates);
    vertexBuffer.position(0);

    return vertexBuffer;
  }

  private int loadShader(int type, String code) {
    int shader = GLES30.glCreateShader(type);
    if (shader == 0) {
      return 0;
    }
    GLES30.glShaderSource(shader, code);
    GLES30.glCompileShader(shader);

    int[] compiled = new int[1];
    GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
    if (compiled[0] == 0) {
      Log.e("TrianglePoint","Failed to load shader: " + GLES30.glGetShaderInfoLog(shader));
      GLES30.glDeleteShader(shader);
      return 0;
    }
    return shader;
  }

  /**
   * Callback to notify engine to remove this particle from the draw list.
   */
  public interface ParticleDeathCallback {

    void onParticleDeath(Particle deadParticle);

  }
}
