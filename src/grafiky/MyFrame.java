/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafiky;

import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Tom
 */
public class MyFrame extends Frame implements ActionListener, ItemListener {
    
    MyCanvas C;
    
    Button buttonAddPointMode;
    Button buttonAddLine;
    Button buttonMovePoint;
    Button buttonChooseStart;
    Button buttonChooseEnd;
    
    Color DijkstraC;
    Color minSkeletonC;
    
    
    
    MenuBar MB;
    
    Menu M1;    //file
    MenuItem newFile;
    MenuItem saveAs;
    MenuItem load;
    
    
    Menu M4;        //Práca s Grafom
    MenuItem algDijkstra;
    MenuItem minSkeleton;
    
    Menu M2;        //options
    MenuItem ColorOptions;
    
            
    Menu M3;        //about
    MenuItem aboutFrame;
    
    CheckboxMenuItem toggleShowDistance;
    
    
    TextField lineValue;
    int mode;
    
    public MyFrame(String name) {
        super(name);
        this.setSize(800, 550);
        this.setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter ()
                                {   public void windowClosing(WindowEvent e) {
                                    
                                    System.exit(0);
                                    }
                                }
        );
        
        
        C= new MyCanvas(this);
        this.add(C);
        
        DijkstraC = Color.RED;
        minSkeletonC = Color.BLUE;
        
        Panel p = new Panel();
        
        int mode = 2;
        buttonAddPointMode = new Button("Mod pridavania bodov");  //mode 1
        buttonAddPointMode.addActionListener(this);
        
        buttonAddLine = new Button("Mod spajania bodov");         //mode 2
        buttonAddLine.addActionListener(this);
        
