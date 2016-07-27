package com.iquestgroup.gltest.gl.renderer;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.iquestgroup.gltest.shapes.TrianglePoint;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.opengl.GLES30.GL_BACK;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES30.GL_DEPTH_TEST;
import static android.opengl.GLES30.GL_LEQUAL;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glClearDepthf;
import static android.opengl.GLES30.glCullFace;
import static android.opengl.GLES30.glDepthFunc;
import static android.opengl.GLES30.glEnable;
import static android.opengl.GLES30.glViewport;

public class CameraRotationRenderer implements GLSurfaceView.Renderer {

  private float[] mvpMatrix = new float[16];
  private float[] projectionMatrix = new float[16];
  private float[] viewMatrix = new float[16];

  private float xPos = 0;
  private float yPos = 0;
  private float zPos = 1;
  private float xUp = 0;
  private float yUp = -1;
  private float zUp = 0;

  private static final float TRANSLATION_STEP = 0.001f;
  private float xTranslation = 0;
  private float yTranslation = 0;
  private float zTranslation = 0;

  private List<TrianglePoint> pointList = new ArrayList<>();
  private Random random;

  @Override
  public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
    glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
    random = new Random(System.currentTimeMillis());
    double pitchOfA = Math.toRadians(45);
    double pitchOfBC = Math.toRadians(135);
    for (int i=0; i<18; i++) {
      double azimuthOfA = Math.toRadians(20 * i);
      double azimuthOfB = azimuthOfA - Math.toRadians(8.0);
      double azimuthOfC = azimuthOfA + Math.toRadians(8.0);

      float[] coordinates = new float[9];
      // A
      coordinates[0] = (float) (Math.sin(pitchOfA) * Math.cos(azimuthOfA));
      coordinates[1] = (float) (Math.cos(pitchOfA));
      coordinates[2] = (float) (Math.sin(pitchOfA) * Math.sin(azimuthOfA));
      // B
      coordinates[3] = (float) (Math.sin(pitchOfBC) * Math.cos(azimuthOfB));
      coordinates[4] = (float) (Math.cos(pitchOfBC));
      coordinates[5] = (float) (Math.sin(pitchOfBC) * Math.sin(azimuthOfB));
      // C
      coordinates[6] = (float) (Math.sin(pitchOfBC) * Math.cos(azimuthOfC));
      coordinates[7] = (float) (Math.cos(pitchOfBC));
      coordinates[8] = (float) (Math.sin(pitchOfBC) * Math.sin(azimuthOfC));

      Log.d("CameraRotationRenderer",String.format("Initializing polygon with coords (%.3f,%.3f,%.3f)"
          + "|(%.3f,%.3f,%.3f)|(%.3f,%.3f,%.3f)", coordinates[0], coordinates[1], coordinates[2],
          coordinates[3], coordinates[4], coordinates[5],
          coordinates[6], coordinates[7], coordinates[8]));

      TrianglePoint point = new TrianglePoint(getNextRandomFloat(), getNextRandomFloat(), getNextRandomFloat());
      point.init(true, coordinates);
      pointList.add(point);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl10, int width, int height) {
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glClearDepthf(2.5f);
    glViewport(0, 0, width, height);
    glCullFace(GL_BACK);
    float ratio = (float) width / (float) height;
    Matrix.frustumM(projectionMatrix, 0, -0.5f * ratio, 0.5f * ratio, -0.5f, 0.5f, 0.25f, 3.5f);
  }

  @Override
  public void onDrawFrame(GL10 gl10) {
    Matrix.setLookAtM(viewMatrix, 0, xTranslation, -yTranslation, zTranslation, xPos, -yPos, zPos, xUp, yUp, zUp);
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    for (TrianglePoint point : pointList) {
      point.draw(mvpMatrix);
    }
  }

  public void setCameraPosition(float xPos, float yPos, float zPos, float xUp, float yUp, float zUp,
      float xTranslation, float yTranslation, float zTranslation) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.zPos = zPos;
    this.xUp = xUp;
    this.yUp = yUp;
    this.zUp = zUp;
    this.xTranslation = xTranslation;
    this.yTranslation = yTranslation;
    this.zTranslation = zTranslation;
  }

  private float getNextRandomFloat() {
    return random.nextBoolean() ? random.nextFloat() : -random.nextFloat();
  }
}
