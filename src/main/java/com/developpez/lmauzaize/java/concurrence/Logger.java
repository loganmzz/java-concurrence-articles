package com.developpez.lmauzaize.java.concurrence;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;
import java.util.TimeZone;

public class Logger {

  private static final long startTime = System.currentTimeMillis();
  private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
  public static final UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
      printStackTrace(t, e);
    }
    public String toString() {
      return "Logger.printStackTrace(Thread,Throwable)";
    }
  };
  private Logger() {}

  
  public static void println(String msg, Object... params) {
    println(Thread.currentThread(), msg, params);
  }
  public static void println(Thread thread, String msg, Object... params) {
    // Message
    String format = "%tT.%<tL [%-15s] " + msg + "%n";

    // Paramètres
    Object[] args = new Object[2 + (params != null ? params.length : 0)];
    Calendar cal = Calendar.getInstance(UTC);
    cal.setTimeInMillis(System.currentTimeMillis() - startTime);
    args[0] = cal;
    args[1] = Thread.currentThread().getName();
    if (args.length > 2) {
      System.arraycopy(params, 0, args, 2, params.length);
    }
    System.out.printf(format, args);
  }

  public static void printStackTrace(Throwable t) {
    printStackTrace(Thread.currentThread(), t);
  }
  public static void printStackTrace(Thread thread, Throwable t) {
    StringWriter writer = new StringWriter();
    t.printStackTrace(new PrintWriter(writer, true));
    println(thread, "%s%n%s", t, writer);
  }
}
