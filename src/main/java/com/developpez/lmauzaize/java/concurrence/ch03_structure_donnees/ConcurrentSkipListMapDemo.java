package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.developpez.lmauzaize.java.concurrence.Logger;

//////////////////////////////////////////
public class ConcurrentSkipListMapDemo {
  class Activité implements Runnable {
    String nom;
    List<Runnable> actions = new ArrayList<>();
    Activité(String nom) {
      this.nom  = nom;
    }
    public void run() {
      String nom = Thread.currentThread().getName();
      try {
        Thread.currentThread().setName(this.nom);
        for (Runnable action : actions) {
          action.run();
        }
      } finally {
        Thread.currentThread().setName(nom);
      }
    }
    String clé(int i) {
      return String.format("%s-%04d", nom, i);
    }

    // Actions

    /** Insère la clé "<nom>-<i>" **/
    Activité put(final int i) {
      actions.add(new Runnable() {
        public void run() {
          String clé = clé(i);
          map.put(clé, nom);
          Logger.println("Ajout %s", clé);
        }
      });
      return this;
    }
    /** Supprime la clé **/
    Activité remove(final String clé) {
      actions.add(new Runnable() {
        public void run() {
          map.remove(clé);
          Logger.println("Retrait %s", clé);
        }
      });
      return this;
    }
    /** Crée une map content "n" clé ("<nom>-<i>") et l'insère massivement **/
    Activité putAll(final int début, final int n) {
      actions.add(new Runnable() {
        public void run() {
          Map<String, String> temp = new HashMap<>(n, 1.0f);
          for (int i = 0; i < n; i++) {
            temp.put(clé(début + i), nom);
          }
          map.putAll(temp);
        }
      });
      return this;
    }
    /** Itère sur les entrées et fait une pause entre chaque **/
    Activité parcours(final TimeUnit unité, final long temps) {
      actions.add(new Runnable() {
        public void run() {
          try {
            for (Entry<String, String> entry : map.entrySet()) {
              unité.sleep(temps);
              Logger.println("%s", entry);
            }
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
      return this;
    }
    /** Effectue une pause **/
    Activité pause(final TimeUnit unité, final int temps) {
      actions.add(new Runnable() {
        public void run() {
          try {
            unité.sleep(temps);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
      return this;
    }
  }

  ConcurrentSkipListMap<String, String> map = new ConcurrentSkipListMap<>();
  List<Activité> activités = new ArrayList<>();
  List<Runnable> après     = Collections.emptyList();
  List<Runnable> avant     = Collections.emptyList();
  List<Runnable> courant   = Collections.emptyList();
  /** Crée une nouvelle activité. Les activités sont exécutées en parallèle **/
  Activité activité(String nom) {
    Activité activité = new Activité(nom);
    activités.add(activité);
    return activité;
  }
  /** Indique les actions avant "exécuter" **/
  ConcurrentSkipListMapDemo avant() {
    avant = new ArrayList<>();
    courant = avant;
    return this;
  }
  /** Indique les actions après "exécuter" **/
  ConcurrentSkipListMapDemo après() {
    après = new ArrayList<>();
    courant = après;
    return this;
  }
  /** Insère "n" clés **/
  ConcurrentSkipListMapDemo remplir(final int n) {
    courant.add(new Runnable() {
      public void run() {
        for (int i = 0; i < n; i++) {
          map.put(String.format("main-%02d", i), "main");
        }
      }
    });
    return this;
  }
  /** Affiche les noms de thread consécutifs **/
  ConcurrentSkipListMapDemo verifierChangementThread() {
    courant.add(new Runnable() {
      public void run() {
        List<String> clés = new ArrayList<>();
        String nom = null;
        int n = 0;
        for (Entry<String, String> entry : map.entrySet()) {
          if (nom == null) {
            nom = entry.getValue();
          } else {
            if (!nom.equals(entry.getValue())) {
              clés.add(String.format("%s x %,d", nom , n));
              n = 0;
              nom = entry.getValue();
            }
          }
          n++;
        }
        clés.add(String.format("%s x %,d", nom , n));
        Logger.println("%s", clés);
      }
    });
    return this;
  }
  /** Exécute les actions "avant", puis lance les activités et enfin exécute les actions "après" **/
  void exécuter() throws InterruptedException {
    courant = Collections.emptyList();

    for (Runnable action : avant) {
      action.run();
    }
    avant = Collections.emptyList();

    List<Activité> activités = new ArrayList<>(this.activités);
    this.activités.clear();
    ExecutorService executor = Executors.newFixedThreadPool(activités.size());
    for (Activité activité : activités) {
      executor.submit(activité);
    }
    executor.shutdown();
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
      executor.shutdownNow();
      throw new CancellationException();
    }

    for (Runnable action : après) {
      action.run();
    }
    après = Collections.emptyList();

    map.clear();
  }
//////////////////////////////////////////
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
ConcurrentSkipListMapDemo demo = new ConcurrentSkipListMapDemo();

Logger.println("---------------------------------");
Logger.println("Itération");
demo.avant().remplir(5);
demo.activité("iter0")                                   .parcours(TimeUnit.MILLISECONDS, 100);
demo.activité("iter1") .pause(TimeUnit.MILLISECONDS, 200).parcours(TimeUnit.MILLISECONDS, 100);
demo.activité("put_10").pause(TimeUnit.MILLISECONDS, 500).put(10);
demo.activité("rmv_02").pause(TimeUnit.MILLISECONDS, 400).remove("main-02");
demo.activité("iter2") .pause(TimeUnit.MILLISECONDS, 800).parcours(TimeUnit.MILLISECONDS,   0);
demo.exécuter();

Logger.println("---------------------------------");
Logger.println("Ajout massif");
for (int i = 0; i < 5; i++) {
  demo.activité("putAll" + i).putAll(0, 200_000);
}
demo.après().verifierChangementThread();
demo.exécuter();
//////////////////////////////////////////
}
}
