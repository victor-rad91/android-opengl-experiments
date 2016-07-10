package com.iquestgroup.gltest.engine.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by victor.rad on 09/07/16.
 */

public class Originator {

  private static final Random RANDOM = new Random(System.currentTimeMillis());
  private static final int MIN_NR_OF_PARTICLES = 15;
  private static final int MAX_NR_OF_PARTICLES = 30;
  private static final float INITIAL_VELOCITY = 0.1f;

  private float x;
  private float y;

  public Originator(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public List<Particle> generateParticles(Particle.ParticleDeathCallback callback) {
    Random random = new Random();
    List<Particle> generatedParticles = new ArrayList<>();
    int nrOfParticlesToGenerate = MIN_NR_OF_PARTICLES + random.nextInt(MAX_NR_OF_PARTICLES - MIN_NR_OF_PARTICLES);
    double angleDiff = 360.0 / (double) nrOfParticlesToGenerate;
    for (int i=0; i<nrOfParticlesToGenerate; i++) {
      double angle = i * angleDiff;
      float xVelocity = (float) Math.sin(angle) * INITIAL_VELOCITY * RANDOM.nextFloat();
      float yVelocity = (float) Math.cos(angle) * INITIAL_VELOCITY * RANDOM.nextFloat();
      Particle particle = new Particle(x, y, 0, xVelocity, yVelocity, 0, 0.05f, callback);
      generatedParticles.add(particle);
    }
    return generatedParticles;
  }

}
