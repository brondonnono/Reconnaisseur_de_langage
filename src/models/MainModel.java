/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import beans.Analyser;
import beans.Automata;
import beans.Thompson;
import java.util.ArrayList;
import observer.adapter.ObservableAdapter;

/**
 *
 * @author Ndadji 
 */
public class MainModel extends ObservableAdapter{

    private Automata  thompson = null;
    private String regEx = null;
    
    /**
     * Methode permettant de construire l'automate et de notifier le resultat  a la vue chargee d'afficher le resultat.
     * toutes les methodes aui commencent par update ou notify permettent d'envoyer un resultat a la vue
     * @param regEx
     * @throws Exception 
     */
    public void compile(String regEx) throws Exception {
        this.regEx = regEx;
        thompson = Thompson.constructAutomata(regEx);
        notifyAutomata(thompson);
        determinise();
        minimise();
    }
    
    /**
     * 
     */
    public void determinise(){
        try {
            //thompson = Analyser.determiniseThompson(thompson);
            notifyDeterminisedAutomata(thompson);
        } catch (Exception ex) {
            
        }
    }
    
    public void minimise(){
        notifyMinimizedAutomata(thompson);
    }
    
    public void check(String mot) {
        try {
            ArrayList<Object> trace = new ArrayList<Object>();
            boolean result = Analyser.check(thompson, mot, thompson.getInitialState(), trace);
            notifyTestResult(mot, result, trace);
        } catch (Exception ex) {
            notifyError("Erreurs survenues");
        }
    }
    
    public String getRegEx() {
        return regEx;
    }
}
