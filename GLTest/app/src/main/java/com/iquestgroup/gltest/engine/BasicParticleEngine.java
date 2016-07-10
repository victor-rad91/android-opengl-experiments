package com.iquestgroup.gltest.engine;

import com.iquestgroup.gltest.engine.particle.Originator;
import com.iquestgroup.gltest.engine.particle.Particle;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by victor.rad on 09/07/16.
 */

public class BasicParticleEngine implements Particle.ParticleDeathCallback {

  private List<Particle> particles;

  private Timer timer;
  private TimerTask particleUpdateTask;

  private EngineUpdateListener listener;

  public BasicParticleEngine() {
    particles = new CopyOnWriteArrayList<>();
    particleUpdateTask = new TimerTask() {
      @Override
      public void run() {
        for (Particle particle : particles) {
          particle.updatePosition();
        }
        if (listener != null) {
          listener.onEngineUpdate();
        }
      }
    };
  }

  public void setListener(EngineUpdateListener listener) {
    this.listener = listener;
  }

  public void startEngine() {
    timer = new Timer();
    timer.scheduleAtFixedRate(particleUpdateTask, 20, 20);
  }

  public void stopEngine() {
    timer.cancel();
  }

  public void generateParticlesFromPoint(float x, float y, float z) {
    Originator originator = new Originator(x, y);
    particles.addAll(originator.generateParticles(this));
  }

  public List<Particle> getParticles() {
    return particles;
  }

  @Override
  public void onParticleDeath(Particle deadParticle) {
    particles.remove(deadParticle);
  }

  public interface EngineUpdateListener {

    void onEngineUpdate();

  }
}
