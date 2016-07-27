package com.iquestgroup.gltest.shapes;


import android.opengl.GLES30;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TrianglePoint {

  private static final float DISTANCE_FROM_CENTER = 0.05f;
  private static final float SQRT_3 = (float) Math.sqrt(3);

  private float centerX;
  private float centerY;
  private float r;
  private float g;
  private float b;

  private int glProgram;

  private FloatBuffer vertexBuffer;

  private float coordinates[] = new float[9];
  private int vertexShader;
  private int fragmentShader;

  private boolean isInitialized = false;

  private float distanceFromCenter = DISTANCE_FROM_CENTER;

  public TrianglePoint(float centerX, float centerY, float r, float g, float b) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public TrianglePoint(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public void init(boolean projectedShader, @Nullable float[] userCoordinates) {
    if (!isInitialized) {
      updateBuffers(userCoordinates);
      compileShaders(projectedShader);
      glProgram = GLES30.glCreateProgram();
      if (glProgram == 0) {
        Log.e("TrianglePoint","Could not create GL program");
        return;
      }
      GLES30.glAttachShader(glProgram, vertexShader);
      GLES30.glAttachShader(glProgram, fragmentShader);
      GLES30.glBindAttribLocation(glProgram, 0, "vPosition");
      GLES30.glLinkProgram(glProgram);

      int[] linked = new int[1];
      GLES30.glGetProgramiv(glProgram, GLES30.GL_LINK_STATUS, linked, 0);
      if (linked[0] == 0) {
        Log.e("TrianglePoint","Error linking program:" + GLES30.glGetProgramInfoLog(glProgram));
        GLES30.glDeleteProgram(glProgram);
      }
    }
    isInitialized = true;
  }

  public float getX() {
    return centerX;
  }

  public float getY() {
    return centerY;
  }

  public void draw() {
    GLES30.glUseProgram(glProgram);

    GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
    GLES30.glEnableVertexAttribArray(0);

    GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
  }

  public void draw(float[] mvpMatrix) {
    GLES30.glUseProgram(glProgram);

    int mvpMatrixHandle = GLES30.glGetUniformLocation(glProgram, "mvpMatrix");
    GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
    GLES30.glEnableVertexAttribArray(0);
    GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
    GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
  }

  private void updateBuffers(float[] userCoordinates) {
    if (userCoordinates == null) {
      coordinates[0] = centerY;
      coordinates[1] = centerX - distanceFromCenter;
      coordinates[2] = 0;

      coordinates[3] = centerY - distanceFromCenter / 2.0f;
      coordinates[4] = centerX + (distanceFromCenter * 3.0f) / (2.0f * SQRT_3);
      coordinates[5] = 0;

      coordinates[6] = centerY + distanceFromCenter / 2.0f;
      coordinates[7] = centerX + (distanceFromCenter * 3.0f) / (2.0f * SQRT_3);
      coordinates[8] = 0;
    }
    else {
      coordinates = userCoordinates;
    }

    ByteBuffer buffer = ByteBuffer.allocateDirect(coordinates.length * 4);
    buffer.order(ByteOrder.nativeOrder());

    vertexBuffer = buffer.asFloatBuffer();
    vertexBuffer.put(coordinates);
    vertexBuffer.position(0);
  }

  private void compileShaders(boolean projectedShader) {
    String vertexShaderCode = projectedShader
        ?
          "#version 300 es 			  \n"
            + "uniform mat4 mvpMatrix;      \n"
            + "in vec4 vPosition;           \n"
            + "void main()                  \n"
            + "{                            \n"
            + "   gl_Position = mvpMatrix * vPosition;\n"
            + "}                            \n"
        :
          "#version 300 es 			  \n"
          + "in vec4 vPosition;           \n"
          + "void main()                  \n"
          + "{                            \n"
          + "   gl_Position = vPosition;  \n"
          + "}                            \n";
    vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
    String fragmentShaderCode =
        "#version 300 es		 			          	\n"
            + "precision mediump float;					  	\n"
            + "out vec4 fragColor;	 			 		  	\n"
            + "void main()                                  \n"
            + "{                                            \n"
            + "  fragColor = vec4 ( " + r + " ," + g + " ," + b + " , 1.0f );	\n"
            + "}                                            \n";
    fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);
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

}
