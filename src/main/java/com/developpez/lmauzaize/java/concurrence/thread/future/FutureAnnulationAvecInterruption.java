package com.developpez.lmauzaize.java.concurrence.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class FutureAnnulationAvecInterruption {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
Callable<Thread> tâche = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("[Attente] Début");
    try {
      Thread.sleep(5_000);
    } catch (InterruptedException e) {
      Logger.println("[Attente] La tâche a été annulée");
    }
    Logger.println("[Attente] Fin");
    return Thread.currentThread();
  }
};
Callable<Thread> suite = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("[Suite] Début");
    Logger.println("[Suite] Fin");
    return Thread.currentThread();
  }
};
ExecutorService executor = Executors.newSingleThreadExecutor();
// Soumission des tâches
Future<Thread> future1 = executor.submit(tâche);
Future<Thread> future2 = executor.submit(suite);

// Programme l'annulation
Thread.sleep(2_000);
Logger.println("Annulation de la tâche [Attente](%s)", future1.cancel(true));

// Récupération des résultats
try {
  Thread thread = future1.get();
  Logger.println("La tâche [Attente] a été exécutée sur %s", thread.getName());
} catch (CancellationException e) {
  Logger.println("La tâche [Attente] a été annulée");
}
Thread thread = future2.get();
Logger.println("La tâche [Suite  ] a été exécutée sur %s", thread.getName());

executor.shutdown();
//////////////////////////////////////////
  }
}
