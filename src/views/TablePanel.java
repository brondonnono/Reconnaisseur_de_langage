/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import beans.Analyser;
import beans.Automata;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Classe permettant d'afficher la table de transition d'un automate dans un JTable.
 * Le modèle du JTable est une instance de la classe TableModel.
 * 
 * @author Ndadji
 */
public class TablePanel extends JPanel{
    
    public TablePanel() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
    }

    /**
     * Redéssiner la table (JTable) avec un nouvel automate (donc une nouvelle table de
     * transition).
     * 
     * @param automata 
     */
    public void reset(Automata automata) {
        this.removeAll();
        this.validate();
        this.repaint();
        
        // Initialisation de la ligne des titres
        String[] titles = new String[Analyser.SYMBOLS.length + 1];
        titles[0] = "Q \\ S";
        for(int i = 1; i < Analyser.SYMBOLS.length + 1; i++)
            titles[i] = Analyser.SYMBOLS[i-1];
        
        // Création de la liste des transitions grâce à la table de transition.
        Object[][] data = new Object[automata.getStates().size()][titles.length],
                   array = automata.getTransitionTableAsArray();
        for(int i = 0; i < automata.getStates().size(); i++){
            JLabel label = new JLabel(automata.getStates().get(i).toString());
            if(automata.getStates().get(i).toString().equals(automata.getInitialState().toString()))
                label.setText(label.getText()+"(I)");
            if(automata.getFinalStates().contains(automata.getStates().get(i)))
                label.setText(label.getText()+"(F)");
            label.setOpaque(true);
            label.setFont(new Font("Times", Font.BOLD, 17));
            label.setHorizontalAlignment(JLabel.CENTER);
            data[i][0] = label;
            for(int j = 0; j < Analyser.SYMBOLS.length; j++){
                ArrayList<Integer> list = (ArrayList<Integer>)array[i][j];
                String str = "";
                for(Integer e : list)
                    str += e+", ";
                if(str.length() > 0)
                    str = str.substring(0, str.lastIndexOf(","));
                data[i][j + 1] = str;
            }
        }
        JTable table = new JTable(new TableModel(data, titles));
        table.setDefaultRenderer(Component.class, TableModel.getComponentRenderer());
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(21, 168, 180));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Times", Font.PLAIN, 16));
        table.getTableHeader().setFont(new Font("Times", Font.BOLD, 17));
        table.setOpaque(false);
        
        // Entête du tableau au nord du panneau pour la rendre fixe.
        this.add(table.getTableHeader(), BorderLayout.NORTH);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(400, 35));
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(null);
        this.add(pane);
        
        this.validate();
        this.repaint();
    }
}
