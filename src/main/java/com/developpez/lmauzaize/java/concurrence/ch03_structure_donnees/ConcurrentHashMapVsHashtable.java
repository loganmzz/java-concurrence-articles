package com.developpez.lmauzaize.java.concurrence.ch03_structure_donnees;

import java.util.TimeZone;

import com.developpez.lmauzaize.java.concurrence.Logger;

public class ConcurrentHashMapVsHashtable {

public static void main(String[] args) throws Exception {
//////////////////////////////////////////
TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
ConcurrentHashMapDemo demo = new ConcurrentHashMapDemo();

demo.tache(    3, demo.ajout(2_000_000));
demo.bench();
Logger.println("");

demo.tache(   30, demo.ajout( 200_000));
demo.bench();
Logger.println("");

demo.tache(  300, demo.ajout(  20_000));
demo.bench();
Logger.println("");

demo.tache(    3, demo.ajout(2_000_000));
demo.tache(1_000, demo.lecture(100));
demo.bench();
Logger.println("");
//////////////////////////////////////////
}

}
