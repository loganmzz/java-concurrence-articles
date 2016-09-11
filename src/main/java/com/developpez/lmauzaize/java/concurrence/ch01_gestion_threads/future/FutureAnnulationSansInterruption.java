package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class FutureAnnulationSansInterruption {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
Callable<Thread> tâche = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("[Attente] Début");
    Thread.sleep(5_000);
    Logger.println("[Attente] Fin");
    return Thread.currentThread();
  }
};
ExecutorService executor = Executors.newSingleThreadExecutor();

// Soumission de la tâche à annuler
Future<Thread> future = executor.submit(tâche);

// Programme l'annulation
Thread.sleep(2_000);
Logger.println("Annulation de la tâche (%s)", future.cancel(false));

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
