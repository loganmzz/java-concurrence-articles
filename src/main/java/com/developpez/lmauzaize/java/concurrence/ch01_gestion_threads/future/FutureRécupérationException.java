package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class FutureRécupérationException {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class MonException extends Exception {}
Callable<Thread> tâche = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("Début");
    Logger.println("Fin");
    throw new MonException();
  }
};
ExecutorService executor = Executors.newSingleThreadExecutor();
try {
  Future<Thread> future = executor.submit(tâche);
  Thread thread = future.get();
  Logger.println("La tâche a été exécutée sur %s", thread);
} catch (ExecutionException e) {
  Logger.printStackTrace(e);
} finally {
  executor.shutdown();
}
//////////////////////////////////////////
  }
}
