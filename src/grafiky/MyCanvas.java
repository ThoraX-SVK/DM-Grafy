/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafiky;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Tom
 */
public class MyCanvas extends DoubleBuffer implements MouseListener, MouseMotionListener{

    MyFrame F;
    LinkedList<MyPoint> points;
    LinkedList<Line> lines; 
    final int circleSize = 20;
    
    boolean drawHelpLine;
    MyPoint lineStart;
    MyPoint lineEnd;
    
    MyPoint toMove;
    
    MyPoint start;
    MyPoint end;
    
    public MyCanvas(MyFrame F) {
        
        this.F = F;
        this.drawHelpLine = false;
        this.lineStart = null;
        this.lineEnd = null;
        
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        
        points = new LinkedList<>();
        lines = new LinkedList<>();
        
        
    }
    
    @Override
    public void paintBuffer(Graphics g) {
        /*
        Postupne vkreslí všetky lajny
        */
        if (lines.size() != 0) {
            for (int i = 0; i < lines.size(); i++) {
                
                g.setColor(lines.get(i).getC());   //nastavíme farbu lajny, zmenená mohlůa byť napr
                                                   // keď ukazuje cestu Dijkstra
                g.drawLine(lines.get(i).S.x, lines.get(i).S.y, lines.get(i).E.x, lines.get(i).E.y);
                
                /*
                Vykresluje vzdialenosti iba ak to má užívatel zakliknuté
                v Nastavenia->Zobraziť ohodnotenia
                */
                if (F.toggleShowDistance.getState()) {
                g.setColor(Color.BLACK);
                g.drawString(Double.toString(lines.get(i).getLineValue()), (lines.get(i).S.x + lines.get(i).E.x)/2, (lines.get(i).S.y + lines.get(i).E.y)/2);
                }
            }
        }
        
        if (drawHelpLine) {
            //pomocná hrana vykreslovaná keď užívatel ťahá myš z bodu ale ešte
            //neprišiel do konečného resp. nepustil drag z myši
            g.drawLine(lineStart.x, lineStart.y, lineEnd.x, lineEnd.y); 
            
        }
        
        /*
        Vypíše všetky pointy v ich farb (farba sa mení pre štart a koniec body)
        */
        if (points.size() != 0) {
            for (int i = 0; i < points.size(); i++) {
                g.setColor(points.get(i).getC());
                g.fillOval(points.get(i).x-circleSize/2, points.get(i).y-circleSize/2, circleSize, circleSize);
            }
        }
        
    }
    
    /**
     * Vráti index v poli bodov na ktorom je bod najbližśí ku kliku
     * Ak spĺňa podmienku že vzdialenosť bod-klik je menšia ako maxdist
     * 
     * Vracia -1 ak taký bod neexistuje
     *
     * @param e
     * @param maxdist
     * @return index najbližšieho bodu v LinkedListe pointov, -1 ak nenašiel žiadny
     */
    private int closestClickedPointIndex(Point e, int maxdist) {
       
        double actualDist;
        double minDist = Double.MAX_VALUE;
        int foundIndex = -1;
        for (int i = 0; i < points.size(); i++) {
            actualDist = Point.distance(points.get(i).x, points.get(i).y, e.getX(), e.getY());

            if (actualDist < minDist && actualDist < maxdist) {
                foundIndex = i;
                minDist = actualDist;
            }
        }    
        
        return foundIndex;
    }

