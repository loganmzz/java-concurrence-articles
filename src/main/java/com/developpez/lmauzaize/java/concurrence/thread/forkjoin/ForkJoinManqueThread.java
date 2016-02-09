package com.developpez.lmauzaize.java.concurrence.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;

import com.developpez.lmauzaize.java.concurrence.thread.forkjoin.ForkJoinMemeThread.Tâche;

public class ForkJoinManqueThread {
  public static void main(String[] args) throws Exception {
//////////////////////////////////////////
new ForkJoinPool(1).invoke(new Tâche());
//////////////////////////////////////////
}

}
