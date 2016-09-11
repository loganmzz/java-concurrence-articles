package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.threadfactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class DefaultThreadFactoryDemo {


public static Thread créerThreadLimité(Runnable tâche, String name) {
  ThreadGroup groupe = new ThreadGroup("groupe");
  groupe.setDaemon(true);
  groupe.setMaxPriority(Thread.MIN_PRIORITY);
  Thread thread = new Thread(groupe, tâche, name);
  thread.setDaemon(true);
  class FauxClassLoader extends ClassLoader {}
  thread.setContextClassLoader(new FauxClassLoader());
  thread.setUncaughtExceptionHandler(Logger.exceptionHandler);
  return thread;
}


public static void affiche(String cas, Thread thread) {
  Logger.println("");
  Logger.println(cas);
  Logger.println("  nom     =%s", thread.getName());
  Logger.println("  groupe  =%s", thread.getThreadGroup().getName());
  Logger.println("  démon   =%s", thread.isDaemon());
  Logger.println("  priorité=%s", thread.getPriority());
  Logger.println("  loader  =%s", thread.getContextClassLoader().getClass().getSimpleName());
  Logger.println("  handler =%s", thread.getUncaughtExceptionHandler());
}

public static void main(String[] args) throws InterruptedException {

  ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
  ThreadFactory customThreadFactory  = Thread::new;

  Runnable tâche = () -> {
    affiche("defaultThreadFactory", defaultThreadFactory.newThread(null));
    affiche("customThreadFactory" , customThreadFactory.newThread(null));
  };

  Logger.println("");
  Logger.println("");
  Logger.println("******************************");
  Logger.println("Depuis main");
  tâche.run();

  Logger.println("");
  Logger.println("");
  Logger.println("******************************");
  Logger.println("Depuis un nouveau thread");
  Thread defaultThread = defaultThreadFactory.newThread(tâche);
  defaultThread.start();
  defaultThread.join();

  Logger.println("");
  Logger.println("");
  Logger.println("******************************");
  Logger.println("Depuis un groupe restreint");
  Thread thread = créerThreadLimité(tâche, "thread");
  thread.start();
  thread.join();
}

}
