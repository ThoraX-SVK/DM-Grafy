/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafiky;

import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Tom
 */
public class MinSkeleton {
    
    static LinkedList<MyPoint> notSeen;
    static LinkedList<MyPoint> adepts;
    static LinkedList<MyPoint> done;
    
    static LinkedList<Line> goodLines;
    static PriorityQueue<Line> consideredLines;
    
    public static void findMinSkeleton(MyPoint start, MyCanvas C, Color lineC) {
       
        for (int i = 0; i < C.lines.size(); i++) {
            C.lines.get(i).setC(Color.BLACK);
        }

        
       notSeen = new LinkedList<>();
       adepts = new LinkedList<>();
       done = new LinkedList<>();
       
       goodLines = new LinkedList<>();
       
        Comparator<Line> comparator = new Comparator<Line>() {
           @Override
           public int compare(Line o1, Line o2) {
               if (o1.lineValue < o2.lineValue)
                   return -1;
               else if (o1.lineValue > o2.lineValue)
                   return 1;
               else 
                   return 0;  
           }
       };
       consideredLines = new PriorityQueue<>(comparator);
       
       adepts.clear();
       done.clear();
       
       
       
       done.add(start);
       
               
       while (done.size() != C.points.size()) {
           
           consideredLines.clear();
           
           for (int i = 0; i < done.size(); i++) {
               
               for (int j = 0; j < C.lines.size(); j++) {
                   
                   if (C.lines.get(j).S == done.get(i) && !done.contains(C.lines.get(j).E)) {
                       if (!adepts.contains(C.lines.get(j).E))
                        adepts.add(C.lines.get(j).E);
                       
                       consideredLines.add(C.lines.get(j));
                   }
                   if (C.lines.get(j).E == done.get(i) && !done.contains(C.lines.get(j).S)) {
                       if (adepts.contains(C.lines.get(j).S))
                        adepts.add(C.lines.get(j).S);
                       
                       consideredLines.add(C.lines.get(j));
                   } 
               }
           }
           
           Line bestLine = consideredLines.poll();
           
           if (bestLine == null) {
              break;
           }
           
           if (done.contains(bestLine.E)) {
               done.add(bestLine.S);
               adepts.remove(bestLine.S);
           }
           else if (done.contains(bestLine.S)) {
               done.add(bestLine.E);
               adepts.remove(bestLine.E);
           }
           
           goodLines.add(bestLine);
           
       }
 
        for (int i = 0; i < goodLines.size(); i++) {
            goodLines.get(i).setC(lineC);
        }

    }
    
    
    
}
