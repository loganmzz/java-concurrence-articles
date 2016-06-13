package com.developpez.lmauzaize.java.concurrence.exchanger;

import java.util.concurrent.Exchanger;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ExchangerDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
final Exchanger<String> exchanger = new Exchanger<>();
Runnable tache = new Runnable() {
  public void run() {
    String envoi = Thread.currentThread().getName();
    Logger.println("Envoi %s", envoi);
    try {
      String recu = exchanger.exchange(envoi);
      Logger.println("Reçu  %s", recu);
    } catch (InterruptedException e) {
      Logger.printStackTrace(e);
    }
  }
};
new Thread(tache, "Foo").start();
new Thread(tache, "Bar").start();
//////////////////////////////////////////
  }
}
