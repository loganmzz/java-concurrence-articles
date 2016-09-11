package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.concurrent.SynchronousQueue;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class SynchronousQueueIteration {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final SynchronousQueue<String> queue = new SynchronousQueue<>();
new Thread("Producteur") {
  public void run() {
    try {
      Logger.println("Ajout");
      queue.put(getName());
      Logger.println("Ajouté");
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
}.start();
Thread.sleep(1_000);
int i = 0;
Logger.println("début");
for (String element : queue) {
  Logger.println("[%02d] %s", i++, element);
}
Logger.println("fin");
Logger.println("%s", queue.poll());
//////////////////////////////////////////
  }
}
