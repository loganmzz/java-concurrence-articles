package com.developpez.lmauzaize.java.concurrence.thread.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ForkJoinBloqué {

static class Verrou {
  Semaphore semaphore = new Semaphore(1);

  public void prendre() {
    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      CancellationException ex = new CancellationException();
      ex.initCause(e);
      throw ex;
    }
  }

  public void libérer() {
    semaphore.release();
  }

  public boolean tenter() {
    return semaphore.tryAcquire();
  }
}

  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
final Verrou verrou = new Verrou();
class Bloqueur extends ForkJoinTâche {
  void calcul() {
    try {
      Logger.println("[%s] Pose du verrou", nom);
      verrou.prendre();
      Logger.println("[%s] Calcul", nom);
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    } finally {
      verrou.libérer();
    }
  }
}
class SousTâche extends ForkJoinTâche {
  SousTâche(int n) {
    super(n);
  }
  void calcul() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
class Tâche extends ForkJoinTâche {
  protected void calcul() {
    List<ForkJoinTask> actions = new ArrayList<>();
    actions.add(new Bloqueur());
    for (int i = 0; i < 5; i++) {
      actions.add(new SousTâche(i));
    }
    invokeAll(actions);
  }
}

Logger.println("Pose du verrou");
verrou.prendre();
Logger.println("Soumission");
Tâche tâche = new Tâche();
new ForkJoinPool(1).execute(tâche);
Logger.println("Pause");
TimeUnit.SECONDS.sleep(5);
Logger.println("Libération du verrou");
verrou.libérer();
tâche.join();
//////////////////////////////////////////
}

}
