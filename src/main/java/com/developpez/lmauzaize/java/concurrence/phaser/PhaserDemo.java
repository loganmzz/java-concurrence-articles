package com.developpez.lmauzaize.java.concurrence.phaser;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Phaser;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class PhaserDemo {
  public static void main(String[] args) throws InterruptedException,BrokenBarrierException {
//////////////////////////////////////////
Phaser phaser = new Phaser() {
  {
    println("<init>");
  }

  private void println(String action) {
    Logger.println("{%-10s} phase=%02d  registred=%02d arrived=%02d unarrived=%02d", action, getPhase(), getRegisteredParties(), getArrivedParties(), getUnarrivedParties());
  }

  @Override
  public int register() {
    int register = super.register();
    println("register");
    return register;
  }

  @Override
  public int arrive() {
    int arrive = super.arrive();
    println("arrive");
    return arrive;
  }

  protected boolean onAdvance(int phase, int registeredParties) {
    Logger.println("{%-10s} phase=%02d  registred=%02d", "onAdvance", phase, registeredParties);
    return super.onAdvance(phase, registeredParties);
  }
};
phaser.register();
phaser.register();
phaser.arrive();
phaser.arrive();
//////////////////////////////////////////
  }
}
