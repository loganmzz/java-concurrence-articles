package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin;

import java.util.concurrent.RecursiveAction;

import com.developpez.lmauzaize.java.concurrence.Logger;

abstract class ForkJoinTâche extends RecursiveAction {
  String  nom;
  ForkJoinTâche() {
    this.nom = getClass().getSimpleName();
  }
  ForkJoinTâche(int n) {
    this.nom = getClass().getSimpleName() + " " + n;
  }
  ForkJoinTâche(String nom) {
    this.nom = nom;
  }
  protected final void compute() {
    Logger.println("[%s] Début", nom);
    calcul();
    Logger.println("[%s] Fin", nom);
  }
  abstract void calcul();
}