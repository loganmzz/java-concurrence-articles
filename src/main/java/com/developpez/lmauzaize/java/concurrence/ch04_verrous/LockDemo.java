package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class LockDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Ressource {
  class Tache {
    String nom;
    long pause;
    TimeUnit unite;
    Tache(String nom, long pause, TimeUnit unite) {
      this.nom = nom;
      this.pause = pause;
      this.unite = unite;
    }
    boolean verrouiller() {
      boolean verrouille = Ressource.this.verrou.tryLock();
      if (verrouille) {
        Logger.println("[%s] Verrouillage de %s", this, Ressource.this);
      } else {
        Logger.println("[%s] Ressource %s occupée", this, Ressource.this);
      }
      return verrouille;
    }
    public void liberer() {
      Ressource.this.verrou.unlock();
      Logger.println("[%s] Libération de %s", this, Ressource.this);
    }
    void pause() throws InterruptedException {
      unite.sleep(pause);
    }
    @Override
    public String toString() {
      return nom;
    }
  }
  Lock verrou = new ReentrantLock();
  String nom;
  Ressource(String nom) {
    this.nom = nom;
  }
  @Override
  public String toString() {
    return nom;
  }
}
final Ressource a = new Ressource("A");
final Ressource b = new Ressource("B");
final Ressource c = new Ressource("C");

class Gestionnaire implements AutoCloseable {

  private Deque<Ressource.Tache> taches = new LinkedList<>();
  private int nbThread;
  private ExecutorService executor = null;

  public Gestionnaire(int nbThread) {
    this.nbThread = nbThread;
  }

  class ResponsableTache implements Runnable {
    @Override
    public void run() {
      try {
        while (estActif()) {
          Ressource.Tache suivant = null;
          synchronized (taches) {
            for (Iterator<Ressource.Tache> it = taches.iterator(); it.hasNext() ;) {
              Ressource.Tache tache = it.next();
              if (tache.verrouiller()) {
                it.remove();
                suivant = tache;
                break;
              }
            }
          }
          if (suivant != null) {
            suivant.pause();
            suivant.liberer();
            synchronized (taches) {
              taches.notifyAll();
            }
          } else {
            TimeUnit.MICROSECONDS.sleep(200);
          }
        }
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
        executor.shutdown();
      }
    }
  }

  public void addTache(Ressource.Tache tache) {
    taches.addLast(tache);
  }

  // Indique si le gestionnaire est actif
  private boolean estActif() {
    return !executor.isShutdown();
  }

  // Créer "nbThread" thread pour traiter les tâches
  public void demarrer() {
    executor = Executors.newFixedThreadPool(nbThread);
    for (int i = 0; i < nbThread; i++) {
      executor.submit(new ResponsableTache());
    }
  }

  // Attend que la file de tâche soit vidée
  public void attendre() throws InterruptedException {
    synchronized (taches) {
      while (!taches.isEmpty()) {
        taches.wait();
      }
    }
  }
  
  @Override
  public void close() {
    if (executor != null) {
      executor.shutdown();
    }
  }
};

try (Gestionnaire gestionnaire = new Gestionnaire(2)) {
  gestionnaire.addTache(a.new Tache("A1", 200, TimeUnit.MILLISECONDS));
  gestionnaire.addTache(a.new Tache("A2", 200, TimeUnit.MILLISECONDS));
  gestionnaire.addTache(b.new Tache("B1", 200, TimeUnit.MILLISECONDS));
  gestionnaire.addTache(c.new Tache("C1", 200, TimeUnit.MILLISECONDS));
  gestionnaire.addTache(c.new Tache("C2", 200, TimeUnit.MILLISECONDS));
  gestionnaire.addTache(b.new Tache("B2", 200, TimeUnit.MILLISECONDS));

  gestionnaire.demarrer();
  gestionnaire.attendre();
}
//////////////////////////////////////////
  }
}

