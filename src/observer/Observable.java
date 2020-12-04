/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

import beans.Automata;
import java.util.ArrayList;


/**
 *
 * @author Ndadji
 */
public interface Observable {
    
    public void addObserver(Observer observer);
    
    public void notifySuccess(String message);
    public void notifyError(String error);
    public void notifyAutomata(Automata automata);
    public void notifyDeterminisedAutomata(Automata automata);
    public void notifyMinimizedAutomata(Automata automata);
    public void notifyTestResult(String mot, boolean result, ArrayList<Object> trace);
}
