package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.concurrent.SynchronousQueue;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class SynchronousQueueDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final SynchronousQueue<String> queue = new SynchronousQueue<>();
//Non bloquant avec exception
try {
  queue.add("A");
} catch (Exception e) {
  Logger.println("Ajout refusé: %s", e);
}
//Non bloquant avec valeur
Logger.println("Ajout? %b", queue.offer("A"));


new Thread("Consommateur") {
  public void run() {
    try {
      Logger.println("Lecture non bloquante : %s", queue.poll());
      Thread.sleep(1_500);
      Logger.println("Lecture non bloquante : %s", queue.poll());
      Logger.println("Attente");
      Logger.println("Lecture     bloquante : %s", queue.take());
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}.start();
new Thread("Producteur") {
  public void run() {
    try {
      Thread.sleep(1_000);
      Logger.println("Ajout       bloquant  : A");
      queue.put("A");
      Thread.sleep(1_000);
      Logger.println("Ajout   non bloquant  : B");
      queue.add("B");
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}.start();
//////////////////////////////////////////
  }
}
