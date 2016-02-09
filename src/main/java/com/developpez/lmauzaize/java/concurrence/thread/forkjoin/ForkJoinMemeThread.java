package com.developpez.lmauzaize.java.concurrence.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ForkJoinMemeThread {

static class SousTâche extends ForkJoinTâche {
  void calcul() {
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
static class SousTâche1 extends SousTâche {}
static class SousTâche2 extends SousTâche {}
static class Tâche extends ForkJoinTâche {
  void calcul() {
    invokeAll(new SousTâche1(), new SousTâche2());
  }
}

  public static void main(String[] args) throws Exception {
//////////////////////////////////////////

new ForkJoinPool().invoke(new Tâche());
//////////////////////////////////////////
}

}
