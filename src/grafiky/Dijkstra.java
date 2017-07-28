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
        
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setDistToStart(Double.MAX_VALUE);
            points.get(i).getParrents().clear();
            points.get(i).setVisited(false);
            points.get(i).setSeen(false);  
        }
        
        for (int i = 0; i < lines.size(); i++) {
           lines.get(i).setC(Color.BLACK);
        }
        
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
                
                FindStartRecursive(end, start, lines,lineC);
                break;
            }
            
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).S == current) {
                    MyPoint tmp = lines.get(i).E;

                    if (!tmp.isVisited()) {
                        
                        if (!tmp.isSeen()) {
                            PQ.remove(tmp);
                            PQ.add(tmp);
                            tmp.setSeen(true);
                        }
                        
                        if (tmp.getDistToStart() > current.getDistToStart() + lines.get(i).getLineValue()) {
                            tmp.setDistToStart(current.getDistToStart() + lines.get(i).getLineValue());
                            tmp.getParrents().clear();
                            tmp.getParrents().add(current);
                            PQ.remove(tmp);
                            PQ.add(tmp);
                        }
                        
                        else if (tmp.getDistToStart() == current.getDistToStart() + lines.get(i).getLineValue()) {
                            tmp.getParrents().add(current);  
                        }
                        
                    }

                }
                if (lines.get(i).E == current) {
                    MyPoint tmp = lines.get(i).S;

                    if (!tmp.isVisited()) {
                        
                        if (!tmp.isSeen()) {
                            PQ.remove(tmp);
                            PQ.add(tmp);
                            tmp.setSeen(true);
                        }
                        
                        if (tmp.getDistToStart() > current.getDistToStart() + lines.get(i).getLineValue()) {
                            tmp.setDistToStart(current.getDistToStart() + lines.get(i).getLineValue());
                            tmp.getParrents().clear();
                            tmp.getParrents().add(current);
                            PQ.remove(tmp);
                            PQ.add(tmp);
                        }
                        
                        else if (tmp.getDistToStart() == current.getDistToStart() + lines.get(i).getLineValue()) {
                            tmp.getParrents().add(current);  
                        }
                    }
                }
            }
            current.setVisited(true);
        }
    }

    private static void FindStartRecursive(MyPoint point, MyPoint start, LinkedList<Line> lines, Color lineC) {
        
        if (point == start) {
            return;
        }
        
        for (int i = 0; i < point.getParrents().size(); i++) {
            FindStartRecursive(point.getParrents().get(i),start,lines,lineC);
            
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
    
    
}
