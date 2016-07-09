package com.iquestgroup.gltest.engine;

import com.iquestgroup.gltest.engine.particle.Originator;
import com.iquestgroup.gltest.engine.particle.Particle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor.rad on 09/07/16.
 */

public class BasicParticleEngine implements Particle.ParticleDeathCallback {

  private List<Particle> particles;

  private Timer timer;
  private TimerTask particleUpdateTask;

  public BasicParticleEngine() {
    particles = Collections.synchronizedList(new ArrayList<Particle>());
  }

  public void startEngine() {

  }

  public void stopEngine() {

  }

  public void generateParticlesFromPoint(float x, float y, float z) {
    Originator originator = new Originator(x, y);
  }

  @Override
  public void onParticleDeath(Particle deadParticle) {
    particles.remove(deadParticle);
  }
}
