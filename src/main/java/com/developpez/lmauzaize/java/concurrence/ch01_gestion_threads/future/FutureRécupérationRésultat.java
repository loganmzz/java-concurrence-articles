package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class FutureRécupérationRésultat {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
Callable<Thread> tâche = new Callable<Thread>() {
  public Thread call() throws Exception {
    Logger.println("Début");
    Thread.sleep(2_000);
    Logger.println("Fin");
    return Thread.currentThread();
  }
};
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Thread> future = executor.submit(tâche);
Thread thread = future.get();
Logger.println("La tâche a été exécutée sur %s", thread.getName());
executor.shutdown();
//////////////////////////////////////////
  }
}
