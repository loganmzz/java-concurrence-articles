package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import com.developpez.lmauzaize.java.concurrence.Logger;


public class PriorityBlockingQueueDemo {
  public static void main(String[] args) throws InterruptedException {
//////////////////////////////////////////
class Élément implements Comparable<Élément> {
  String nom;
  int priorité;
  Élément(String nom, int priorité) {
    this.nom  = nom;
    this.priorité = priorité;
  }
  public int compareTo(Élément that) {
    return this.priorité - that.priorité;
  }
  @Override
  public String toString() {
    return String.format("{%s:%02d}", nom, priorité);
  }
}
PriorityBlockingQueue<Élément> file = new PriorityBlockingQueue<>();
file.offer(new Élément("A", 10));
file.offer(new Élément("B",  0));
file.offer(new Élément("C", 20));
file.offer(new Élément("D", 10));
file.offer(new Élément("E", 10));
file.offer(new Élément("F", 10));
Logger.println("file    =%s", file);
List<Élément> éléments = new ArrayList<>(file.size());
file.drainTo(éléments);
Logger.println("éléments=%s", éléments);
//////////////////////////////////////////
}
}
