package grafiky;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AboutFrame extends Frame{
    
    public AboutFrame() {
        super("O programe");
        this.setSize(250, 90);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter () {   
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.setVisible(true);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawString("Grafíky a iné veci", 87, 50);     //toto by sa malo nejako automatizovať
        g.drawString("v 0.3", 120, 70);                 //keďže je to nahardcodene, nezabudnúť na to no...
    } 
}
