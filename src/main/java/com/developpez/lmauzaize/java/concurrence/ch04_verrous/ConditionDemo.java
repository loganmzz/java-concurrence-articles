package com.developpez.lmauzaize.java.concurrence.ch04_verrous;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class ConditionDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Messagerie {
  ReentrantLock verrou = new ReentrantLock();
  Condition lecture  = verrou.newCondition();
  Condition ecriture = verrou.newCondition();
  Condition libre    = verrou.newCondition();

  volatile String  envoi   = null;
  volatile boolean occupe  = false;

  void envoyer(String message) throws InterruptedException {
    verrou.lock();
    try {

      // Occupation du canal
      while (occupe) {
        libre.await();
      }
      occupe = true;

      // Envoi
      envoi = message;
      ecriture.signal();

      // Attente de l'accusé
      while (envoi == message) {
        lecture.await();
      }

      // Libération du canal
      occupe = false;
      libre.signal();

    } finally {
      verrou.unlock();
    }
  }

  String recevoir() throws InterruptedException {
    verrou.lock();
    try {

      // Attente d'un message
      while (envoi == null) {
        ecriture.await();
      }

      // Réception
      String message = envoi;

      // Accusation
      envoi = null;
      lecture.signalAll();

      return message;

    } finally {
      verrou.unlock();
    }
  }
}
final Messagerie messagerie = new Messagerie();
final int messageParEmetteur = 3;
int emetteur = 2;
class Emetteur implements Callable<Void> {
  @Override
  public Void call() {
    String threadName = Thread.currentThread().getName();
    Thread.currentThread().setName(threadName.replace("-thread-", "-émetteur-"));
    try {
      for (int i = 0; i < messageParEmetteur; i++) {
        messagerie.envoyer(Thread.currentThread().getName() + " > " + i);
      }
    } catch (Exception e) {
      Logger.printStackTrace(e);
    } finally {
      Thread.currentThread().setName(threadName);
    }
    return null;
  }
}
class Recepteur implements Callable<Void> {
  @Override
  public Void call() {
    String threadName = Thread.currentThread().getName();
    Thread.currentThread().setName(threadName.replace("-thread-", "-récepteur-"));
    try {
      String message = messagerie.recevoir();
      Logger.println("%s", message);
    } catch (Exception e) {
      Logger.printStackTrace(e);
    } finally {
      Thread.currentThread().setName(threadName);
    }
    return null;
  }
}

final ExecutorService executor = Executors.newCachedThreadPool();
try {
  List<Callable<Void>> taches = new ArrayList<>(emetteur * (messageParEmetteur+1));
  for (int i = 0; i < emetteur; i++) {
    taches.add(new Emetteur());
    for (int j = 0; j < messageParEmetteur; j++) {
      taches.add(new Recepteur());
    }
  }
  executor.invokeAll(taches);
} finally {
  executor.shutdown();
}
//////////////////////////////////////////
  }
}

