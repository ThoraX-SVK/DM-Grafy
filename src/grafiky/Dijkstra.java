package grafiky;

import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Tom
 */
public class Dijkstra {
    
    public static void Dijkstra (MyCanvas C, LinkedList<MyPoint> points, LinkedList<Line> lines, MyPoint start, MyPoint end, Color lineC) {
        
        preparePoints(points);
        prepareLines(lines);
        
        
        start.setDistToStart(0);
        start.setSeen(true);
        start.setVisited(false);
        
    Comparator<MyPoint> comparator = new Comparator<MyPoint>() {
      
        @Override
        public int compare(MyPoint o1, MyPoint o2) {

            if (o1.getDistToStart() < o2.getDistToStart()) {
                return -1;
            }
            if (o1.getDistToStart() > o2.getDistToStart()) {
                return 1;
            }

            return 0;
            }
        };
    
        PriorityQueue<MyPoint> PQ = new PriorityQueue(comparator);
        
        PQ.add(start);
        
        while(PQ.size() > 0) {
            MyPoint current = PQ.poll();
            
            if (current == end) {
                
                findStartRecursive(end, start, lines, lineC);
                break;
            }
            
            for (int i = 0; i < lines.size(); i++) {
                
                if (lines.get(i).S == current) {    // S - staring point of a line
                    MyPoint tmpPoint = lines.get(i).E;
                    Line tmpLine = lines.get(i);
                    
                    pointRoutine(PQ, tmpPoint, current, tmpLine);
                }
                
                //else if OR if?
                else if (lines.get(i).E == current) { // E -end point of line
                    MyPoint tmpPoint = lines.get(i).S;
                    Line tmpLine = lines.get(i);
                    
                    pointRoutine(PQ, tmpPoint, current, tmpLine);     
                }
                
            }
            current.setVisited(true);
        }
    }

    private static void findStartRecursive(MyPoint point, MyPoint start, LinkedList<Line> lines, Color lineC) {
        
        if (point == start) {
            return;
        }
        
        for (int i = 0; i < point.getParrents().size(); i++) {
            findStartRecursive(point.getParrents().get(i),start,lines,lineC);
            
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).E == point && lines.get(j).S == point.getParrents().get(i)) {
                    lines.get(j).setC(lineC);
                }
                if (lines.get(j).S == point && lines.get(j).E == point.getParrents().get(i)) {
                    lines.get(j).setC(lineC);
                }
            }
        }
    }
    
    private static void preparePoints(LinkedList<MyPoint> points) {
        
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setDistToStart(Double.MAX_VALUE);
            points.get(i).getParrents().clear();
            points.get(i).setVisited(false);
            points.get(i).setSeen(false);  
        }
        
    }
    
    private static void prepareLines(LinkedList<Line> lines) {
        
        for (int i = 0; i < lines.size(); i++) {
           lines.get(i).setC(Color.BLACK);
        }
        
    }
    
    private static void pointRoutine(PriorityQueue<MyPoint> PQ, MyPoint tmpPoint, MyPoint current, Line tmpLine) {
        
        if (!tmpPoint.isVisited()) {

            if (!tmpPoint.isSeen()) {
                PQ.remove(tmpPoint);
                PQ.add(tmpPoint);
                tmpPoint.setSeen(true);
            }

            if (tmpPoint.getDistToStart() > current.getDistToStart() + tmpLine.getLineValue()) {
                tmpPoint.setDistToStart(current.getDistToStart() + tmpLine.getLineValue());
                tmpPoint.getParrents().clear();
                tmpPoint.getParrents().add(current);
                PQ.remove(tmpPoint);
                PQ.add(tmpPoint);
            }
            else if (tmpPoint.getDistToStart() == current.getDistToStart() + tmpLine.getLineValue()) {
                tmpPoint.getParrents().add(current);  
            }

        }
    }
    
    
}
