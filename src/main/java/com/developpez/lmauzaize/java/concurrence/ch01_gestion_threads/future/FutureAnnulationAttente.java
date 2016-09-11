package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class FutureAnnulationAttente {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
Callable<Thread> tâche = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("[Tâche] Début");
    Logger.println("[Tâche] Fin");
    return Thread.currentThread();
  }
};
Callable<Thread> attente = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("[Attente] Début");
    Thread.sleep(3_000);
    Logger.println("[Attente] Fin");
    return Thread.currentThread();
  }
};
ExecutorService executor = Executors.newSingleThreadExecutor();

// Monopolisation du service
executor.submit(attente);

// Soumission de la tâche à annuler
final Future<Thread> future = executor.submit(tâche);

// Programme l'annulation
new Thread("Annulation") {
  public void run() {
    try {
      Thread.sleep(2_000);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    } finally {
      Logger.println("Annulation de la tâche (%s)", future.cancel(false));
    }
  };
}.start();

// Récupération du résultat
try {
  Thread thread = future.get();
  Logger.println("La tâche a été exécutée sur %s", thread);
} catch (CancellationException e) {
  Logger.println("La tâche a été annulée");
}

executor.shutdown();
//////////////////////////////////////////
  }
}
