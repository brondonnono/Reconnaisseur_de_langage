/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import beans.Analyser;
import controllers.MainController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Cette classe permet d'afficher le panneau chargé de récupérer les données d'entrées.
 * L'alphabet, l'E.R et le mot à tester.
 *
 * @author Ndadji
 */
public class RegexPanel extends JPanel{
    private MainController controller;
    private JLabel label, langage;
    private JTextField regex;
    private JTextField word;
    private JTextField alpha;
    private JButton button;
    private ActionListener listener;

    public RegexPanel(MainController controller) {
        this.controller = controller;
        this.initComponents();
    }

    private void initComponents() {
        // Petit style pour l'animation des composants (boutons, labels, ...) au passage de la souris.
        MouseListener mouseListener = new MouseAdapter(){
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(((JComponent)e.getSource()).isEnabled())
                        ((JComponent)e.getSource()).setBackground(new Color(21, 168, 180));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ((JComponent)e.getSource()).setBackground(Color.GRAY);
                }
                
        };
        
        // Ecouteur des différents boutons pour la réalisation les différentes actions.
        listener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if(command.equals("compile")){
                    try{
                        controller.isRegexWellFormed(regex.getText());
                        langage.setForeground(Color.ORANGE.darker());
                        langage.setText("Langage");
                        word.setEnabled(true);
                    }catch(Exception ex){
                        langage.setForeground(Color.red);
                        langage.setText(ex.getMessage());
                    }
                    return;
                }
                
                if(command.equals("change")){
                    try{
                        controller.changeAlphabet(alpha.getText());
                        word.setEnabled(false);
                        langage.setForeground(Color.ORANGE.darker());
                        langage.setText("Langage");
                    }catch(RuntimeException ex){
                        langage.setForeground(Color.red);
                        langage.setText(ex.getMessage());
                        String init = "";
                        for(String str : Analyser.SYMBOLS)
                            if(!str.equals(Analyser.EPSILON))
                                init += "-"+str;
                        if(init.length() != 0)
                            init = init.substring(1);
                        alpha.setText(init);
                    }
                    return;
                }
                
                if(command.equals("check")){
                    try{
                        controller.check(word.getText());
                        langage.setForeground(Color.ORANGE.darker());
                        langage.setText("Langage");
                    }catch(RuntimeException ex){
                        langage.setForeground(Color.red);
                        langage.setText(ex.getMessage());
                    }
                    return;
                }
            }
            
        };
        
        langage = new JLabel("Langage");
        langage.setPreferredSize(new Dimension(190, 25));
        langage.setFont(new Font("Times", Font.PLAIN, 15));
        langage.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
        langage.setOpaque(true);
        langage.setIcon(new ImageIcon(getClass().getResource("/ressources/images/arrow.png")));
        langage.setForeground(Color.ORANGE.darker());
        this.add(langage);
        
        label = new JLabel("");
        label.setPreferredSize(new Dimension(190, 15));
        this.add(label);
        
        label = new JLabel("   Votre E.R (RegEx)");
        label.setPreferredSize(new Dimension(190, 25));
        label.setFont(new Font("Times", Font.PLAIN, 15));
        label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(21, 168, 180));
        this.add(label);
        
        regex = new JTextField("");
        regex.setPreferredSize(new Dimension(190, 30));
        regex.setForeground(Color.BLUE);
        regex.setHorizontalAlignment(JTextField.CENTER);
        regex.setFont(new Font("Times", Font.PLAIN, 19));
        this.add(regex);
        
        button = new JButton("Compiler l'E.R");
        button.setPreferredSize(new Dimension(190, 25));
        button.setFont(new Font("Times", Font.PLAIN, 15));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setActionCommand("compile");
        button.addActionListener(listener);
        this.add(button);
        
        label = new JLabel("");
        label.setPreferredSize(new Dimension(190, 20));
        this.add(label);
        
        label = new JLabel("   Votre Alphabet");
        label.setPreferredSize(new Dimension(190, 25));
        label.setFont(new Font("Times", Font.PLAIN, 15));
        label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(21, 168, 180));
        this.add(label);
        
        String init = "";
        for(String str : Analyser.SYMBOLS)
            if(!str.equals(Analyser.EPSILON))
                init += "-"+str;
        if(init.length() != 0)
            init = init.substring(1);
        alpha = new JTextField(init);
        alpha.setPreferredSize(new Dimension(190, 30));
        alpha.setForeground(Color.BLUE);
        alpha.setHorizontalAlignment(JTextField.CENTER);
        alpha.setFont(new Font("Times", Font.PLAIN, 19));
        this.add(alpha);
        
        button = new JButton("Changer l'alphabet");
        button.setPreferredSize(new Dimension(190, 25));
        button.setFont(new Font("Times", Font.PLAIN, 15));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setActionCommand("change");
        button.addActionListener(listener);
        this.add(button);
        
        label = new JLabel("");
        label.setPreferredSize(new Dimension(190, 20));
        this.add(label);
        
        label = new JLabel("   Votre Mot");
        label.setPreferredSize(new Dimension(190, 25));
        label.setFont(new Font("Times", Font.PLAIN, 15));
        label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLUE));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(21, 168, 180));
        this.add(label);
        
        word = new JTextField("");
        word.setPreferredSize(new Dimension(190, 30));
        word.setForeground(Color.BLUE);
        word.setEnabled(false);
        word.setHorizontalAlignment(JTextField.CENTER);
        word.setFont(new Font("Times", Font.PLAIN, 19));
        this.add(word);
        
        button = new JButton("Vérifier l'appartenance");
        button.setPreferredSize(new Dimension(190, 25));
        button.setFont(new Font("Times", Font.PLAIN, 15));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.addMouseListener(mouseListener);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setActionCommand("check");
        button.addActionListener(listener);
        this.add(button);
    }

    /**
     * Methode qui permet de peindre le panneau
     * 
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(250, 250, 250));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.GRAY.brighter());
        g.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
        g.fillRect(15, this.getHeight() - 75, this.getWidth() - 25, 60);
        g.setColor(Color.GREEN);
        g.fillRect(10, this.getHeight() - 70, this.getWidth() - 25, 60);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Cambria", Font.BOLD, 23));
        g.drawString("G TEAM", 60, this.getHeight() - 47);
        g.setFont(new Font("Cambria", Font.BOLD, 20));
        g.drawString("  2019  ", 70, this.getHeight() - 17);
    }
}