        lineValue = new TextField("Vaha hrany", 7);
        lineValue.addActionListener(this);
        lineValue.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               lineValue.setText("");
            }
        });
        
        
        buttonMovePoint = new Button("Pohyb bodov");            //mode 3
        buttonMovePoint.addActionListener(this);
        
        buttonChooseStart = new Button("Zaciatocny bod");       //mode 4
        buttonChooseStart.addActionListener(this);
        
        buttonChooseEnd = new Button("Konecny bod");            //mode 5
        buttonChooseEnd.addActionListener(this);
        
        
        
        
        p.add(buttonAddPointMode);
        p.add(buttonAddLine);
        p.add(lineValue);
        p.add(buttonMovePoint);
        p.add(buttonChooseStart);
        p.add(buttonChooseEnd);
        
        
        this.add("South",p);
        
        MB = new MenuBar();
        this.setMenuBar(MB);
        
        /*
        Súbor
        ----------
        Nový Súbor -> newFile
        Uložiť ako -> save
        Nahrať -> load
        */
        M1 = new Menu("Súbor");
        
        newFile = new MenuItem("Nový súbor");
        newFile.addActionListener(this);
        M1.add(newFile);
        saveAs = new MenuItem("Uložiť ako");
        saveAs.addActionListener(this);
        M1.add(saveAs);
        load = new MenuItem("Nahrať");
        load.addActionListener(this);
        M1.add(load);
        
        /*
        Algoritmy
        -------
        Dijkstrov
        Min kostra
        
        */
        M4 = new Menu("Algoritmy");
        algDijkstra = new MenuItem("Dijkstrov algoritmus");
        algDijkstra.addActionListener(this);
        M4.add(algDijkstra);
        minSkeleton = new MenuItem("Minimálna kostra");
        minSkeleton.addActionListener(this);
        M4.add(minSkeleton);
        
        /*
        Nastavenia
        ------
        Farba čiar pre dijstru/min kostru
        Toggle na zobrazenie ohodnotení na hranach
        */
        M2 = new Menu("Nastavenia");
        ColorOptions = new MenuItem("Nastvenia farby čiar");
        ColorOptions.addActionListener(this);
        M2.add(ColorOptions);
        
        toggleShowDistance = new CheckboxMenuItem("Zobraziť ohodnotenia hrán");
        toggleShowDistance.addItemListener(this);
        toggleShowDistance.setState(true);
        M2.add(toggleShowDistance);
        
        
        M3 = new Menu("Pomoc");
        aboutFrame = new MenuItem("O programe");
        aboutFrame.addActionListener(this);
        M3.add(aboutFrame);
        
        
        MB.add(M1);
        MB.add(M4);
        MB.add(M2);
        MB.add(M3);
        

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonAddPointMode) {
            mode = 1;
        }
        else if (e.getSource() == buttonAddLine) {
            mode = 2;
        }
        else if (e.getSource() == buttonMovePoint) {
            mode = 3;
        }
        else if (e.getSource() == buttonChooseStart) {
            mode = 4;
        }
        else if (e.getSource() == buttonChooseEnd) {
            mode = 5;
        }
        else if (e.getSource() == algDijkstra) {
            
            /*
            Ak nie je určený začiatčný bod užívatelom, resp. nie je známy
            upozorníme ho na to bliknutím tlačidla.
            */
            if (C.start == null) {
                Color prev = buttonChooseEnd.getBackground();
                buttonChooseStart.setBackground(Color.red);
                
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } 
                catch (InterruptedException ex1) {

                }
                buttonChooseStart.setBackground(prev);
            }
            
            /*
            Ak nie je určený konečný bod užívatelom, resp. nie je známy
            upozorníme ho na to bliknutím tlačidla.
            */
            if (C.end == null) {
                Color prev = buttonChooseEnd.getBackground();
                buttonChooseEnd.setBackground(Color.red);
                
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } 
                catch (InterruptedException ex1) {

                }
                buttonChooseEnd.setBackground(prev);
            }
            
            /*
            Dijsktrov alg. nutne potrebuje začiatočný a konečný bod,
            spustíme ho iba ak vieme aké body to sú
            */
            if (C.end != null && C.start != null)
                grafiky.Dijkstra.Dijkstra(C, C.points, C.lines, C.start, C.end, DijkstraC);
            
            /*
            Repaint na zobrazenie nového stavu na ploche
            */
            C.repaint();
        }
        
        else if (e.getSource() == minSkeleton) {
            
            /*
            Pre min. kostru potrebujeme vedieť začiatočý bod, ak nie je určený
            upozorníme užívateľa bliknutím.
            */
            if (C.start == null) {
                Color prev = buttonChooseEnd.getBackground();
                buttonChooseStart.setBackground(Color.red);
                
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } 
                catch (InterruptedException ex1) {

                }
                buttonChooseStart.setBackground(prev);
            }
            
            /*
            Keďže vo výsledku a pri práci nepotrbujeme end point,
            prejdeme všetky body a ak je niektorý konečný (tj červený)
            dáme ho naspať na čierno + nastavíme že už nepoznáme C.end
            
            Porovnávajú sa zložky RGB (červená)m konzisentnejšie ako
            keď sa porovnávali tie dva objekty, z nahraného súboru je farba
            C.pointu nie rovnakej adresy ako Color.RED
            */
            for (int i = 0; i < C.points.size(); i++) {
                if (C.points.get(i).getC().getRed() == Color.RED.getRed()) {
                    C.points.get(i).setC(Color.BLACK);
                    C.end = null;
                }
            }
            
            if (C.start != null)
                MinSkeleton.findMinSkeleton(C.start, C, minSkeletonC);
            
            C.repaint();
            
        }
        
        /*
        Vyčistí okno, zmaže všetky body a čiary
        a zmaže si aj start a end
        */
        else if (e.getSource() == newFile) {
            C.lines.clear();
            C.points.clear();
            C.start = null;
            C.end = null;
            C.repaint();
        }
        
        /*
        Ukladanie, najprv pointy, potom lajny.
        */
        else if (e.getSource() == saveAs) {
            FileDialog FD = new FileDialog(this, "Save as",FileDialog.SAVE);
            FD.setVisible(true);
          
            if(FD.getFile() != null) {
                try {
                    FileOutputStream fos=new FileOutputStream(FD.getDirectory() + FD.getFile());
                    try (ObjectOutputStream os = new ObjectOutputStream(fos)) {
                        os.writeObject(C.points);
                        os.writeObject(C.lines);
                    }
                }
                catch (IOException ex) {
                    
                    System.out.println(ex);
                }
            }
            
            
            
        }
        /*
        Loaduje, najprv pointy potom lajny, zaleží asi na poradí v save (hore)
        */
        else if (e.getSource() == load) {
            
            FileDialog FD = new FileDialog(this, "Load",FileDialog.LOAD);
            FD.setVisible(true);

            if (FD.getFile() != null) {
                C.lines.clear();
                C.points.clear();
                try {
                FileInputStream fis=new FileInputStream(FD.getDirectory() + FD.getFile());
                ObjectInputStream is = new ObjectInputStream(fis);
                    try {
                        C.points = (LinkedList<MyPoint>)is.readObject();
                        C.lines = (LinkedList<Line>)is.readObject();
                        
                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex);
                    }
                
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }
                
                /*
                Kontrola, či niektorý bod nie je zelený (štart) alebo červený (end)
                
                Ak taký nájdeme treba systému povedať, ktorý to je;
                
                Opať sa porovnáva po zložkách, pretože takto sa porovnávajú dva int-y
                a 255 = 255 vždy a nie adresy objektov ktoré nejako blbli. Podozrenie je
                že pri nahrávaní tie farby už nie sú rovnaké objekty. Resp. sú
                rovnaké farebne ale objekty sú to dva...
                */
                for (int i = 0; i < C.points.size(); i++) {
                    if (C.points.get(i).getC().getGreen() == Color.GREEN.getGreen()) {
                        C.start = C.points.get(i);
                    }
                    else if (C.points.get(i).getC().getRed() == Color.RED.getRed()) {
                        C.end = C.points.get(i);
                    }
                }
                
                C.repaint();
            }

        } 

        else if (e.getSource() == aboutFrame) {
            
            AboutFrame af = new AboutFrame();
        }
        else if (e.getSource() == ColorOptions) {
            ColorDialog CD = new ColorDialog(this, "Vyber farby", true);
        }
        
    }
    
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        
        if (e.getSource() == toggleShowDistance) {

            if (!toggleShowDistance.getState()) {
                toggleShowDistance.setState(false);
            }
            else {
                toggleShowDistance.setState(true);
            }

            C.repaint();
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public TextField getLineValue() {
        return lineValue;
    }

    public void setLineValue(TextField lineValue) {
        this.lineValue = lineValue;
    }

    public Color getDijkstraC() {
        return DijkstraC;
    }

    public void setDijkstraC(Color DijkstraC) {
        this.DijkstraC = DijkstraC;
    }

    public Color getMinSkeletonC() {
        return minSkeletonC;
    }

    public void setMinSkeletonC(Color minSkeletonC) {
        this.minSkeletonC = minSkeletonC;
    }

}
