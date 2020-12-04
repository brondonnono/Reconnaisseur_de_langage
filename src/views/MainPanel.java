/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import beans.Automata;
import com.mxgraph.swing.mxGraphComponent;
import controllers.MainController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import observer.adapter.ObserverAdapter;

/**
 *
 * @author Ndadji Maxime
 */
public class MainPanel extends JPanel{
    private MainController controller;
    private TablePanel thompsonAFN = null, thompsonAFD = null, thompsonAFDM = null;
    private JPanel motPanel = null, graphPanel = null;
    private JLabel labelRes = null, labelTrace = null;
    private Updater updater;
    private JTabbedPane onglets;
    
    public MainPanel(MainController controller) {
        this.controller = controller;
        this.initComponents();
        updater = new Updater();
        controller.getModel().addObserver(updater);
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        onglets = new JTabbedPane();
        this.add(onglets);
    }
    
    /**
     * Mise à jour des automates
     * 
     * @param aut 
     */
    public void update(Automata aut){
        if(thompsonAFN == null){

            thompsonAFN = new TablePanel();
            thompsonAFN.setPreferredSize(new Dimension(500, 250));
            
            onglets.add(thompsonAFN, "Automate Initial");
            onglets.setSelectedComponent(thompsonAFN);
        }
        thompsonAFN.reset(aut);
        onglets.setSelectedComponent(thompsonAFN);
        validate();
        repaint();
    }
    
    public void updateDeterminise(Automata aut){
        if(thompsonAFD == null){

            thompsonAFD = new TablePanel();
            thompsonAFD.setPreferredSize(new Dimension(500, 250));
            
            onglets.add(thompsonAFD, "Automate Fini Déterministe");
        }
        thompsonAFD.reset(aut);
        validate();
        repaint();
    }
    
    public void updateMinimise(Automata aut){
        if(thompsonAFDM == null){

            thompsonAFDM = new TablePanel();
            thompsonAFDM.setPreferredSize(new Dimension(500, 250));
            
            onglets.add(thompsonAFDM, "Automate Fini Déterministe Minimal");
        }
        thompsonAFDM.reset(aut);
        validate();
        repaint();
    }
    
    public void updateGraph(Automata aut){
        if(graphPanel == null){
            
            graphPanel = new JPanel();
            graphPanel.setPreferredSize(new Dimension(700, 250));
            
            onglets.add(graphPanel, "Graphe de l'automate");
            onglets.setSelectedComponent(graphPanel);
        }
        graphPanel.removeAll();
        try {
            mxGraphComponent comp = Parsers.automataToMxGraph(aut);
            graphPanel.add(Parsers.automataToMxGraph(aut));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        validate();
        repaint();
    }
    
    public void updateMot(String mot, boolean result, ArrayList<Object> trace){
        if(motPanel == null){
            
            motPanel = new JPanel();
            motPanel.setPreferredSize(new Dimension(700, 250));
            
            labelRes = new JLabel();
            labelRes.setPreferredSize(new Dimension(700, 50));
            labelRes.setFont(new Font("Times", Font.PLAIN, 17));
            
            labelTrace = new JLabel();
            labelTrace.setPreferredSize(new Dimension(700, 50));
            labelTrace.setFont(new Font("Times", Font.PLAIN, 17));
            
            motPanel.add(labelRes);
            motPanel.add(labelTrace);
            onglets.add(motPanel, "Résultat du test");
            onglets.setSelectedComponent(motPanel);
        }
        String text = result ? "Le mot (" + ((mot == null || mot.trim().isEmpty()) ? "£" : mot) + ") est accepté" : "Le mot (" +
                ((mot == null || mot.trim().isEmpty()) ? "£" : mot) + ") est rejeté";
        labelRes.setText(text);
        labelRes.setToolTipText("<html><b>"+text+"</b></html>");
        if(result){
            labelRes.setIcon(new ImageIcon(getClass().getResource("/ressources/images/success.png")));
            labelTrace.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
            labelRes.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
            labelRes.setForeground(Color.BLUE);
            labelTrace.setForeground(Color.BLUE);
        }else{
            labelRes.setIcon(new ImageIcon(getClass().getResource("/ressources/images/error.png")));
            labelTrace.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.RED));
            labelRes.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.RED));
            labelRes.setForeground(Color.RED);
            labelTrace.setForeground(Color.RED);
        }
        String t = trace.get(0).toString();
        for(int i = 1; i < trace.size(); i++){
            if(i % 2 == 1)
                t += " --"+trace.get(i).toString()+"-> ";
            else
                t += trace.get(i).toString();
        }
        labelTrace.setText(t);
        labelTrace.setToolTipText("<html><b>"+t+"</b></html>");
        onglets.setSelectedComponent(motPanel);
        validate();
        repaint();
    }
    
    public class Updater extends ObserverAdapter{

        @Override
        public void updateAutomata(Automata automata) {
            update(automata);
            updateGraph(automata);
        }

        @Override
        public void updateDeterminisedAutomata(Automata automata) {
            updateDeterminise(automata);
        }

        @Override
        public void updateMinimizedAutomata(Automata automata) {
            updateMinimise(automata);
        }

        @Override
        public void updateTestResult(String mot, boolean result, ArrayList<Object> trace) {
            updateMot(mot, result, trace);
        }
    }
}
