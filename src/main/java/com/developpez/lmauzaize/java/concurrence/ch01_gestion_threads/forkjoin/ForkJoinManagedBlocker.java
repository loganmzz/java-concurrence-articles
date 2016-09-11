package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;
import com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin.ForkJoinBloqué.Verrou;

public class ForkJoinManagedBlocker {
  static class Verrouilleur implements ManagedBlocker, AutoCloseable {
    Verrou verrou;
    boolean verrouillé = false;
    Verrouilleur(Verrou verrou) throws InterruptedException {
      this.verrou = verrou;
      ForkJoinPool.managedBlock(this);
    }
    public boolean isReleasable() {
      if (!verrouillé) {
        verrouillé = verrou.tenter();
      }
      return verrouillé;
    }
    public boolean block() {
      if (!verrouillé) {
        verrou.prendre();
        verrouillé = true;
      }
      return verrouillé;
    }
    public void close() {
      verrou.libérer();
      verrouillé = false;
    }
  }

  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
final Verrou verrou = new Verrou();

class Bloqueur extends ForkJoinTâche {
  void calcul() {
    Logger.println("[%s] Pose du verrou", nom);
    try (Verrouilleur verrouilleur = new Verrouilleur(verrou)) {
      Logger.println("[%s] Calcul", nom);
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
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
    for (int i = 0; i < 10; i++) {
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
TimeUnit.SECONDS.sleep(3);
Logger.println("Libération du verrou");
verrou.libérer();
tâche.join();
//////////////////////////////////////////
}

}
