/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafiky;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Tom
 */
public class Line implements Serializable {
    
    MyPoint S;  //Starting point
    MyPoint E;  //Ending point
    double lineValue;
    Color C;
    
    public Line(MyPoint S, MyPoint E, double val) {
        this.S = S;
        this.E = E;
        this.lineValue = val;
        this.C = Color.BLACK;
    }

    public double getLineValue() {
        return lineValue;
    }

    public void setLineValue(double lineValue) {
        this.lineValue = lineValue;
    }

    public Color getC() {
        return C;
    }

    public void setC(Color C) {
        this.C = C;
    }
    
    
}
