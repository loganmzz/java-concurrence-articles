package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class NouveauRunnable {
  public static void main(String[] args) {

Runnable runnable = new Runnable() {
  public void run() {
    Logger.println("Je suis un nouveau thread !");
  }
};
new Thread(runnable).start();
new Thread(runnable).start();

  }
}
