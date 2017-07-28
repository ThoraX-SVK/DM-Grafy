package grafiky;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Tom
 */
public class MyPoint extends Point implements Serializable{
    
    LinkedList<MyPoint> neighborMyPoints;
    Color C;
    
    
    
    boolean visited;
    boolean seen;
    double distToStart;
    LinkedList<MyPoint> parrents;
    
    
    public MyPoint(Point p) {
        super(p);
        C = Color.BLACK;
        parrents = new LinkedList<>();
        
        neighborMyPoints = new LinkedList<>();
     
    }
    
    public LinkedList<MyPoint> getNeighborMyPoints() {
        return neighborMyPoints;
    }

    public void setNeighborMyPoints(LinkedList<MyPoint> neighborMyPoints) {
        this.neighborMyPoints = neighborMyPoints;
    }

    public Color getC() {
        return C;
    }

    public void setC(Color C) {
        this.C = C;
    }

    public double getDistToStart() {
        return distToStart;
    }

    public void setDistToStart(double distToStart) {
        this.distToStart = distToStart;
    }

    public LinkedList<MyPoint> getParrents() {
        return parrents;
    }

    public void setParrents(LinkedList<MyPoint> parrents) {
        this.parrents = parrents;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    

}
