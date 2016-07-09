package com.iquestgroup.gltest.engine;


import android.util.Log;
import com.iquestgroup.gltest.shapes.TrianglePoint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleColorEngine {

  private static final float COLOR_MAX = 1000;
  private static final float COLOR_MIN = 100;

  private EngineUpdateListener listener;
  private volatile ArrayDeque<TrianglePoint> pointQueue = new ArrayDeque<>();

  private float r = 100;
  private float g = 100;
  private float b = 500;

  private Timer timer;

  private volatile int queueLength = 10;

  public void startTimer() {
    Log.d("SimpleColorEngineTimer","Starting timer");
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Log.d("SimpleColorEngineTimer","Timer ticks");
        queueLength++;
      }
    }, 1000, 1000);
  }

  public void stopTimer() {
    timer.cancel();
  }

  public void updateR(float dr) {
    r += dr;
    if (r > COLOR_MAX) {
      r = COLOR_MAX;
    }
    else if (r < COLOR_MIN) {
      r = COLOR_MIN;
    }
    if (listener != null) {
      listener.onEngineUpdate();
    }
  }

  public void updateG(float dg) {
    g += dg;
    if (g > COLOR_MAX) {
      g = COLOR_MAX;
    }
    else if (g < COLOR_MIN) {
      g = COLOR_MIN;
    }
    if (listener != null) {
      listener.onEngineUpdate();
    }
  }

  public void updateB(float db) {
    b += db;
    if (b > COLOR_MAX) {
      b = COLOR_MAX;
    }
    else if (b < COLOR_MIN) {
      b = COLOR_MIN;
    }
    if (listener != null) {
      listener.onEngineUpdate();
    }
  }

  public void putPointInQueue(float x, float y, float r, float g, float b) {
    pointQueue.addLast(new TrianglePoint(x, y, r, g, b));
    Log.d("SimpleColorEngine","Queue nr of elements is " + pointQueue.size());
    if (pointQueue.size() >= queueLength) {
      pointQueue.removeFirst();
    }
    if (listener != null) {
      listener.onEngineUpdate();
    }
  }

  public List<TrianglePoint> getPointQueue() {
    return new ArrayList<>(pointQueue);
  }

  public float getR() {
    return r / COLOR_MAX;
  }

  public float getG() {
    return g / COLOR_MAX;
  }

  public float getB() {
    return b / COLOR_MAX;
  }

  public void setListener(EngineUpdateListener listener) {
    this.listener = listener;
  }


  public interface EngineUpdateListener {

    void onEngineUpdate();

  }
}
