package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;
import com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin.ForkJoinBloqué.Verrou;
import com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin.ForkJoinManagedBlocker.Verrouilleur;

public class ForkJoinManagedBlockerLimite {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
class TâcheBloquante extends ForkJoinTâche {
  Verrou verrou1 = new Verrou();
  Verrou verrou2 = new Verrou();
  TâcheBloquante(int n) {
    super(n);
  }
  protected void calcul() {
    try {
      Logger.println("[%s] Attente non-managed", nom);
      verrou1.prendre();

      Logger.println("[%s] Passage en mode 'managed'", nom);
      try (Verrouilleur verrouilleur = new Verrouilleur(verrou2)) {}
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
class SousTâche extends ForkJoinTâche {
  SousTâche(int n) {
    super(n);
  }
  @Override
  void calcul() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}
class Tâche extends ForkJoinTâche {
  @Override
  void calcul() {
    List<ForkJoinTâche> soustâches = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      soustâches.add(new SousTâche(i));
    }
    invokeAll(soustâches);
  }
}

int parallélisme = 2;
ForkJoinPool pool = new ForkJoinPool(parallélisme);

Logger.println("Saturation du pool");
List<TâcheBloquante> bloqués = new ArrayList<>(parallélisme);
for (int i = 1; i <= parallélisme; i++) {
  TâcheBloquante bloqué = new TâcheBloquante(i);
  bloqué.verrou1.prendre();
  bloqué.verrou2.prendre();
  pool.submit(bloqué);
  bloqués.add(bloqué);
}

Logger.println("Ajout de tâches supplémentaires");
Tâche tâche = new Tâche();
pool.submit(tâche);

for (TâcheBloquante bloqué : bloqués) {
  TimeUnit.SECONDS.sleep(5);
  bloqué.verrou1.libérer();
}

tâche.join();
Logger.println("Libération des tâches bloquantes");
for (TâcheBloquante bloqué : bloqués) {
  bloqué.verrou2.libérer();
  bloqué.join();
}
//////////////////////////////////////////
}

}
