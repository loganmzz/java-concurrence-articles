package com.developpez.lmauzaize.java.concurrence.ch02_synchronisation.api_native;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class Volatile {
  public static void main(String[] args) {
//////////////////////////////////////////
Runnable tache = new Runnable() {
  private volatile int compteur = 0;

  @Override
  public void run() {
    int valeur = compteur;
    while (compteur <= 10) {
      switch (Thread.currentThread().getName()) {
        case "Lecteur":
          int temp = compteur;
          if (temp != valeur) {
            Logger.println("Lecture : %02d -> %02d", valeur, compteur);
            valeur = temp;
          }
          break;
        case "Ecrivain":
          valeur = compteur++;
          Logger.println("Ecriture: %02d -> %02d", valeur, compteur);
          Thread.yield();
          break;
      }
    }
  }
};
new Thread(tache, "Lecteur").start();
new Thread(tache, "Ecrivain").start();
//////////////////////////////////////////
  }
}
