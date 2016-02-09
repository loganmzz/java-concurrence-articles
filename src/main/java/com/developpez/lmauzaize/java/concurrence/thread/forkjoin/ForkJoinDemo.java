package com.developpez.lmauzaize.java.concurrence.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

import com.developpez.lmauzaize.java.concurrence.Logger;



public class ForkJoinDemo {
public static void main(String[] args) throws Exception {
//////////////////////////////////////////
class Randomizer {
  int[] table = new int[1024 * 1024 * 256];
  void random(int start, int end) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = start; i <= end; i++) {
      table[i] = random.nextInt();
    }
  }
  void randomAll() {
    random(0, table.length - 1);
    table = null;
  }
}
class RandomizerForkJoinAction extends RecursiveAction {
  Randomizer randomizer;
  int start, end;
  RandomizerForkJoinAction() {
    this.randomizer = new Randomizer();
    this.start      = 0;
    this.end        = randomizer.table.length - 1;
  }
  RandomizerForkJoinAction(Randomizer randomizer, int start, int end) {
    this.randomizer = randomizer;
    this.start      = start;
    this.end        = end;
  }

  @Override
  protected void compute() {
    if ((end - start) <= 1024 * 1024) {
      randomizer.random(start, end);
    } else {
      int mid = (this.end + this.start) /2;
      invokeAll(
          new RandomizerForkJoinAction(randomizer, start, mid),
          new RandomizerForkJoinAction(randomizer, mid+1, end)
      );
    }
  }
}

long start;

Logger.println("Monothread - début");
start = System.currentTimeMillis();
new Randomizer().randomAll();
Logger.println("Monothread - fin (%d ms)", System.currentTimeMillis() - start);

Logger.println("Fork/Join - début");
start = System.currentTimeMillis();
new ForkJoinPool().invoke(new RandomizerForkJoinAction());
Logger.println("Fork/Join - fin (%d ms)", System.currentTimeMillis() - start);
//////////////////////////////////////////
}


}
