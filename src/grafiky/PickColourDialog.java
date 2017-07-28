package grafiky;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Tom
 */
public class PickColourDialog extends Dialog implements ActionListener{
    
    Button Red;
    Button Blue;
    Button Green;
    Button Yellow;
    Button Orange;
    Button Magenta;
    
    Color C;
    
    public PickColourDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        
        this.setSize(300, 150);
        this.setLocationRelativeTo(owner);
        this.setResizable(true);
        GridLayout gl = new GridLayout(0, 3);
        this.setLayout(gl);
        
        addWindowListener(new WindowAdapter () {   
            @Override
            public void windowClosing(WindowEvent e) {
                C = null;
                dispose();
            }
        });
        
        C = null;
        
        Red = new Button("");
        Red.addActionListener(this);
        Red.setBackground(Color.RED);
        
        Blue = new Button("");
        Blue.addActionListener(this);
        Blue.setBackground(Color.BLUE);
        
        Green = new Button("");
        Green.addActionListener(this);
        Green.setBackground(Color.GREEN);
        
        Yellow = new Button("");
        Yellow.addActionListener(this);
        Yellow.setBackground(Color.YELLOW);
        
        Orange = new Button("");
        Orange.addActionListener(this);
        Orange.setBackground(Color.ORANGE);
        
        Magenta = new Button("");
        Magenta.addActionListener(this);
        Magenta.setBackground(Color.MAGENTA);
        
        this.add(Red);
        this.add(Blue);
        this.add(Green);
        this.add(Yellow);
        this.add(Orange);
        this.add(Magenta);
        
        
        this.setVisible(true);
    }

  
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Red) {
            C = Color.RED;
            dispose();
        }
        else if (e.getSource() == Blue) {
            C = Color.BLUE;
            dispose();
        }
        else if (e.getSource() == Green) {
            C = Color.GREEN;
            dispose();
        }
        else if (e.getSource() == Yellow) {
            C = Color.YELLOW;
            dispose();
        }
        else if (e.getSource() == Orange) {
            C = Color.ORANGE;
            dispose();
        }
        else if (e.getSource() == Magenta) {
            C = Color.MAGENTA;
            dispose();
        }
    }
    
}
