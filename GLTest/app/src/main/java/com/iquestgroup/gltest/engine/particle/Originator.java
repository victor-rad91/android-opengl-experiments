package com.iquestgroup.gltest.engine.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by victor.rad on 09/07/16.
 */

public class Originator {

  private static final int MIN_NR_OF_PARTICLES = 15;
  private static final int MAX_NR_OF_PARTICLES = 30;

  private float x;
  private float y;

  public Originator(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public List<Particle> generateParticles() {
    Random random = new Random(System.currentTimeMillis());
    List<Particle> generatedParticles = new ArrayList<>();

    int nrOfParticlesToGenerate = MIN_NR_OF_PARTICLES + random.nextInt(MAX_NR_OF_PARTICLES - MIN_NR_OF_PARTICLES);
    float angleDiff = 360.0f / (float) nrOfParticlesToGenerate;


    return generatedParticles;
  }

}
