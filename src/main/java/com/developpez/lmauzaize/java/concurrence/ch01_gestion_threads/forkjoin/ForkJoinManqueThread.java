package com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin;

import java.util.concurrent.ForkJoinPool;

import com.developpez.lmauzaize.java.concurrence.ch01_gestion_threads.forkjoin.ForkJoinMemeThread.Tâche;

public class ForkJoinManqueThread {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
new ForkJoinPool(1).invoke(new Tâche());
//////////////////////////////////////////
}

}
