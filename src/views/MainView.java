/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.MainController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 * Fenêtre principale qui est affichée
 *
 * @author Ndadji 
 */
public class MainView extends JFrame{
    private MainController controller;
    private RegexPanel regexPanel;
    private MainPanel mainPanel;
    private JToolBar toolBar;
//    private JButton details;
    private JMenuBar menuBar;
    private ActionListener listener;
//    private Automata thompson;
//    private Updater updater;
//    private JMenuItem detailsItem;

    private MainView(MainController controller) {
        this.controller = controller;
        
        this.setTitle("G_TEAM_PROJECT");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setMinimumSize(new Dimension(1000, 600));
        this.setLocationRelativeTo(null);
        this.setIconImage((new ImageIcon(getClass().getResource("/ressources"
                + "/images/logo.png"))).getImage());
        this.initComponents();
    }
    
    private void initComponents() {
        // Initialise les écouteurs
        this.initListeners();
        
        // Initialise les panneaux
        this.initPanels();
        
        // Initialise les menus
        this.initMenus();
        
        // Initialise les barres d'outils
        this.initToolBar();
        
        // Initialise l'objet de mise à jour
      /*  updater = new Updater();
        controller.getModel().addObserver(updater);*/
    }
    
    private void initListeners() {
        listener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if(command.equals("details")){
                    return;
                }
                if(command.equals("fermer")){
                    System.exit(0);
                }
                if(command.equals("infos")){
                    
                }
            }
            
        };
    }

    private void initPanels() {
        regexPanel = new RegexPanel(controller);
        regexPanel.setPreferredSize(new Dimension(200, 500));
        this.add(regexPanel, BorderLayout.WEST);
        
        mainPanel = new MainPanel(controller);
        this.add(mainPanel);
    }

    private void initMenus() {
        menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY.brighter()));
        
        JMenu menu = new JMenu("Fichier");
        menu.setFont(new Font("Times", Font.PLAIN, 15));
        
        JMenuItem item = new JMenuItem("Accueil");
        item.setFont(new Font("Times", Font.PLAIN, 15));
        item.addActionListener(listener);
        menu.add(item);
        
        item = new JMenuItem("Fermer");
        item.setActionCommand("fermer");
        item.setFont(new Font("Times", Font.PLAIN, 15));
        item.addActionListener(listener);
        menu.add(item);
        
        menuBar.add(menu);
        /*
        menu = new JMenu("Général");
        menu.setFont(new Font("Times", Font.PLAIN, 15));
        
        item = new JMenuItem("Informations");
        item.setActionCommand("infos");
        item.setFont(new Font("Times", Font.PLAIN, 15));
        item.addActionListener(listener);
        menu.add(item);
        
        item = new JMenuItem("Aide");
        item.setActionCommand("help");
        item.setFont(new Font("Times", Font.PLAIN, 15));
        item.addActionListener(listener);
        menu.add(item);
        
        menuBar.add(menu);
        */
        this.setJMenuBar(menuBar);
    }
    
    // Construction de la barre d'outils
    private void initToolBar() {
        // Petit style pour l'animation des composants (boutons, labels, ...) au passage de la souris.
        MouseListener mouseListener = new MouseAdapter(){
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(((JComponent)e.getSource()).isEnabled())
                        ((JComponent)e.getSource()).setBackground(new Color(21, 168, 180));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ((JComponent)e.getSource()).setBackground(null);
                }
                
        };
        toolBar = new JToolBar();
        toolBar.setBackground(new Color(245, 245, 250));
        JButton button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/ressources/images/home.png")));
        button.setPreferredSize(new Dimension(38, 38));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(null);
        toolBar.add(button);
        
        button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/ressources/images/info.png")));
        button.setPreferredSize(new Dimension(38, 38));
        button.setActionCommand("infos");
        button.addActionListener(listener);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(null);
        toolBar.add(button);
        
        button = new JButton();
        button.setIcon(new ImageIcon(getClass().getResource("/ressources/images/help.png")));
        button.setPreferredSize(new Dimension(38, 38));
        button.setActionCommand("help");
        button.addActionListener(listener);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(null);
        toolBar.add(button);
        
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY.brighter()));
        
        this.add(toolBar, BorderLayout.NORTH);
    }
    
    /**
     * Mise à jour de l'automate
     * 
     * @param automata 
     */
    /*public void update(Automata automata){
        thompson = automata;
    }
    
    public class Updater extends ObserverAdapter{

        @Override
        public void updateAutomata(Automata automata) {
            update(automata);
        }

        @Override
        public void updateDeterminisedAutomata(Automata automata) {
            super.updateDeterminisedAutomata(automata);
        }

        @Override
        public void updateMinimizedAutomata(Automata automata) {
            super.updateMinimizedAutomata(automata);
        }
        
    }*/
    
    public static void main(String[] args){
        MainController controller = new MainController();
        MainView view = new MainView(controller);
        view.setVisible(true);
    }
}