    @Override
    /*
    Useless :D
    */
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        /*
        Toto značí, že sme v aplikácii v móde
        pridávania/uberania bodov
        */
        if (F.getMode() == 1) {
            
            /*
            Ak to bol klik lavým tlačítkom, pridáme na dané súradnice bod
            */
            if(e.getButton() == MouseEvent.BUTTON1) { 
                points.add(new MyPoint(e.getPoint()));
                repaint();
            }
            /*
            Ak to bol klik pravým tlačítkom
            */
            else if (e.getButton() == MouseEvent.BUTTON3) {
                //Zistíme index bodu na ktorý sme klikli, -1 ak na žiadny
                int toDeleteIndex = closestClickedPointIndex(e.getPoint(),circleSize);
                
                //našli sme bod na zmazanie
                if(toDeleteIndex != -1) {
                    /*
                    Kontrola aby sa s bodom zmazali aj všetky hrany ktoré
                    s ním incidujú
                    
                    somethingWasDeleted je pred while true aby to do neho vstupilo
                    
                        Nasledne je predpoklad, že žiadnu lajnu nezmažem, pretože
                        bod už s nijakou neinciduje
                    
                        Prejdú sa všetky lajny, vždy sa kontroluje, %ci bod ktorý chcem zmazať
                        nebol začiatok/koniec hrany.
                    
                        Ak áno, lajnu zmažem a idem znova
                    
                    Na konci ešte skontrolujeme %ci zmazávaný bod nie je náhodou štart/ciel
                    ak áno, dáme v systéme na miesto start/ciel null
                    
                    Nakoniec ho zmažeme
                    
                    */
                    boolean somethingWasDeleted = true;    
                    while(somethingWasDeleted) {
                        somethingWasDeleted = false;
                        
                        for (int i = 0; i < lines.size(); i++) {
                            if (lines.get(i).E == points.get(toDeleteIndex)) {
                                lines.remove(i);
                                somethingWasDeleted = true;
                                break;
                            }
                            if (lines.get(i).S == points.get(toDeleteIndex)) {
                                lines.remove(i);
                                somethingWasDeleted = true;
                                break;
                            }
                        }
                        
                    }
                    
                    if (points.get(toDeleteIndex) == start)
                        start = null;
                    if(points.get(toDeleteIndex) == end)
                        end = null;
                    
                    points.remove(toDeleteIndex);
                }

                repaint();
            }
        }
        /*
        Mód robenia čiar medzi dvoma bodmi
        */
        else if (F.getMode() == 2) {
            /*
            Ak sme stlačili lavé tlačidlo
            */
            if (e.getButton() == MouseEvent.BUTTON1) {
                //Skontrolujeme či sme klikli na bod, čiazu možeme ťahať iba z bodu
                int toChooseIndex = closestClickedPointIndex(e.getPoint(),circleSize);
                
                //ak sme klikli na bod
                if (toChooseIndex != -1) {
                lineStart = points.get(toChooseIndex); //nastavíme ho ako štartovací bod
                                                        //dočasnej čiary
                drawHelpLine = true;    //povolíme vykreslovanie pomocnej čiary                       
                }
                else {
                    drawHelpLine = false; //neklikli sme na bod, čiara sa kresliť nebude
                }
            } 
            else if (e.getButton() == MouseEvent.BUTTON3) {
                
                //nejako dokodit mazanie hran?
                /*
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                ?
                */
                
                
            }
            
        }
        /*
        Sme v móde chytenia bodu a jeho následného ťahania
        
        Užívatel klikne na bod, čím začne prces ťahania bodu
        
        toMove je bod s ktorým budeme pracovať v mouseDRagged a mouseReleased,
        je to ťahaný bod
        */
        else if (F.getMode() == 3) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int toChooseIndex = closestClickedPointIndex(e.getPoint(), circleSize);
                
