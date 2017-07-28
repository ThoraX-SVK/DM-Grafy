/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafiky;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Tom
 */
public class ColorDialog extends Dialog implements ActionListener{

    MyFrame F;
    Button dijkstraC;
    Button minSkeletonC;
    
    public ColorDialog(MyFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.setSize(275, 65);
        this.setLocationRelativeTo(owner);
        this.setLayout(new FlowLayout());
        this.setResizable(false);
        
        addWindowListener(new WindowAdapter () {   
            public void windowClosing(WindowEvent e) {

                dispose();
            }
        });

        F = owner;
        
        dijkstraC = new Button("Farba Dijkstra");
        dijkstraC.addActionListener(this);
        minSkeletonC = new Button("Farba minimálnej kostry");
        minSkeletonC.addActionListener(this);
        
        Panel P = new Panel();

        P.add(dijkstraC);
        P.add(minSkeletonC);
        
        this.add("South",P);
       
        
       this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == dijkstraC) {
            
            PickColourDialog PCD = new PickColourDialog(F, "Vyber si farbu", true);
            if (PCD.C != null)
                F.setDijkstraC(PCD.C);
            
        }
        else if (e.getSource() == minSkeletonC) {
            PickColourDialog PCD = new PickColourDialog(F, "Vyber si farbu", true);
            if (PCD.C != null)
                F.setMinSkeletonC(PCD.C);  
            
        }

    }
    
    
}
