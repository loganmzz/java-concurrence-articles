package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ArrayBlockingQueueIteration {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final BlockingQueue<String> file = new ArrayBlockingQueue<>(100);
for (int i = 0; i < 3; i++) {
  new Thread("Producteur-" + i) {
    public void run() {
      try {
        for (int i = 0; i < 4; i++) {
          String e = getName() + "-" + i;
          Logger.println("Ajout %s", e);
          file.put(e);
          Thread.sleep(40);
        }
      } catch (InterruptedException e) {
        Logger.printStackTrace(e);
      }
    }
  }.start();
}
int i = 0;
Thread.sleep(50);
Logger.println("début");
for (String élément : file) {
  Logger.println("[%02d] %s", i++, élément);
  Thread.sleep(50);
}
Logger.println("fin");
//////////////////////////////////////////
  }
}