                if (toChooseIndex != -1) {
                    toMove = points.get(toChooseIndex);
                }
                        
            }
        }
        
        /*
        Označenie bodu na zeleno, označovanie štrtu
        */
        else if (F.getMode() == 4) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                
                int toChooseIndex = closestClickedPointIndex(e.getPoint(), circleSize);
                
                if (toChooseIndex != -1) {
                    
                    /*
                    Všetky pointy ktoré sú doteraz zelené, sa prefarbia na čierno
                    Tj zmažeme doterajší štart ak nejaký je
                    */
                    for (int i = 0; i < points.size(); i++) {
                        if (points.get(i).getC().getGreen() == Color.GREEN.getGreen()) {
                            points.get(i).setC(Color.BLACK);
                        }
                    }
                    
                    points.get(toChooseIndex).setC(Color.GREEN);
                    start = points.get(toChooseIndex);
                    repaint();
                }
            }
        }
        
        /*
        Rovnako ako hore, iba s koncom
        */
        else if (F.getMode() == 5) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                
                int toChooseIndex = closestClickedPointIndex(e.getPoint(), circleSize);
                
                if (toChooseIndex != -1) {
                    
                    for (int i = 0; i < points.size(); i++) {
                        if (points.get(i).getC().getRed() == Color.RED.getRed()) {
                            points.get(i).setC(Color.BLACK);
                        }
                    }
                    
                    points.get(toChooseIndex).setC(Color.RED);
                    end = points.get(toChooseIndex);
                    repaint();
                }
            }
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
        /*
        Ak som v móde kreslenia čiary, a zrovna kreslím nejakú čiaru
        */
        if (F.mode == 2 && drawHelpLine) {
            
            int indexLineEndPoint = closestClickedPointIndex(e.getPoint(),2*circleSize);
            /*
            Skontrolujememe, či miesto kde sme pusili myš je bod A ZÁROVEŇ či daný
            bod nie je začiatkom čiary aby sa nevytvorila čiara
            A->A (do samého seba)
            
            Ak je aspoň jedno pravda, neskončili sme čiaru na dobrom mieste a teda
            skončíme s jej vykreslovaním a upraceme jej atribúy
            */
            if (indexLineEndPoint == -1 || (lineStart == points.get(indexLineEndPoint))) {
                drawHelpLine = false;   //už ju nekreslíme
                lineStart = null;       //neexistuje jej začiatčný bod
                lineEnd = null;         //ani jej konečný bod
                repaint();
            }
            /*
            Úspešne sme pustili myš nad dobrým bodom
            */
            else {
                lineEnd = points.get(indexLineEndPoint);    //získame konečný bod
                /*
                Zistíme, či daná čiara už neexistuje pre dané dva body
                Ak áno nepridávameju druhýkrát
                
                Pre každú lajnu skontrolujeme či existuje lajna ktorá
                má začiatok a koniec zhodný ako ako náš začiatok a koniec alebo
                či nemá koniec v našom začiatku a začiatok v našom
                konci (tj, krslili by sme v potismere)
                
                */
                boolean toAdd = true;                       

                for (int i = 0; i< lines.size(); i++) {
                    if (lines.get(i).S == lineStart &&
                        lines.get(i).E == lineEnd) {
                        toAdd = false;
                        break;
                    }
                    if (lines.get(i).S == lineEnd &&
                        lines.get(i).E == lineStart) {
                        toAdd = false;
                        break;
                    }
                }
                
                /*
                Ak lajna ešte neexistuje, ideme ju pridať
                */
                if (toAdd) {
                    /*
                    Snažíme sa zistiť, aká hodnota je v políčku lineValue kde uźívatel
                    zadáva ohodnotenie hrany
                    */
                    try {
                        //ak užívatel zadal číslo menšie alebo rovné nule, neplatné
                        if (Double.parseDouble(F.getLineValue().getText()) <= 0) {
                            F.getLineValue().setBackground(Color.red);  //červená
                            F.getLineValue().setText("Zadaj kladne cislo");
                            //počkáme 200ms aby vznikol efekt bliknutia
                            try {
                                TimeUnit.MILLISECONDS.sleep(200);
                            } catch (InterruptedException ex1) {

                            }
                                
                            F.getLineValue().setBackground(null); //nastavíme na beznu farbu
                        }
                        
                        //ak užívatel zadal dobré ohodnotenie hrany, lajnu zapíšeme
                        else {
                        lines.add(new Line(lineStart, lineEnd,Double.parseDouble(F.getLineValue().getText())));
                        }
                    }
                    
                    /*
                    Prípad že by sa objavil v lineValue text, resp. Parserby nedokázal urobiť
                    z toho čo je v políčku číslo
                    */
                    catch (NumberFormatException ex) {
                        F.getLineValue().setBackground(Color.red);
                        F.getLineValue().setText("Zadaj vahu hrany");
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException ex1) {
                            
                        }
                        
                        F.getLineValue().setBackground(null);
                    }
                    
                }
                
                //lajnu sme pridali/resp zlyhali
                //vyčistíme pomocnú hranu
                drawHelpLine = false;
                lineStart = null;
                lineEnd = null;
                repaint();

            }
        }
        /*
        Pohyboval som bodom a pustil som myš
        teda už ničím nepohybujeme, toMove dame na null
        */
        if (F.mode == 3) {
            toMove = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*
        Ak dragujem a som v móde spájania dvoch bodov
        A mám začiatčný bod a teraz sa snažím dostať ku konečnému
        bodu
        */
        if (F.getMode() == 2 && drawHelpLine) {
            lineEnd = new MyPoint(e.getPoint());   
            repaint();
        }
        
        /*
        Ak pohybujeme bod, a nejaký máme aj vybraný
        meníme mu súradnice na tie
        kde máme práve myš    
        */
        if (F.getMode() == 3 && toMove != null) {
            toMove.x = e.getX();
            toMove.y = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
    
    
}
