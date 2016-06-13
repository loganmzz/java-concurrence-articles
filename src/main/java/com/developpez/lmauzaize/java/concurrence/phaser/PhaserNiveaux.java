package com.developpez.lmauzaize.java.concurrence.phaser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Phaser;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class PhaserNiveaux {

// Gère un "pool" nommé de phaser et trace les actions
// L'état de tous les phasers est tracé après chaque action
static class PhaserLogger {
  Map<String, Phaser> phasers = new LinkedHashMap<>();
  void log() {
    for (Entry<String,Phaser> entry : phasers.entrySet()) {
      Logger.println("{%-10s} phase=%02d parties=%02d", entry.getKey(), entry.getValue().getPhase(), entry.getValue().getRegisteredParties());
    }
    Logger.println("");
  }
  void log(String nom, String action) {
    Logger.println("{%-10s} %s", nom, action);
  }
  Phaser phaser(String nom) {
    return phasers.get(nom);
  }
  void enregistré(String nom, Phaser phaser) {
    log(nom, "création");
    phasers.put(nom, phaser);
    log();
  }

  void créer(String nom) {
    Phaser phaser = new Phaser();
    enregistré(nom, phaser);
  }
  void créer(String parent, String nom) {
    Phaser phaser = new Phaser(phaser(parent));
    enregistré(nom, phaser);
  }
  void register(String nom) {
    log(nom, "register");
    phaser(nom).register();
    log();
  }
  void arriveAndDeregister(String nom) {
    log(nom, "arriveAndDeregister");
    phaser(nom).arriveAndDeregister();
    log();
  }
}
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
PhaserLogger logger = new PhaserLogger();
logger.créer("racine");
logger.register("racine");
logger.créer("racine", "enfant");
logger.register("enfant");
logger.register("enfant");
logger.arriveAndDeregister("enfant");
logger.arriveAndDeregister("enfant");
//////////////////////////////////////////
  }
}
